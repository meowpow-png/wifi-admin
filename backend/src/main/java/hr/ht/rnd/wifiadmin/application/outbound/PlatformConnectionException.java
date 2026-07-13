package hr.ht.rnd.wifiadmin.application.outbound;

/**
 * Indicates that the platform could not be reached.
 */
public final class PlatformConnectionException extends PlatformTransportException {

    /**
     * Creates a platform connection exception.
     *
     * @param message the exception message
     * @param cause the underlying cause
     */
    public PlatformConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
