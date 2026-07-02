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
        @Nullable String password
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
     * @throws NullPointerException if {@code cpeId}, {@code wifiBand}, or {@code ssid} is {@code null}
     * @throws IllegalArgumentException if {@code cpeId} or {@code ssid} is blank
     */
    public WifiConfiguration(
            String cpeId,
            WifiBand wifiBand,
            String ssid,
            @Nullable WifiEncryptionType encryptionType,
            @Nullable String password
    ) {
        Objects.requireNonNull(cpeId, "cpeId must not be null");
        Objects.requireNonNull(wifiBand, "wifiBand must not be null");
        Objects.requireNonNull(ssid, "ssid must not be null");

        if (cpeId.isBlank()) {
            throw new IllegalArgumentException("cpeId must not be blank");
        }
        if (ssid.isBlank()) {
            throw new IllegalArgumentException("ssid must not be blank");
        }
        this.cpeId = cpeId;
        this.wifiBand = wifiBand;
        this.ssid = ssid;
        this.encryptionType = Objects.requireNonNullElse(
                encryptionType,
                WifiEncryptionType.OPEN
        );
        this.password = password;
    }
}
