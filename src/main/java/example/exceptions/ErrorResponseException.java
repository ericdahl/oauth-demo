package example.exceptions;

import model.ErrorCode;
import model.ErrorResponse;

/**
 * Created by ecd on 3/20/16.
 */
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
