package hr.ht.rnd.wifiadmin.domain.wifi;

/**
 * Supported Wi-Fi encryption types.
 */
public enum WifiEncryptionType {
    OPEN(false),
    WEP(true),
    WPA_PSK(true),
    WPA2_PSK(true),
    WPA3_SAE(true),
    WPA2_ENTERPRISE(false);

    private final boolean requiresPassword;

    WifiEncryptionType(boolean requiresPassword) {
        this.requiresPassword = requiresPassword;
    }

    /**
     * Returns whether this encryption
     * type requires a password.
     */
    boolean requiresPassword() {
        return requiresPassword;
    }
}
