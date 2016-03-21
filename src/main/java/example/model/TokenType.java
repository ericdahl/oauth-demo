package example.model;

import java.util.IllegalFormatCodePointException;

/**
 * Created by ecd on 3/20/16.
 */
public enum TokenType {
    BEARER("bearer");

    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TokenType getByName(String name) {
        if ("bearer".equals(name)) {
            return BEARER;
        }
        throw new IllegalArgumentException("No enum constant");

    }
}
