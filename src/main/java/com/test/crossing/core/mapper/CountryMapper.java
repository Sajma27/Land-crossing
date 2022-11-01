package com.test.crossing.core.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.crossing.core.model.Country;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

@Service
public class CountryMapper {

    public HashMap<String, Country> readCountriesMap(URL url) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(url);
        HashMap<String, Country> countries = new HashMap<>();
        Iterator<JsonNode> nodes = jsonNode.elements();
        while (nodes.hasNext()) {
            JsonNode node = nodes.next();
            Country nextCountry = new Country();
            nextCountry.setCca3(node.get("cca3").asText());
            HashMap<String, Country> boarders = new HashMap<>();
            Iterator<JsonNode> bordersIt = node.get("borders").elements();
            while (bordersIt.hasNext()) {
                boarders.put(bordersIt.next().asText(), null);
            }
            nextCountry.setBorders(boarders);
            countries.put(nextCountry.getCca3(), nextCountry);
        }
        countries.forEach((cca3, country) -> {
            for (String boarderCca3 : country.getBorders().keySet()) {
                country.getBorders().replace(boarderCca3, countries.get(boarderCca3));
            }
        });
        return countries;
    }

}
