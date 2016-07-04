package org.example.oauth2.model.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourcePathConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcePathConfig.class);

    private String path;
    private String target;
    private String requiredScope;


    public ResourcePathConfig() {
    }

    public ResourcePathConfig(String path, String target) {
        this.path = path;
        this.target = target;
    }

    public String getRequiredScope() {
        return requiredScope;
    }

    public void setRequiredScope(String requiredScope) {
        this.requiredScope = requiredScope;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String matches(final String prefix,
                           final String candidatePath) {

        final String prefixedPath = prefix + path;

        LOGGER.info("comparing [{}] to candidate [{}]", prefixedPath, candidatePath);

        final String[] candidateParts = candidatePath.split("/");
        final String[] pathParts = prefixedPath.split("/");
        if (candidateParts.length != pathParts.length) {
            return null;
        }

        for (int i = 0; i < pathParts.length; ++i) {
            final String candidatePart = candidateParts[i];
            final String pathPart = pathParts[i];
            if (!pathPartIsVariable(pathPart)) {
                if (!pathPart.equals(candidatePart)) {
                    return null;
                }
            }
        }
        return prefixedPath;
    }

    private static boolean pathPartIsVariable(final String pathPart) {
        return pathPart.startsWith("{") && pathPart.endsWith("}"); // FIXME: hacky
    }

    @Override
    public String toString() {
        return "ResourcePathConfig{" +
                "path='" + path + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
