package org.example.oauth2.exception;

public class DuplicateAppException extends RuntimeException {
    public DuplicateAppException(String message) {
        super(message);
    }
}
