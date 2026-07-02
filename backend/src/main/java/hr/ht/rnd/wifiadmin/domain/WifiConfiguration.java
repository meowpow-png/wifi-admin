package hr.ht.rnd.wifiadmin.domain;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public record WifiConfiguration(
        String cpeId,
        WifiBand wifiBand,
        String ssid,
        WifiEncryptionType encryptionType,
        @Nullable String password
) {

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
