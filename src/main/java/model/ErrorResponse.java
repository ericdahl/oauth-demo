package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
    private final ErrorCode error;


    @JsonCreator
    public ErrorResponse(@JsonProperty("error") final ErrorCode error) {
        this.error = error;
    }

    public String getError() {
        return error.getMessage();
    }
}
