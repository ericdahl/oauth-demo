package example.exceptions;

/**
 * Created by ecd on 3/5/16.
 */
public class NoSuchTokenException extends RuntimeException {
    public NoSuchTokenException() {
        super();
    }

    public NoSuchTokenException(String message) {
        super(message);
    }
}
