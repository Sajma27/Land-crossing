package com.test.crossing.service;

import com.test.crossing.core.model.CrossingResult;
import com.test.crossing.core.service.CrossingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrossingServiceTest {

    private final ResponseStatusException badRequestException = new ResponseStatusException(HttpStatus.BAD_REQUEST);

    @Autowired
    private CrossingService crossingService;

    @Test
    public void countriesLoadedFromURL() throws NoSuchFieldException, IllegalAccessException {
        Field countriesField = CrossingService.class.getDeclaredField("countries");
        countriesField.setAccessible(true);
        assertNotNull(countriesField.get(crossingService));
        assertFalse(((HashMap)countriesField.get(crossingService)).isEmpty());
    }

    @Test
    public void shouldGetResultShort() {
        CrossingResult result = crossingService.getCrossing("CZE", "ITA");
        assertEquals(3, result.getRoute().size());
        assertEquals("CZE", result.getRoute().get(0));
        assertEquals("ITA", result.getRoute().get(2));
    }

    @Test
    public void shouldGetResultLong() {
        CrossingResult result = crossingService.getCrossing("TUR", "PRT");
        assertEquals(8, result.getRoute().size());
        assertEquals("TUR", result.getRoute().get(0));
        assertEquals("ESP", result.getRoute().get(result.getRoute().size() - 2));
        assertEquals("PRT", result.getRoute().get(result.getRoute().size() - 1));
    }

    @Test
    public void invalidOriginOrDestination() {
        assertThrows(badRequestException.getClass(), () -> crossingService.getCrossing(null, "PRT"));
        assertThrows(badRequestException.getClass(), () -> crossingService.getCrossing("NOT_EXISITNG", "PRT"));
        assertThrows(badRequestException.getClass(), () -> crossingService.getCrossing("POL", "NOT_EXISITNG"));
        assertThrows(badRequestException.getClass(), () -> crossingService.getCrossing("POL", null));
        assertThrows(badRequestException.getClass(), () -> crossingService.getCrossing("POL", "POL"));
        assertThrows(badRequestException.getClass(), () -> crossingService.getCrossing(null, null));
    }

    @Test
    public void originOrDestinationWithNoBoarder() {
        assertThrows(badRequestException.getClass(), () -> crossingService.getCrossing("POL", "ABW"));
        assertThrows(badRequestException.getClass(), () -> crossingService.getCrossing("ABW", "POL"));
    }

    @Test
    public void noLandCrossing() {
        assertThrows(badRequestException.getClass(), () -> crossingService.getCrossing("POL", "MEX"));
        assertThrows(badRequestException.getClass(), () -> crossingService.getCrossing("MEX", "POL"));
    }
}
