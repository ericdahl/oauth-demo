package org.example.oauth2.dao;

import org.example.oauth2.exception.NoSuchTokenException;
import org.example.oauth2.model.Token;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class TokenDAO {

    private final Map<String, Token> tokens = new HashMap<>();

    public Token getToken(String accessToken) {

        final Token token = tokens.get(accessToken);
        if (token == null) {
            throw new NoSuchTokenException(accessToken);
        }
        return token;
    }

    public void add(final Token token) {
        tokens.put(token.getAccessToken(), token);
    }

    public Set<String> getTokens() {
        return tokens.keySet();
    }
}
