package org.example.oauth2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class App {
    private final String name;
    private final String clientId;
    private final String clientSecret;
    private final String developerUsername;

    public App(String clientId, String clientSecret, String name, String developerUsername) {

        this.clientId = clientId;
        this.name = name;
        this.clientSecret = clientSecret;
        this.developerUsername = developerUsername;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("client_id")
    public String getClientId() {
        return clientId;
    }

//    @JsonIgnore
    @JsonProperty("client_secret")
    public String getClientSecret() {
        return clientSecret;
    }

    public String getDeveloperUsername() {
        return developerUsername;
    }
}
