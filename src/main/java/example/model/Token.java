package example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    private final String accessToken;

    @JsonCreator
    public Token(@JsonProperty("access_token") String accessToken) {
        this.accessToken = accessToken;
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
}
