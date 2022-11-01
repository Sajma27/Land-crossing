package com.test.crossing.core.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Country {
    private String cca3;
    private HashMap<String, Country> borders;

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
}
