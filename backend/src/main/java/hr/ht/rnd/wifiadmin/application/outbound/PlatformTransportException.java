package hr.ht.rnd.wifiadmin.application.outbound;

/**
 * Indicates that communication with the platform
 * failed before a valid response could be processed.
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
