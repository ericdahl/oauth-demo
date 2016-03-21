package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    // TODO:
    //  - token expiration
    //  - refresh token
    //  - scopes
    //      - manage resources scope
    //  - oauth filter?
    //  - authorization code grant type
    //  - web UI for apps?
    //

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
