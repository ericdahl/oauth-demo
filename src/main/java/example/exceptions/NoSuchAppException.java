package example.exceptions;

public class NoSuchAppException extends ResourceNotFoundException {

    public NoSuchAppException(String message) {
        super(message);
    }

    public NoSuchAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
