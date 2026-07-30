package hr.ht.rnd.wifiadmin.env.internal;

import hr.ht.rnd.wifiadmin.env.api.Environment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses environment configurations.
 */
final class EnvironmentParser {

    private EnvironmentParser() {}

    /**
     * Parses an environment from the specified lines.
     *
     * @param lines environment file lines
     *
     * @return parsed environment
     * @throws IllegalArgumentException if the input is invalid
     */
    static Environment parse(List<String> lines) {
        var env = new LinkedHashMap<String, String>();

        for (String line : lines) {
            if (line.isBlank() || line.startsWith("#")) {
                continue;
            }
            var entry = parseEntry(line);
            env.put(entry.getKey(), entry.getValue());
        }
        return new Environment(
                get(env, Environment.Variable.AES_KEY),
                get(env, Environment.Variable.JWT_SECRET),
                get(env, Environment.Variable.SOAP_ENDPOINT),
                get(env, Environment.Variable.DB_HOST),
                get(env, Environment.Variable.DB_PORT),
                get(env, Environment.Variable.DB_NAME),
                get(env, Environment.Variable.DB_USER),
                get(env, Environment.Variable.DB_PASSWORD),
                get(env, Environment.Variable.ADMIN_USERNAME),
                get(env, Environment.Variable.ADMIN_PASSWORD)
        );
    }

    private static Map.Entry<String, String> parseEntry(String line) {
        var parts = line.split("=", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid .env entry: " + line);
        }
        return Map.entry(parts[0], parts[1]);
    }

    private static String get(Map<String, String> env, Environment.Variable variable) {
        return env.getOrDefault(variable.name(), variable.defaultValue());
    }
}
