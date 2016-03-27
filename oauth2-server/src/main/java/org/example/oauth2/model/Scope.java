package org.example.oauth2.model;

public class Scope {
    private final String scope;

    public Scope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }
}
