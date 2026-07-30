package hr.ht.rnd.wifiadmin.common;

import org.springframework.http.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.spi.LoggingEventBuilder;

import java.util.Objects;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.Event;
import static hr.ht.rnd.wifiadmin.common.StructuredLog.Field;

/**
 * Fluent builder for creating structured log entries.
 * <p>
 * <strong>API Note:</strong>
 * A log entry is emitted only after {@link #log()}
 * is called. Builders that are not terminated
 * are discarded without producing any log output.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class StructuredLogBuilder {

    private final LoggingEventBuilder delegate;

    StructuredLogBuilder(LoggingEventBuilder delegate) {
        Objects.requireNonNull(delegate, "delegate must not be null");
        this.delegate = delegate;
    }

    /**
     * Adds a canonical application event.
     * <p>
     * <strong>API Note:</strong>
     * This method should normally be called before
     * adding structured fields to ensure the event
     * appears first in the emitted log entry.
     *
     * @param event the event to log
     *
     * @return this builder
     */
    public StructuredLogBuilder withEvent(Event event) {
        delegate.addKeyValue("event", event.name());
        delegate.setMessage(event.message());
        return this;
    }

    /**
     * Adds a custom log message.
     * <p>
     * <strong>API Note:</strong>
     * Prefer {@link #withEvent(Event)} where applicable.
     *
     * @param message the log message
     *
     * @return this builder
     */
    public StructuredLogBuilder withMessage(String message) {
        delegate.setMessage(message);
        return this;
    }

    /**
     * Adds a structured log field.
     * <p>
     * <strong>API Note:</strong>
     * Prefer {@link #withField(Field, Object)} for canonical fields.
     *
     * @param key the field name
     * @param value the field value
     *
     * @return this builder
     */
    public StructuredLogBuilder withField(String key, Object value) {
        delegate.addKeyValue(key, value);
        return this;
    }

    /**
     * Adds a canonical structured log field.
     *
     * @param field the field to add
     * @param value the field value
     *
     * @return this builder
     */
    public StructuredLogBuilder withField(Field field, Object value) {
        withField(field.key(), value);
        return this;
    }

    /**
     * Adds standard HTTP request context.
     *
     * @param request the HTTP request
     *
     * @return this builder
     */
    public StructuredLogBuilder withRequest(HttpServletRequest request) {
        delegate.addKeyValue(Field.HTTP_METHOD.key(), request.getMethod());
        delegate.addKeyValue(Field.HTTP_PATH.key(), request.getRequestURI());
        delegate.addKeyValue(Field.CLIENT_IP.key(), request.getRemoteAddr());
        delegate.addKeyValue(Field.USER_AGENT.key(), request.getHeader(HttpHeaders.USER_AGENT));
        return this;
    }

    /**
     * Adds the exception that caused the log event.
     *
     * @param cause the exception to attach
     *
     * @return this builder
     */
    public StructuredLogBuilder withCause(Throwable cause) {
        delegate.setCause(cause);
        return this;
    }

    /**
     * Emits the configured log entry.
     */
    public void log() {
        delegate.log();
    }
}
