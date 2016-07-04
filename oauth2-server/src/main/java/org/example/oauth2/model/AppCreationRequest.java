package org.example.oauth2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

public class AppCreationRequest {

    @NotNull
    private final String name;

    @NotNull
    private final Set<String> scopes;

    @NotNull @Valid
    private final Developer developer;

    @JsonCreator
    public AppCreationRequest(@JsonProperty("name") final String name,
                              @JsonProperty("scopes") final Set<String> scopes,
                              @JsonProperty("developer") final Developer developer) {
        this.name = name;
        this.scopes = scopes;
        this.developer = developer;
    }

    public String getName() {
        return name;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public Set<String> getScopes() {
        return scopes;
    }
}
