package hr.ht.rnd.wifiadmin.infra.transport.rest;

/**
 * Indicates that a REST request contains invalid data.
 */
public final class InvalidRequestException extends RuntimeException {

    /**
     * Creates an invalid request exception.
     *
     * @param cause the underlying cause
     */
    public InvalidRequestException(Throwable cause) {
        super(cause.getMessage());
    }
}
