package org.example.oauth2.model.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@ConfigurationProperties(prefix = "oauth2")
public class ResourceConfig {

    @Value("${oauth2.resources.prefix:/go}")
    private String resourcePrefix;

    private List<ResourcePathConfig> resources = new ArrayList<>();

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

    public Optional<ResourcePathConfig> findTarget(String path) {

        for (ResourcePathConfig resourcePathConfig : resources) {
            if (resourcePathConfig.matches(resourcePrefix, path) != null) {
                return Optional.of(resourcePathConfig);
            }
        }
        return Optional.empty();
    }
}
