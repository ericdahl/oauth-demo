package org.example.oauth2.service;

import org.example.oauth2.exception.ResourceNotFoundException;
import org.example.oauth2.model.internal.ResolvedResource;
import org.example.oauth2.model.internal.ResourceConfig;
import org.example.oauth2.model.internal.ResourcePathConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResourceResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceResolver.class);

    private final ResourceConfig resourceConfig;
    private final String prefix;

    @Autowired
    public ResourceResolver(ResourceConfig resourceConfig,
                            @Value("${oauth2.resources.prefix:/go}") String prefix) {
        this.resourceConfig = resourceConfig;
        this.prefix = prefix;
    }

    public ResolvedResource resolve(final String path) {

        final ResourcePathConfig pathConfig = resourceConfig
                .findTarget(path)
                .orElseThrow(() -> new ResourceNotFoundException("No target for path [" + path + "]"));

        final String strippedPath = path.replace(prefix, "");
        LOGGER.info("Evaluating pathConfig [{}] / [{}] for path [{}]", pathConfig.getPath(), pathConfig.getTarget(), strippedPath);

        String target = pathConfig.getTarget();

        final String[] pathParts = pathConfig.getPath().split("/");
        final String[] inputPathPaths = strippedPath.split("/");

        for (int i = 0; i < pathParts.length; ++i) {
            final String pathPart = pathParts[i];
            if (pathPart.startsWith("{") && pathPart.endsWith("}")) {
                LOGGER.info("Evaluating path part [{}] with [{}]", pathPart, inputPathPaths[i]);
                target = target.replace(pathPart, inputPathPaths[i]);
            }
        }

        return new ResolvedResource(target, pathConfig);
    }
}
