package org.example.oauth2.dao;

import org.example.oauth2.model.Token;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenDAO {

    private final Map<String, Token> tokens = new HashMap<>();

    public Token getToken(String accessToken) {
        return tokens.get(accessToken);
    }

    public void add(final Token token) {
        tokens.put(token.getAccessToken(), token);
    }
}
