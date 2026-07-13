package hr.ht.rnd.wifiadmin.application.exception;

/**
 * Indicates that communication with the
 * platform failed after the request was sent
 * but before a complete response could be received or processed.
 */
public final class PlatformCommunicationException extends PlatformTransportException {

    /**
     * Creates a platform communication exception.
     *
     * @param message the exception message
     * @param cause the underlying cause
     */
    public PlatformCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
