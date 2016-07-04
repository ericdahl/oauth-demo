package org.example.oauth2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class App {
    private final String name;
    private final String clientId;
    private final String clientSecret;
    private final Set<String> scopes;
    private final Developer developer;

    public App(String clientId,
               String clientSecret,
               String name,
               Developer developer,
               Set<String> scopes) {

        this.clientId = clientId;
        this.name = name;
        this.clientSecret = clientSecret;
        this.developer = developer;
        this.scopes = Collections.unmodifiableSet(new HashSet<>(scopes));
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
