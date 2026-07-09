package hr.ht.rnd.wifiadmin.application.exception;

/**
 * Indicates that the platform responded
 * successfully, but the response could not be processed.
 */
public final class PlatformResponseException extends PlatformException {

    /**
     * Creates a platform response exception.
     *
     * @param cause the underlying cause
     */
    public PlatformResponseException(Throwable cause) {
        super("Platform returned an invalid response", cause);
    }
}
