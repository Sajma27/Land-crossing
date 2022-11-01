package com.test.crossing.core.model;

import java.util.List;

public class CrossingResult {

    private List<String> route;

    public CrossingResult(List<String> route) {
        this.route = route;
    }

    public List<String> getRoute() {
        return route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }
}
