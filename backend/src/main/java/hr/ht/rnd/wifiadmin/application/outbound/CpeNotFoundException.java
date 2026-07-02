package hr.ht.rnd.wifiadmin.application.outbound;

/**
 * Indicates that a CPE device does not exist on the platform.
 */
public final class CpeNotFoundException extends PlatformException {

    /**
     * Creates a CPE not found exception.
     *
     * @param cpeId the CPE device identifier
     * @param cause the underlying cause
     */
    public CpeNotFoundException(String cpeId, Throwable cause) {
        super("CPE not found: " + cpeId, cause);
    }
}
