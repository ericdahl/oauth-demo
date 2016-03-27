package org.example.auth.exception;

public class InvalidPasswordException extends AuthorizationException {
    public InvalidPasswordException() {
        super();
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
