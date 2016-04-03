package org.example.oauth2.model.internal;

public class ResourcePathConfig {

    private String path;
    private String target;

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

    @Override
    public String toString() {
        return "ResourcePathConfig{" +
                "path='" + path + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
