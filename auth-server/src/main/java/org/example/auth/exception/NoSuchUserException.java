package org.example.auth.exception;

public class NoSuchUserException extends AuthorizationException {
    public NoSuchUserException() {
        super();
    }

    public NoSuchUserException(String message) {
        super(message);
    }
}
