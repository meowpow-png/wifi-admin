package hr.ht.rnd.wifiadmin.common;

import org.slf4j.MDC;

import java.util.Objects;
import java.util.UUID;

/**
 * Manages the logging context for the current execution.
 * <p>
 * The logging context provides a trace identifier
 * automatically included in structured log
 * entries to enable log correlation.
 * <p>
 * The context is backed by the underlying logging
 * framework and is isolated per execution. Callers
 * should open a context at the beginning of an execution
 * and ensure it is closed when processing completes.
 */
public final class LogContext {

    private LogContext() {}

    /**
     * Opens a new logging context
     * with a generated trace identifier.
     *
     * @return a new logging context scope
     */
    public static Scope open() {
        return open(UUID.randomUUID().toString());
    }

    /**
     * Opens a new logging context
     * with the specified trace identifier.
     * <p>
     * <strong>API Note:</strong>
     * This is typically used when continuing an
     * existing trace propagated from an external caller.
     *
     * @param traceId trace identifier to associate with the current execution
     *
     * @return a new logging context scope
     */
    public static Scope open(String traceId) {
        Objects.requireNonNull(traceId, "traceId must not be null");

        MDC.put(StructuredLog.Field.TRACE_ID.key(), traceId);

        return () -> MDC.remove(StructuredLog.Field.TRACE_ID.key());
    }

    /**
     * Represents the lifetime of an active logging context.
     */
    public interface Scope extends AutoCloseable {

        @Override
        void close();
    }
}
