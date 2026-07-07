package hr.ht.rnd.wifiadmin.common;

import org.slf4j.Logger;

/**
 * Factory for creating structured log builders.
 * <p>
 * Standardizes structured field names while delegating
 * log emission to the underlying SLF4J logger.
 */
public final class StructuredLog {

    /**
     * Creates a TRACE log builder.
     *
     * @param logger target logger
     *
     * @return structured log builder
     */
    public static StructuredLogBuilder trace(Logger logger) {
        return new StructuredLogBuilder(logger.atTrace());
    }

    /**
     * Creates a DEBUG log builder.
     *
     * @param logger target logger
     *
     * @return structured log builder
     */
    public static StructuredLogBuilder debug(Logger logger) {
        return new StructuredLogBuilder(logger.atDebug());
    }

    /**
     * Creates an INFO log builder.
     *
     * @param logger target logger
     *
     * @return structured log builder
     */
    public static StructuredLogBuilder info(Logger logger) {
        return new StructuredLogBuilder(logger.atInfo());
    }

    /**
     * Creates a WARN log builder.
     *
     * @param logger target logger
     *
     * @return structured log builder
     */
    public static StructuredLogBuilder warn(Logger logger) {
        return new StructuredLogBuilder(logger.atWarn());
    }

    /**
     * Creates an ERROR log builder.
     *
     * @param logger target logger
     *
     * @return structured log builder
     */
    public static StructuredLogBuilder error(Logger logger) {
        return new StructuredLogBuilder(logger.atError());
    }

    private StructuredLog() {}

    /**
     * Canonical structured log fields shared across the application.
     */
    public enum Field {

        HTTP_METHOD("http_method"),
        HTTP_PATH("http_path"),
        CLIENT_IP("client_ip"),
        USER_AGENT("user_agent"),

        USERNAME("username"),
        CPE_ID("cpe_id"),

        VALIDATION_MESSAGE("validation_message"),

        DATE("date"),
        TIME_ZONE("time_zone"),

        CONFIGURATION_COUNT("configuration_count"),
        EXPECTED_CONFIGURATION_COUNT("expected_configuration_count");

        private final String key;

        Field(String key) {
            this.key = key;
        }

        String key() {
            return key;
        }
    }

    /**
     * Canonical application events used for structured logging.
     */
    public enum Event {

        APPLICATION_BOOTSTRAP_STARTED("Starting application bootstrap"),
        APPLICATION_BOOTSTRAP_COMPLETED("Application bootstrap completed"),

        SCHEDULED_SYNCHRONIZATION_TRIGGERED("Scheduled synchronization triggered"),
        PLATFORM_SYNCHRONIZATION_STARTED("Starting platform synchronization"),
        CPE_SYNCHRONIZATION_STARTED("Synchronizing CPE"),
        NEXT_PLATFORM_SYNCHRONIZATION_SCHEDULED("Next platform synchronization scheduled"),
        PLATFORM_SYNCHRONIZATION_DISPATCH_COMPLETED("Platform synchronization dispatching completed"),
        PLATFORM_SYNCHRONIZATION_COMPLETED("Platform synchronization completed"),
        PLATFORM_SYNCHRONIZATION_ABORTED("Platform synchronization aborted"),

        RETRIEVED_CONFIGURATION_PERSISTENCE_STARTED("Persisting retrieved configuration"),
        UPDATED_CONFIGURATION_PERSISTENCE_STARTED("Persisting updated configuration"),

        AUTHENTICATION_ATTEMPT("Authentication attempt"),
        AUTHENTICATION_SUCCEEDED("Authentication succeeded"),
        AUTHENTICATION_FAILED("Authentication failed"),

        ADMINISTRATOR_PASSWORD_CHANGE_ATTEMPT("Administrator password change attempt"),
        ADMINISTRATOR_PASSWORD_CHANGED("Administrator password changed"),

        REQUEST_VALIDATION_FAILED("Request validation failed"),
        INVALID_REQUEST("Invalid request"),
        REQUEST_BODY_PARSE_FAILED("Failed to parse request body"),

        PLATFORM_CPE_REPORTED_NOT_FOUND("Platform reported CPE not found"),
        WIFI_CONFIGURATION_NOT_FOUND("Wi-Fi configuration not found"),
        CPE_NOT_FOUND("CPE not found"),
        PLATFORM_RESPONSE_INVALID("SOAP platform returned an invalid response"),
        PLATFORM_COMMUNICATION_FAILED("SOAP platform communication failed"),

        ADMINISTRATOR_ACCOUNT_NOT_FOUND("Administrator account not found"),
        UNHANDLED_ASYNC_EXCEPTION("Unhandled exception in async method"),
        UNHANDLED_EXCEPTION("Unhandled exception");

        private final String message;

        Event(String message) {
            this.message = message;
        }

        String message() {
            return message;
        }
    }
}
