package example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {

    private final String accessToken;
    private final TokenType tokenType;

    public TokenResponse(@JsonProperty("access_token") String accessToken, TokenType tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public TokenResponse(Token token) {
        this.accessToken = token.getAccessToken();
        this.tokenType = token.getTokenType();
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType.getName();
    }
}
