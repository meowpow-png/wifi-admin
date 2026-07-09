package hr.ht.rnd.wifiadmin.application.exception;

/**
 * Thrown when authentication cannot be completed.
 */
public final class AuthenticationException extends RuntimeException {

    /**
     * Creates a new authentication exception.
     *
     * @param message the exception message
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Creates a new authentication exception.
     *
     * @param cause the cause of the exception
     */
    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
