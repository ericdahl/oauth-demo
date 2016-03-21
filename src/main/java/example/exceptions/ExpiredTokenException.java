package example.exceptions;

import example.model.Token;

/**
 * Created by ecd on 3/20/16.
 */
public class ExpiredTokenException extends AuthorizationException {

    private final Token expiredToken;

    public ExpiredTokenException(Token token) {
        this.expiredToken = token;
    }
}
