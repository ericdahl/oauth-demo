package org.example.oauth2.dao;

import org.example.oauth2.exception.DuplicateAppException;
import org.example.oauth2.model.App;
import org.example.oauth2.model.Developer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AppDAO {

    private final Map<String, App> apps = new HashMap<>();
    private final Map<String, App> appsByAppName = new HashMap<>();

    public AppDAO() {
        final Developer developer = new Developer("mydeveloperusername");
        save(new App("myid", "mysecret", "myapp", developer)); // TODO: remove
    }


    public void save(App app) {
        if (apps.containsKey(app.getClientId())) {
            throw new DuplicateAppException("App clientId already in use");
        }
        if (appsByAppName.containsKey(app.getName())) {
            throw new DuplicateAppException("App name already in use");
        }

        apps.put(app.getClientId(), app);
        appsByAppName.put(app.getName(), app);
    }

    public App get(String clientId) {
        return apps.get(clientId);
    }

    public App getByName(final String name) {
        return apps.get(name);
    }

    public Set<String> getClientIds() {
        return apps.keySet();
    }
}
