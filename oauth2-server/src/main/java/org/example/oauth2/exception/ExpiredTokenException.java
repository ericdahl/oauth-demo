package org.example.oauth2.exception;

import org.example.oauth2.model.Token;

public class ExpiredTokenException extends AuthorizationException {

    private final Token expiredToken;

    public ExpiredTokenException(Token token) {
        super("Token [" + token + "] is expired");
        this.expiredToken = token;
    }

    public Token getExpiredToken() {
        return expiredToken;
    }
}
