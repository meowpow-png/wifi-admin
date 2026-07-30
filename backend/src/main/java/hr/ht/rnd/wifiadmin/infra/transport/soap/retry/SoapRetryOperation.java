package hr.ht.rnd.wifiadmin.infra.transport.soap.retry;

import org.springframework.core.retry.Retryable;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Retryable SOAP operation identified by a request context.
 *
 * @param <T> the operation result type
 */
public final class SoapRetryOperation<T> implements Retryable<T> {

    private final Supplier<T> operation;
    private final SoapRequestContext context;

    /**
     * Creates a new retryable SOAP operation.
     *
     * @param operation the operation to execute
     * @param context the request context
     *
     * @throws NullPointerException if any argument is {@code null}
     */
    public SoapRetryOperation(Supplier<T> operation, SoapRequestContext context) {
        Objects.requireNonNull(operation, "operation must not be null");
        Objects.requireNonNull(context, "context must not be null");

        this.operation = operation;
        this.context = context;
    }

    @Override
    public T execute() {
        return operation.get();
    }

    @Override
    public String getName() {
        return context.toString();
    }
}
