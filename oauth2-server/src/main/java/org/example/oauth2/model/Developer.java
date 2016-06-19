package org.example.oauth2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Developer {

    private String name;

    @JsonCreator
    public Developer(@JsonProperty("name") final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
