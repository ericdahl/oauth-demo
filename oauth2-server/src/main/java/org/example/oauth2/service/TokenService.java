package org.example.oauth2.service;

import org.example.oauth2.exception.ExpiredTokenException;
import org.example.oauth2.exception.NoSuchTokenException;
import org.example.oauth2.model.Token;
import org.example.oauth2.model.TokenType;
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