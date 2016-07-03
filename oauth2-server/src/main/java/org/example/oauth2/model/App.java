package org.example.oauth2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class App {
    private final String name;
    private final String clientId;
    private final String clientSecret;
    private final Developer developer;

    public App(String clientId, String clientSecret, String name, Developer developer) {

        this.clientId = clientId;
        this.name = name;
        this.clientSecret = clientSecret;
        this.developer = developer;
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

    @JsonProperty("developer")
    public Developer getDeveloper() {
        return developer;
    }
}
