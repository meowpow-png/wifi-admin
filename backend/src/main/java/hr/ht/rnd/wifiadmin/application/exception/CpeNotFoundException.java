package hr.ht.rnd.wifiadmin.application.exception;

/**
 * Indicates that a CPE device does not exist on the platform.
 */
public final class CpeNotFoundException extends PlatformException {

    private final String cpeId;

    /**
     * Creates a CPE not found exception.
     *
     * @param cpeId the CPE device identifier
     * @param cause the underlying cause
     */
    public CpeNotFoundException(String cpeId, Throwable cause) {
        super("CPE not found: " + cpeId, cause);
        this.cpeId = cpeId;
    }

    /**
     * Returns the identifier of
     * the CPE device that was not found.
     */
    public String cpeId() {
        return cpeId;
    }
}
