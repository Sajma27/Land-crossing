package com.test.crossing.api;

import com.test.crossing.core.model.CrossingResult;
import com.test.crossing.core.service.CrossingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/routing")
public class CrossingController {

    private final CrossingService crossingService;

    @Autowired
    public CrossingController(CrossingService crossingService) {
        this.crossingService = crossingService;
    }

    @RequestMapping(value = "/{origin}/{dest}", method = RequestMethod.GET)
    public @ResponseBody CrossingResult get(@PathVariable("origin") String origin, @PathVariable("dest") String dest) {
        return crossingService.getCrossing(origin, dest);
    }
}
