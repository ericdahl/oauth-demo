package org.example.oauth2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {

    private final String accessToken;
    private final TokenType tokenType;
    private final long expiresIn;

    public TokenResponse(@JsonProperty("access_token") String accessToken,
                         TokenType tokenType,
                         final long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public TokenResponse(Token token) {
        this.accessToken = token.getAccessToken();
        this.tokenType = token.getTokenType();
        this.expiresIn = token.getExpiresIn();
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType.getName();
    }

    @JsonProperty("expires_in")
    public long getExpiresIn() {
        return expiresIn;
    }
}
