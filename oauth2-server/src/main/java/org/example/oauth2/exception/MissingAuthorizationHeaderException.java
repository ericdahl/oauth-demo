package org.example.oauth2.exception;

public class MissingAuthorizationHeaderException extends RuntimeException {

    public MissingAuthorizationHeaderException() {
        super();
    }

    public MissingAuthorizationHeaderException(String message) {
        super(message);
    }
}
