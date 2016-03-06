package example.exceptions;

/**
 * Created by ecd on 3/5/16.
 */
public class MissingAuthorizationHeaderException extends RuntimeException {

    public MissingAuthorizationHeaderException() {
        super();
    }

    public MissingAuthorizationHeaderException(String message) {
        super(message);
    }
}
