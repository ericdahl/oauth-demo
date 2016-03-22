package example.service;

import example.exceptions.NoSuchAppException;
import example.model.App;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AppService {

    private final Map<String, App> apps = new HashMap<String, App>();

    public AppService() {
        apps.put("myid", new App("myid", "myapp", "mydeveloperusername")); // TODO: remove
    }

    public App register(String name, String developerUsername) {
        final App app = new App(UUID.randomUUID().toString(), name, developerUsername);
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

}
