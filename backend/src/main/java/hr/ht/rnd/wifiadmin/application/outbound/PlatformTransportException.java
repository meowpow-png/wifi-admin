package hr.ht.rnd.wifiadmin.application.outbound;

/**
 * Indicates that communication with the platform
 * failed before a valid response could be processed.
 */
public abstract class PlatformTransportException extends PlatformException {

    /**
     * Creates a platform transport exception.
     *
     * @param cause the underlying cause
     */
    protected PlatformTransportException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
