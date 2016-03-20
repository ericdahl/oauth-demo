package example.exceptions;

import model.ErrorResponse;

/**
 * Created by ecd on 3/20/16.
 */
public class ErrorResponseException extends RuntimeException {

    private final String error;

    public ErrorResponseException(final String error) {
        this.error = error;
    }

    public ErrorResponse getError() {
        return new ErrorResponse(error);
    }
}
