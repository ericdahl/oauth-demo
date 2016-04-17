package org.example.oauth2.model.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class ResourcePathConfig {

    private String path;
    private String target;


    public ResourcePathConfig() {
    }

    public ResourcePathConfig(String path, String target) {
        this.path = path;
        this.target = target;
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

    public boolean matches(final String prefix,
                           final String candidatePath) {
        return (prefix + path).equals(candidatePath);
    }

    @Override
    public String toString() {
        return "ResourcePathConfig{" +
                "path='" + path + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
