package org.example.oauth2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {


    private final ErrorCode error;
    private final String description;


    @JsonCreator
    public ErrorResponse(@JsonProperty("error") final ErrorCode error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error.getMessage();
    }

    @JsonProperty("error_description")
    public String getErrorDescription() {
        return description;
    }
}
