package org.example.oauth2.exception;


import org.example.oauth2.model.ErrorCode;
import org.example.oauth2.model.ErrorResponse;

public class ErrorResponseException extends RuntimeException {

    private final ErrorCode error;
    private final String description;

    public ErrorResponseException(final ErrorCode error, String description) {
        this.error = error;
        this.description = description;
    }

    public ErrorResponse getError() {
        return new ErrorResponse(error, description);
    }
}
