package hr.ht.rnd.wifiadmin.application.exception;

/**
 * Thrown when an expected account cannot be found.
 */
public final class AccountNotFoundException extends RuntimeException {

    /**
     * Creates a new account not found exception.
     *
     * @param message the exception message
     *
     */
    public AccountNotFoundException(String message) {
        super(message);
    }
}
