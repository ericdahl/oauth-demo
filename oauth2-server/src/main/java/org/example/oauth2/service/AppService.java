package org.example.oauth2.service;

import org.example.oauth2.dao.AppDAO;
import org.example.oauth2.exception.AuthorizationException;
import org.example.oauth2.exception.NoSuchAppException;
import org.example.oauth2.model.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AppService {

    private final AppDAO appDAO;

    @Autowired
    public AppService(AppDAO appDAO) {
        this.appDAO = appDAO;
    }

    public App register(String name, String developerUsername) {
        final App app = new App(UUID.randomUUID().toString(), UUID.randomUUID().toString(), name, developerUsername);
        appDAO.save(app);
        return app;
    }

    public App getById(String clientId) {

        final App app = appDAO.get(clientId);
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
