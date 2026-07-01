package hr.ht.rnd.wifiadmin.domain;

import java.util.Objects;

public record WifiConfiguration(
        String cpeId,
        WifiBand wifiBand,
        String ssid,
        WifiEncryptionType encryptionType,
        String password
) {

    public WifiConfiguration {
        Objects.requireNonNull(cpeId, "cpeId must not be null");
        Objects.requireNonNull(wifiBand, "wifiBand must not be null");
        Objects.requireNonNull(ssid, "ssid must not be null");

        if (cpeId.isBlank()) {
            throw new IllegalArgumentException("cpeId must not be blank");
        }
        if (ssid.isBlank()) {
            throw new IllegalArgumentException("ssid must not be blank");
        }
        if (encryptionType == WifiEncryptionType.OPEN) {
            if (password != null) {
                throw new IllegalArgumentException("password must be null for OPEN encryption");
            }
        }
        else {
            Objects.requireNonNull(password, "password must not be null");
            if (password.isBlank()) {
                throw new IllegalArgumentException("password must not be blank");
            }
        }
    }
}
