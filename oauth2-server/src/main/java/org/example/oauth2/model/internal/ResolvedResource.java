package org.example.oauth2.model.internal;

public class ResolvedResource {

    private final String target;
    private final ResourcePathConfig config;


    public ResolvedResource(final String target,
                            final ResourcePathConfig config) {
        this.target = target;
        this.config = config;
    }

    public String getTarget() {
        return target;
    }

    public ResourcePathConfig getConfig() {
        return config;
    }

    public String getRequiredScope() {
        return config.getRequiredScope();
    }
}
