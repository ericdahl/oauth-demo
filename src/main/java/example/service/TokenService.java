package example.service;

import example.exceptions.ExpiredTokenException;
import example.exceptions.NoSuchTokenException;
import example.model.Token;
import example.model.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TokenService {

    private final Map<String, Token> tokens = new HashMap<String, Token>();

    @Value("${accessToken.expiresInSeconds:3600}")
    private long expiresIn;


    public Token generate(String clientId, String username) {
        Token token = new Token(UUID.randomUUID().toString(), TokenType.BEARER.getName(), clientId, username, System.currentTimeMillis(), expiresIn);
        tokens.put(token.getAccessToken(), token);
        return token;
    }

    public Token validate(String accessToken) {
        Token token = tokens.get(accessToken);
        if (token == null) {
            throw new NoSuchTokenException();
        } else if (token.isExpired()) {
            throw new ExpiredTokenException(token);
        }


        return token;
    }
}
