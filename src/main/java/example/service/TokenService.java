package example.service;

import example.exceptions.NoSuchTokenException;
import example.model.Scope;
import example.model.Token;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TokenService {

    private final Map<String, Token> tokens = new HashMap<String, Token>();

    public Token generate() {
        Token token = new Token(UUID.randomUUID().toString());
        tokens.put(token.getAccessToken(), token);
        return token;
    }

    public Token validate(String accessToken) {
        Token token = tokens.get(accessToken);
        if (token == null) {
            throw new NoSuchTokenException();
        }

        return token;
    }
}
