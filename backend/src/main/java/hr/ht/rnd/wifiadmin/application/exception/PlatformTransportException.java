package hr.ht.rnd.wifiadmin.application.exception;

/**
 * Base exception for failures that occur while
 * communicating with the platform at the transport level.
 */
public abstract class PlatformTransportException extends PlatformException {

    /**
     * Creates a platform transport exception.
     *
     * @param message the exception message
     * @param cause the underlying cause
     */
    protected PlatformTransportException(String message, Throwable cause) {
        super(message, cause);
    }
}
