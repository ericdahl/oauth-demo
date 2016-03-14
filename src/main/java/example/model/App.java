package example.model;

public class App {
    private final String name;
    private final String clientId;
    private final String developerUsername;

    public App(String clientId, String name, String developerUsername) {

        this.clientId = clientId;
        this.name = name;
        this.developerUsername = developerUsername;
    }

    public String getName() {
        return name;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDeveloperUsername() {
        return developerUsername;
    }
}
