package example.service;

import example.model.App;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AppService {

    private final Map<String, App> apps = new HashMap<String, App>();

    public AppService() {
        apps.put("myid", new App("myid", "myapp")); // TODO: remove
    }

    public App register(String name) {
        final App app = new App(UUID.randomUUID().toString(), name);
        apps.put(app.getClientId(), app);
        return app;
    }

    public App getById(String clientId) {
        return apps.get(clientId);
    }

}
