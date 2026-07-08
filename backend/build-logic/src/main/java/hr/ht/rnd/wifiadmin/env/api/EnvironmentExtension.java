package hr.ht.rnd.wifiadmin.env.api;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Gradle extension exposing
 * the project environment configuration.
 */
public class EnvironmentExtension {

    @Nullable
    private Environment environment;

    /**
     * Returns the environment configuration.
     *
     * @throws IllegalStateException if environment
     * configuration has not been initialized
     */
    public Environment getEnvironment() {
        if (environment == null) {
            throw new IllegalStateException("environment has not been initialized");
        }
        return environment;
    }

    /**
     * Sets the environment configuration.
     *
     * @param environment environment configuration
     *
     * @throws NullPointerException if {@code environment} is {@code null}
     */
    public void setEnvironment(Environment environment) {
        Objects.requireNonNull(environment, "environment must not be null");
        this.environment = environment;
    }
}
