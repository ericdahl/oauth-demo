package example.exceptions;

/**
 * Created by ecd on 3/13/16.
 */
public class InvalidPasswordException extends AuthorizationException {
    public InvalidPasswordException() {
        super();
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
