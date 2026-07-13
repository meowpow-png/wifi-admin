package hr.ht.rnd.wifiadmin.application.exception;

/**
 * Indicates that communication with the
 * platform timed out before a response could be received.
 */
public final class PlatformTimeoutException extends PlatformTransportException {

    /**
     * Creates a platform timeout exception.
     *
     * @param message the exception message
     * @param cause the underlying cause
     */
    public PlatformTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
