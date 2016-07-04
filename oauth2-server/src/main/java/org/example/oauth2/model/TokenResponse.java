package org.example.oauth2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class TokenResponse {

    private final String accessToken;
    private final TokenType tokenType;
    private final Set<String> scopes;
    private final long expiresIn;

    public TokenResponse(@JsonProperty("access_token") final String accessToken,
                         @JsonProperty("scopes") final Set<String> scopes,
                         final TokenType tokenType,
                         final long expiresIn) {
        this.accessToken = accessToken;
        this.scopes = scopes;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public TokenResponse(final Token token) {
        this.accessToken = token.getAccessToken();
        this.tokenType = token.getTokenType();
        this.expiresIn = token.getExpiresIn();
        this.scopes = new HashSet<>(token.getScopes());
    }

    @JsonProperty("scope")
    public Set<String> getScopes() {
        return scopes;
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
