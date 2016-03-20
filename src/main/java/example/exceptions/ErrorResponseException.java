package example.exceptions;

import model.ErrorCode;
import model.ErrorResponse;

/**
 * Created by ecd on 3/20/16.
 */
public class ErrorResponseException extends RuntimeException {

    private final ErrorCode error;

    public ErrorResponseException(final ErrorCode error) {
        this.error = error;
    }

    public ErrorResponse getError() {
        return new ErrorResponse(error);
    }
}
