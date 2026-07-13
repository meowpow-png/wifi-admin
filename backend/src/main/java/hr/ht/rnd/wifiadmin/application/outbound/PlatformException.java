package hr.ht.rnd.wifiadmin.application.outbound;

/**
 * Base exception for platform failures.
 */
abstract class PlatformException extends RuntimeException {

    /**
     * Creates a platform exception.
     *
     * @param message the exception message
     * @param cause the underlying cause
     */
    PlatformException(String message, Throwable cause) {
        super(message, cause);
    }
}
