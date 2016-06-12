package org.example.oauth2.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.oauth2.model.Token;

public class InternalTokenData {

    private final Token token;

    @JsonCreator
    public InternalTokenData(@JsonProperty("token") final Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
