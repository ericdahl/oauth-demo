package org.example.oauth2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    private final String accessToken;
    private final String clientId;
    private final String username;
    private final TokenType tokenType;

    private final long generatedAt;
    private final long expiresIn;

    @JsonCreator
    public Token(@JsonProperty("access_token") String accessToken,
                 @JsonProperty("token_type") String tokenType,
                 @JsonProperty("client_id") String clientId,
                 @JsonProperty("username") String username,
                 @JsonProperty("generatedAt") long generatedAtMillis,
                 @JsonProperty("expires_in") long expiresInSeconds) {
        this.accessToken = accessToken;
        this.tokenType = TokenType.getByName(tokenType);
        this.clientId = clientId;
        this.username = username;
        this.generatedAt = generatedAtMillis;
        this.expiresIn = expiresInSeconds;
    }


    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("expires_in")
    public long getExpiresIn() {
        return expiresIn;
    }

    public long getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }

    public String getClientId() {
        return clientId;
    }

    public String getUsername() {
        return username;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    private long getExpiresAt() {
        return generatedAt + expiresIn * 1000;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > getExpiresAt();
    }
}
