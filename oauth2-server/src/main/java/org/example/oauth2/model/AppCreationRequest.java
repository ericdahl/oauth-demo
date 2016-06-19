package org.example.oauth2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AppCreationRequest {
    private final String name;
    private final Developer developer;

    @JsonCreator
    public AppCreationRequest(@JsonProperty("name") String name,
                              @JsonProperty("developer") Developer developer) {
        this.name = name;
        this.developer = developer;
    }

    public String getName() {
        return name;
    }

    public Developer getDeveloper() {
        return developer;
    }



}
