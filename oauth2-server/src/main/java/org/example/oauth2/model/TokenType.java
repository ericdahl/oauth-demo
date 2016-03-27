package org.example.oauth2.model;

public enum TokenType {
    BEARER("bearer");

    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TokenType getByName(String name) {
        if ("bearer".equals(name)) {
            return BEARER;
        }
        throw new IllegalArgumentException("No enum constant");

    }
}
