package com.test.crossing.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.crossing.core.mapper.CountryMapper;
import com.test.crossing.core.model.Country;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class CountryMapperTest {

    private final CountryMapper countryMapper = new CountryMapper();

    @Test
    public void simpleMapping() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[" +
                "{" +
                "\"cca3\": \"EMPTY_BOARDER\"," +
                "\"borders\": []" +
                "}, " +
                "{" +
                "\"cca3\": \"TEST\"," +
                "\"borders\": [\"TEST1\"]" +
                "}, " +
                "{" +
                "\"cca3\": \"TEST1\"," +
                "\"borders\": [\"TEST\", \"TEST2\"]" +
                "}]");
        HashMap<String, Country> countries = countryMapper.readCountriesMap(jsonNode);
        assertNotNull(countries.get("EMPTY_BOARDER"));
        assertNotNull(countries.get("TEST"));
        assertNotNull(countries.get("TEST1"));
        assertNull(countries.get("NOT_EXISTING"));
        assertEquals(0, countries.get("EMPTY_BOARDER").getBorders().size());
        assertEquals(1, countries.get("TEST").getBorders().size());
        assertEquals(2, countries.get("TEST1").getBorders().size());
    }

    @Test
    public void invalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[" +
                "{" +
                "\"cca3\": \"INVALID\"" +
                "}, " +
                "{" +
                "\"borders\": [\"INVALID\"]" +
                "}]");
        assertThrows(NullPointerException.class, () -> countryMapper.readCountriesMap(jsonNode));
    }
}
