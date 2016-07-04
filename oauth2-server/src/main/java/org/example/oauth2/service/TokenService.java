package org.example.oauth2.service;

import org.example.oauth2.dao.TokenDAO;
import org.example.oauth2.exception.ExpiredTokenException;
import org.example.oauth2.model.Token;
import org.example.oauth2.model.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenDAO tokenDAO;

    private static final Set<String> DEFAULT_SCOPES;
    static {
        Set<String> scopes = new HashSet<>();
        scopes.add("todos:read");
        DEFAULT_SCOPES = Collections.unmodifiableSet(scopes);
    }

    @Value("${accessToken.expiresInSeconds:3600}")
    private long expiresIn;

    @Autowired
    public TokenService(final TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    public Token generate(String clientId, String username) {
        final Token token = new Token(UUID.randomUUID().toString(), TokenType.BEARER.getName(), clientId, username, System.currentTimeMillis(), expiresIn, DEFAULT_SCOPES);
        tokenDAO.add(token);
        return token;
    }

    public Token generate(String clientId, String username, final Set<String> scopes) {
        final Token token = new Token(UUID.randomUUID().toString(), TokenType.BEARER.getName(), clientId, username, System.currentTimeMillis(), expiresIn, scopes);
        tokenDAO.add(token);
        return token;
    }

    public Token validate(final String accessToken) {
        final Token token = tokenDAO.getToken(accessToken);

        if (token.isExpired()) {
            throw new ExpiredTokenException(token);
        }

        return token;
    }
}
