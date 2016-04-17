package org.example.oauth2.service;

import org.example.oauth2.exception.MissingAuthorizationHeaderException;
import org.example.oauth2.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthTokenValidationService {

    private static final Pattern HEADER_PATTERN = Pattern.compile("^Bearer ([a-z0-9-]+)$");

    private final TokenService tokenService;

    @Autowired
    public AuthTokenValidationService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Token validate(final String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new MissingAuthorizationHeaderException();
        }

        final Matcher matcher = HEADER_PATTERN.matcher(authorizationHeader);
        if (!matcher.matches()) {
            throw new MissingAuthorizationHeaderException("Authorization header [" + authorizationHeader + "] is malformed");
        }
        return tokenService.validate(matcher.group(1));
    }
}
