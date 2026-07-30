package hr.ht.rnd.wifiadmin.env.internal;

import hr.ht.rnd.wifiadmin.env.api.Environment;

import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class providing methods
 * for reading and writing environment files.
 */
public final class EnvFiles {

    private EnvFiles() {}

    /**
     * Loads an environment from the specified file.
     *
     * @param path path to the environment file
     *
     * @return loaded environment
     * @throws IllegalStateException if the file does not exist
     * @throws RuntimeException if the file cannot be read
     */
    public static Environment loadEnvFile(Path path) {
        if (!Files.exists(path)) {
            var message =  "Environment file does not exist: " + path;
            throw new IllegalStateException(message);
        }
        try {
            return EnvironmentParser.parse(Files.readAllLines(path));
        }
        catch (IOException e) {
            var message = "Failed to load environment file: " + path;
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Creates an environment file if it does not already exist.
     *
     * @param targetFile target environment file
     * @param logger logger for lifecycle messages
     */
    public static void createEnvFile(File targetFile, Logger logger) {
        if (targetFile.exists()) {
            logger.lifecycle("Environment file '{}' already exists, skipping setup",
                    targetFile.getName()
            );
            return;
        }
        writeEnvFile(targetFile);

        logger.lifecycle("Created environment file: {}",
                targetFile.getName()
        );
    }

    private static void writeEnvFile(File targetFile) {
        saveEnv(targetFile, EnvironmentFactory.create());
    }

    private static void saveEnv(File file, Environment env) {
        var content = EnvironmentSerializer.serialize(env);
        try {
            Files.writeString(file.toPath(), content);
        }
        catch (IOException e) {
            var message = "Failed to save environment file: " + file;
            throw new RuntimeException(message, e);
        }
    }
}
