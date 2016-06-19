package org.example.oauth2.dao;

import org.example.oauth2.model.App;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AppDAO {

    private final Map<String, App> apps = new HashMap<>();

    public AppDAO() {
        apps.put("myid", new App("myid", "mysecret", "myapp", "mydeveloperusername")); // TODO: remove
    }


    public void save(App app) {
        apps.put(app.getClientId(), app);
    }

    public App get(String clientId) {
        return apps.get(clientId);
    }

    public Set<String> getClientIds() {
        return apps.keySet();
    }
}
