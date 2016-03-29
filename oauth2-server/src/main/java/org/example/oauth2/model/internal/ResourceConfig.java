package org.example.oauth2.model.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "oauth2")
public class ResourceConfig {

//    List<Resource> resources = new ArrayList<>();

    private List<ResourceConfig> resources = new ArrayList<ResourceConfig>();

    public List<ResourceConfig> getResources() {
        return resources;
    }

    public void setResources(List<ResourceConfig> resourceConfigs) {
        this.resources = resourceConfigs;
    }

    @Override
    public String toString() {
        return "ResourceListConfig{" +
                "resources=" + resources +
                '}';
    }
}
