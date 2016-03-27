package org.example.oauth2.model;

public enum ErrorCode {

    INVALID_REQUEST("invalid_request");

    private final String message;

    ErrorCode(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
