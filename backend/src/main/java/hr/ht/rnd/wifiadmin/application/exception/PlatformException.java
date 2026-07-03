package hr.ht.rnd.wifiadmin.application.exception;

/**
 * Base exception for platform failures.
 */
public abstract class PlatformException extends RuntimeException {

    /**
     * Creates a platform exception.
     *
     * @param message the exception message
     * @param cause the underlying cause
     */
    protected PlatformException(String message, Throwable cause) {
        super(message, cause);
    }
}
