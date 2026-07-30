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

        TRACE_ID("trace_id"),

        HTTP_METHOD("http_method"),
        HTTP_PATH("http_path"),
        CLIENT_IP("client_ip"),
        USER_AGENT("user_agent"),

        CPE_ID("cpe_id"),
        VALIDATION_MESSAGE("validation_message"),
        PLATFORM_REQUEST_CONTEXT("request_context"),
        RETRY_COUNT("retry_count"),

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

        APPLICATION_LOG_LEVEL_CHANGED("Changed application log level"),
        SOAP_PAYLOAD_LOG_LEVEL_CHANGED("Changed SOAP payload log level"),

        SCHEDULED_SYNCHRONIZATION_TRIGGERED("Scheduled synchronization triggered"),
        PLATFORM_SYNCHRONIZATION_STARTED("Starting platform synchronization"),
        PLATFORM_SYNCHRONIZATION_TRACKING_STARTED("Starting platform synchronization tracking"),
        CPE_SYNCHRONIZATION_STARTED("Synchronizing CPE"),
        NEXT_PLATFORM_SYNCHRONIZATION_SCHEDULED("Next platform synchronization scheduled"),

        PLATFORM_SYNCHRONIZATION_PROGRESS_UPDATED("Platform synchronization progress updated"),
        PLATFORM_SYNCHRONIZATION_DISPATCH_COMPLETED("Platform synchronization dispatching completed"),
        PLATFORM_SYNCHRONIZATION_COMPLETED("Platform synchronization completed"),
        PLATFORM_SYNCHRONIZATION_ABORTED("Platform synchronization aborted"),

        PERSIST_RETRIEVED_CONFIGURATION_STARTED("Persisting retrieved configuration"),
        PERSIST_RETRIEVED_CONFIGURATION_COMPLETED("Completed persisting retrieved configuration"),
        PERSIST_UPDATED_CONFIGURATION_STARTED("Persisting updated configuration"),
        PERSIST_UPDATED_CONFIGURATION_COMPLETED("Completed persisting updated configuration"),

        RETRIEVE_WIFI_CONFIGURATION_STARTED("Retrieving Wi-Fi configuration"),
        RETRIEVE_WIFI_CONFIGURATION_FAILED("Failed to retrieve Wi-Fi configuration"),
        UPDATE_WIFI_CONFIGURATION_STARTED("Updating Wi-Fi configuration"),

        OUTBOUND_SOAP_REQUEST("Outbound SOAP request"),
        INBOUND_SOAP_RESPONSE("Inbound SOAP response"),
        SOAP_PAYLOAD_FORMATTING_FAILED("Failed to format SOAP payload"),

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

        PLATFORM_RETRY_ATTEMPT("Retrying platform communication"),
        PLATFORM_RETRY_EXHAUSTED("Platform communication retry exhausted"),

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
