package hr.ht.rnd.wifiadmin.infra.transport.soap.retry;

import java.util.Objects;

/**
 * Describes an outbound SOAP request.
 *
 * @param operation the logical SOAP operation
 * @param cpeId the target CPE identifier
 */
public record SoapRequestContext(String operation, String cpeId) {

    /**
     * Creates a new SOAP request context.
     *
     * @param operation the logical SOAP operation
     * @param cpeId the target CPE identifier
     *
     * @throws NullPointerException if any argument is {@code null}
     * @throws IllegalArgumentException if any argument is blank
     */
    public SoapRequestContext {
        Objects.requireNonNull(operation, "operation must not be null");
        Objects.requireNonNull(cpeId, "cpeId must not be null");

        if (operation.isBlank()) {
            throw new IllegalArgumentException("operation must not be blank");
        }
        if (cpeId.isBlank()) {
            throw new IllegalArgumentException("cpeId must not be blank");
        }
    }

    @Override
    public String toString() {
        return "%s [cpeId=%s]".formatted(operation, cpeId);
    }
}
