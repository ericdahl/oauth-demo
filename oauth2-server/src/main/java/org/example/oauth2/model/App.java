package org.example.oauth2.model;

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

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getDeveloperUsername() {
        return developerUsername;
    }
}
