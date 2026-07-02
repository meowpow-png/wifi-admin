package hr.ht.rnd.wifiadmin.infra.rest;

/**
 * Indicates that a REST request contains invalid data.
 */
public final class InvalidRequestException extends RuntimeException {

    /**
     * Creates an invalid request exception.
     *
     * @param message the validation failure message
     * @param cause the underlying cause
     */
    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
