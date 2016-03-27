package org.example.oauth2.service;

import org.example.oauth2.exception.AuthorizationException;
import org.example.oauth2.exception.NoSuchAppException;
import org.example.oauth2.model.App;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AppService {

    private final Map<String, App> apps = new HashMap<String, App>();

    public AppService() {
        apps.put("myid", new App("myid", "mysecret", "myapp", "mydeveloperusername")); // TODO: remove
    }

    public App register(String name, String developerUsername) {
        final App app = new App(UUID.randomUUID().toString(), UUID.randomUUID().toString(), name, developerUsername);
        apps.put(app.getClientId(), app);
        return app;
    }

    public App getById(String clientId) {

        final App app = apps.get(clientId);
        if (app == null) {
            throw new NoSuchAppException("Client id [" + clientId + "] does not exist");
        }
        return app;
    }

    public App authenticate(final String clientId, final String clientSecret) {
        final App app = getById(clientId);
        if (!clientSecret.equals(app.getClientSecret())) {
            throw new AuthorizationException("invalid client secret");
        }
        return app;
    }

}
