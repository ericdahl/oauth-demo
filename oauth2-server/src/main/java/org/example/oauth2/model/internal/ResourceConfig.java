package org.example.oauth2.model.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "oauth2")
public class ResourceConfig {

    private List<ResourcePathConfig> resources = new ArrayList<ResourcePathConfig>();

    public List<ResourcePathConfig> getResources() {
        return resources;
    }

    public void setResources(List<ResourcePathConfig> resourceConfigs) {
        this.resources = resourceConfigs;
    }

    @Override
    public String toString() {
        return "ResourceListConfig{" +
                "resources=" + resources +
                '}';
    }

    public String findPath(String path) {
        for (ResourcePathConfig resourcePathConfig : resources) {
            if (resourcePathConfig.getPath().equals(path)) {
                return resourcePathConfig.getTarget();
            }
        }
        return null;
    }
}
