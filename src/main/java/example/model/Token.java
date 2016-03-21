package example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    private final String accessToken;
    private final String clientId;
    private final String username;
    private final TokenType tokenType;

    @JsonCreator
    public Token(@JsonProperty("access_token") String accessToken,
                 @JsonProperty("token_type") String tokenType,
                 @JsonProperty("client_id") String clientId,
                 @JsonProperty("username") String username) {
        this.accessToken = accessToken;
        this.tokenType = TokenType.getByName(tokenType);
        this.clientId = clientId;
        this.username = username;
    }


    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
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

}
