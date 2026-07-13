package hr.ht.rnd.wifiadmin.application.exception;

/**
 * Thrown when a persistence operation fails.
 */
public class PersistenceException extends RuntimeException {

    /**
     * Creates a persistence exception.
     *
     * @param cause the underlying cause
     */
    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
