package org.example.oauth2.exception;

public class NoSuchTokenException extends RuntimeException {
    public NoSuchTokenException() {
        super();
    }

    public NoSuchTokenException(String message) {
        super(message);
    }
}
