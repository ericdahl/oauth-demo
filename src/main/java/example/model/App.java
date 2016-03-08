package example.model;

public class App {
    private final String name;
    private final String clientId;

    public App(String clientId, String name) {

        this.clientId = clientId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getClientId() {
        return clientId;
    }

}
