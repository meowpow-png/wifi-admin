package hr.ht.rnd.wifiadmin.env.internal;

import hr.ht.rnd.wifiadmin.env.api.Environment;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Factory for creating environment configurations.
 */
public final class EnvironmentFactory {

    private EnvironmentFactory() {}

    /**
     * Creates a default development environment.
     *
     * @return default environment
     */
    public static Environment create() {
        return new Environment(
                randomBase64(32),
                randomBase64(32),
                Environment.Variable.SOAP_ENDPOINT.defaultValue(),
                Environment.Variable.DB_HOST.defaultValue(),
                Environment.Variable.DB_PORT.defaultValue(),
                Environment.Variable.DB_NAME.defaultValue(),
                Environment.Variable.DB_USER.defaultValue(),
                Environment.Variable.DB_PASSWORD.defaultValue(),
                Environment.Variable.ADMIN_USERNAME.defaultValue(),
                Environment.Variable.ADMIN_PASSWORD.defaultValue()
        );
    }

    private static String randomBase64(int bytes) {
        var data = new byte[bytes];
        new SecureRandom().nextBytes(data);
        return Base64.getEncoder().encodeToString(data);
    }
}
