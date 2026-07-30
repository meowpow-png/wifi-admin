package hr.ht.rnd.wifiadmin.env.internal;

import hr.ht.rnd.wifiadmin.env.api.Environment;

import java.util.stream.Collectors;

/**
 * Serializes environment configurations.
 */
final class EnvironmentSerializer {

    private EnvironmentSerializer() {}

    /**
     * Serializes the specified environment.
     *
     * @param env environment to serialize
     *
     * @return serialized environment
     */
    static String serialize(Environment env) {
        return env.toMap().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("\n", "", "\n"));
    }
}
