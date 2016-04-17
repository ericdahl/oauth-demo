package org.example.oauth2.exception;

public class NoSuchTokenException extends RuntimeException {

    private String invalidToken;

    public NoSuchTokenException(final String invalidToken) {
        super("No token found for [" + invalidToken + "]");
        this.invalidToken = invalidToken;
    }

    public String getExpiredToken() {
        return invalidToken;
    }
}
