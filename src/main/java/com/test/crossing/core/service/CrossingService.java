package com.test.crossing.core.service;

import com.test.crossing.core.mapper.CountryMapper;
import com.test.crossing.core.model.Country;
import com.test.crossing.core.model.CrossingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
public class CrossingService {
    private final HashMap<String, Country> countries;

    @Autowired
    public CrossingService(CountryMapper countryMapper) throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/mledoze/countries/master/countries.json");
        this.countries = countryMapper.readCountriesMap(url);
    }

    public CrossingResult getCrossing(String origin, String destination) {
        if (isOriginOrDestInvalid(origin, destination)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Country originCountry = countries.get(origin);
        Country destCountry = countries.get(destination);
        if (isOriginOrDestCountryInvalid(originCountry, destCountry)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Deque<Country> countriesToCheck = new ArrayDeque<>();
        Set<String> visitedCca3 = new HashSet<>();
        originCountry.setRoute(List.of(origin));
        updateCountriesToCheck(originCountry, countriesToCheck, visitedCca3, destCountry);
        while (!countriesToCheck.isEmpty()) {
            Country nextCountry = countriesToCheck.removeFirst();
            if (nextCountry.getCca3().equals(destCountry.getCca3())) {
                return new CrossingResult(nextCountry.getRoute());
            }
            updateCountriesToCheck(nextCountry, countriesToCheck, visitedCca3, destCountry);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    private boolean isOriginOrDestInvalid(String origin, String destination) {
        return origin == null || destination == null || origin.equals(destination);
    }

    private boolean isOriginOrDestCountryInvalid(Country originCountry, Country destCountry) {
        return originCountry == null || destCountry == null ||
                originCountry.getBorders().size() < 1 || destCountry.getBorders().size() < 1;
    }

    private void updateCountriesToCheck(Country country, Deque<Country> countriesToCheck, Set<String> visitedCca3, Country destCountry) {
        for (Country borderCountry : country.getBorders().values()) {
            if (visitedCca3.contains(borderCountry.getCca3())) {
                continue;
            }
            if (destCountry.getBorders().containsKey(borderCountry.getCca3())) {
                countriesToCheck.addFirst(borderCountry);
            } else {
                countriesToCheck.add(borderCountry);
            }
            visitedCca3.add(borderCountry.getCca3());
            List<String> route = new ArrayList<>(List.copyOf(country.getRoute()));
            route.add(borderCountry.getCca3());
            borderCountry.setRoute(route);
        }
    }

}
