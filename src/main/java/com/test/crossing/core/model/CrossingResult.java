package com.test.crossing.core.model;

import java.util.LinkedList;

public class CrossingResult {

    private LinkedList<String> route = new LinkedList<>();

    public CrossingResult() {
    }

    public CrossingResult(LinkedList<String> route) {
        this.route = route;
    }

    public LinkedList<String> getRoute() {
        return route;
    }

    public void setRoute(LinkedList<String> route) {
        this.route = route;
    }
}
