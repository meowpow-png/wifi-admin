package hr.ht.rnd.wifiadmin.infra.transport.client;

import org.springframework.core.retry.RetryException;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.core.retry.Retryable;

import java.util.Objects;

/**
 * Base class for resilient clients.
 * <p>
 * Applies the application's retry policy while
 * delegating recovery and exception handling to subclasses.
 *
 * @param <C> the request context type
 */
public abstract class ResilientClient<C> {

    private final RetryTemplate retryTemplate;

    /**
     * Creates a new resilient client.
     *
     * @param retryTemplate the retry template
     *
     * @throws NullPointerException if {@code retryTemplate} is {@code null}
     */
    public ResilientClient(RetryTemplate retryTemplate) {
        Objects.requireNonNull(retryTemplate, "retryTemplate must not be null");
        this.retryTemplate = retryTemplate;
    }

    /**
     * Executes an operation under the configured retry policy.
     *
     * @param action the operation to execute
     * @param context the request context
     *
     * @return the operation result
     */
    protected final <T> T executeWithRetry(Retryable<T> action, C context) {
        try {
            return retryTemplate.execute(action);
        }
        catch (RetryException e) {
            return attemptRecovery(e.getLastException(), context);
        }
    }

    private <T> T attemptRecovery(Throwable cause, C context) {
        T result = recoverOrThrow(cause, context);
        onRecovery(cause, context);
        return result;
    }

    /**
     * Recovers from an exhausted retry attempt
     * or throws a client-specific exception.
     *
     * @param cause the last failure
     * @param context the request context
     *
     * @return the recovered result
     */
    protected abstract <T> T recoverOrThrow(Throwable cause, C context);

    /**
     * Invoked after a successful recovery.
     * <p>
     * The default implementation does nothing.
     *
     * @param cause the last failure
     * @param context the request context
     */
    protected void onRecovery(Throwable cause, C context) {}
}
