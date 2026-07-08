package hr.ht.rnd.wifiadmin.env.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents development environment configuration.
 */
public record Environment(
        String aesKey,
        String jwtSecret,
        String soapEndpoint,
        String dbHost,
        String dbPort,
        String dbName,
        String dbUser,
        String dbPassword,
        String adminUsername,
        String adminPassword
) {
    /**
     * Creates a new environment.
     *
     * @throws NullPointerException if any value is {@code null}
     * @throws IllegalArgumentException if any value is blank
     */
    public Environment {
        aesKey = requireNotBlank(aesKey, "aesKey");
        jwtSecret = requireNotBlank(jwtSecret, "jwtSecret");
        soapEndpoint = requireNotBlank(soapEndpoint, "soapEndpoint");
        dbHost = requireNotBlank(dbHost, "dbHost");
        dbPort = requireNotBlank(dbPort, "dbPort");
        dbName = requireNotBlank(dbName, "dbName");
        dbUser = requireNotBlank(dbUser, "dbUser");
        dbPassword = requireNotBlank(dbPassword, "dbPassword");
        adminUsername = requireNotBlank(adminUsername, "adminUsername");
        adminPassword = requireNotBlank(adminPassword, "adminPassword");
    }

    /**
     * Returns this environment
     * as a map of environment variables.
     */
    public Map<String, String> toMap() {
        var values = new LinkedHashMap<String, String>();

        values.put(Variable.AES_KEY.name(), aesKey);
        values.put(Variable.JWT_SECRET.name(), jwtSecret);
        values.put(Variable.SOAP_ENDPOINT.name(), soapEndpoint);
        values.put(Variable.DB_HOST.name(), dbHost);
        values.put(Variable.DB_PORT.name(), dbPort);
        values.put(Variable.DB_NAME.name(), dbName);
        values.put(Variable.DB_USER.name(), dbUser);
        values.put(Variable.DB_PASSWORD.name(), dbPassword);
        values.put(Variable.ADMIN_USERNAME.name(), adminUsername);
        values.put(Variable.ADMIN_PASSWORD.name(), adminPassword);

        return values;
    }

    private static String requireNotBlank(String value, String name) {
        Objects.requireNonNull(value, name + " should not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " should not be blank");
        }
        return value;
    }

    /**
     * Environment variable.
     */
    public enum Variable {
        AES_KEY(""),
        JWT_SECRET(""),
        SOAP_ENDPOINT("http://localhost:8080/platform"),
        DB_HOST("localhost"),
        DB_PORT("5432"),
        DB_NAME("wifi_admin"),
        DB_USER("admin"),
        DB_PASSWORD("admin"),
        ADMIN_USERNAME("admin"),
        ADMIN_PASSWORD("admin");

        private final String defaultValue;

        Variable(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        /**
         * Returns the default value
         * of this environment variable.
         *
         * @return default value or empty string if not set
         */
        public String defaultValue() {
            return defaultValue;
        }
    }
}
