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
        Set<String> visitedCca3 = new HashSet<>();
        LinkedList<String> resultList = checkCountry(visitedCca3, originCountry, destCountry);
        if (resultList != null) {
            return new CrossingResult(resultList);
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

    private LinkedList<String> checkCountry(Set<String> visitedCca3, Country country, Country destCountry) {
        if (visitedCca3.contains(country.getCca3())) {
            return null;
        }
        if (country.getCca3().equals(destCountry.getCca3())) {
            LinkedList<String> destinationList = new LinkedList<>();
            destinationList.add(destCountry.getCca3());
            return destinationList;
        }
        visitedCca3.add(country.getCca3());
        for (Country borderCountry : country.getBorders().values()) {
            LinkedList<String> result = checkCountry(visitedCca3, borderCountry, destCountry);
            if (result != null) {
                result.addFirst(country.getCca3());
                return result;
            }
        }
        return null;
    }

}
