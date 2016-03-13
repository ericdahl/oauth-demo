package example.exceptions;

/**
 * Created by ecd on 3/13/16.
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
