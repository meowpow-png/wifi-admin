package hr.ht.rnd.wifiadmin.domain;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Represents the Wi-Fi configuration of a CPE device.
 */
public record WifiConfiguration(
        String cpeId,
        WifiBand wifiBand,
        String ssid,
        WifiEncryptionType encryptionType,
        @Nullable WifiPassword password
) {

    /**
     * Creates a Wi-Fi configuration.
     *
     * @param cpeId the CPE device identifier
     * @param wifiBand the Wi-Fi frequency band
     * @param ssid the wireless network name
     * @param encryptionType the encryption type, or {@code null} to use {@link WifiEncryptionType#OPEN}
     * @param password the wireless network password, or {@code null} if not specified
     *
     * @throws NullPointerException if {@code cpeId}, {@code wifiBand},
     * or {@code ssid} is {@code null}, or if {@code password} is
     * {@code null} and the encryption type requires a password
     * @throws IllegalArgumentException if {@code cpeId} or {@code ssid} is blank
     */
    public WifiConfiguration(
            String cpeId,
            WifiBand wifiBand,
            String ssid,
            @Nullable WifiEncryptionType encryptionType,
            @Nullable WifiPassword password
    ) {
        Objects.requireNonNull(cpeId, "cpeId must not be null");
        Objects.requireNonNull(wifiBand, "wifiBand must not be null");
        Objects.requireNonNull(ssid, "ssid must not be null");

        if (cpeId.isBlank()) {
            throw new IllegalArgumentException("CPE ID must not be blank");
        }
        if (ssid.isBlank()) {
            throw new IllegalArgumentException("SSID must not be blank");
        }
        encryptionType = Objects.requireNonNullElse(
                encryptionType,
                WifiEncryptionType.OPEN
        );
        if (encryptionType.requiresPassword()) {
            var message = "password must not be null for " + encryptionType;
            Objects.requireNonNull(password, message);
        }
        this.cpeId = cpeId;
        this.wifiBand = wifiBand;
        this.ssid = ssid;
        this.encryptionType = encryptionType;
        this.password = password;
    }
}
