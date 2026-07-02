package hr.ht.rnd.wifiadmin.application.outbound;

/**
 * Indicates that the platform responded
 * successfully, but the response could not be processed.
 */
public class PlatformResponseException extends PlatformException {

    /**
     * Creates a platform response exception.
     *
     * @param cause the underlying cause
     */
    public PlatformResponseException(Throwable cause) {
        super("Platform returned an invalid response", cause);
    }
}
