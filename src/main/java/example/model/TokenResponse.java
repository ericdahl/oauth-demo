package example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {

    private final String accessToken;

    public TokenResponse(@JsonProperty("access_token") String accessToken) {
        this.accessToken = accessToken;
    }

    public TokenResponse(Token token) {
        this.accessToken = token.getAccessToken();
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }
}
