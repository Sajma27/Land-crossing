package com.test.crossing.core.model;

import java.util.HashMap;
import java.util.List;

public class Country {
    private String cca3;
    private HashMap<String, Country> borders;
    private List<String> route = null;

    public String getCca3() {
        return cca3;
    }

    public void setCca3(String cca3) {
        this.cca3 = cca3;
    }

    public HashMap<String, Country> getBorders() {
        return borders;
    }

    public void setBorders(HashMap<String, Country> borders) {
        this.borders = borders;
    }

    public List<String> getRoute() {
        return route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }
}
