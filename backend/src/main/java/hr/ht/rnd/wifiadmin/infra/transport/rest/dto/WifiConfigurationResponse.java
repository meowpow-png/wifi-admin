package hr.ht.rnd.wifiadmin.infra.transport.rest.dto;

import hr.ht.rnd.wifiadmin.domain.WifiBand;
import hr.ht.rnd.wifiadmin.domain.WifiEncryptionType;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * REST response containing a Wi-Fi configuration.
 *
 * @param cpeId the CPE device identifier
 * @param wifiBand the Wi-Fi frequency band
 * @param ssid the Wi-Fi network name
 * @param encryptionType the Wi-Fi encryption type
 * @param password the Wi-Fi password
 */
public record WifiConfigurationResponse(
        String cpeId,
        WifiBand wifiBand,
        String ssid,

        @Nullable
        WifiEncryptionType encryptionType,

        @Nullable
        String password
) {

    /**
     * REST response containing a Wi-Fi configuration.
     *
     * @param cpeId the CPE device identifier
     * @param wifiBand the Wi-Fi frequency band
     * @param ssid the Wi-Fi network name
     * @param encryptionType the Wi-Fi encryption type
     * @param password the Wi-Fi password
     *
     * @throws NullPointerException if {@code cpeId},
     * {@code wifiBand}, or {@code ssid} is {@code null}
     */
    public WifiConfigurationResponse {
        Objects.requireNonNull(cpeId, "cpeId must not be null");
        Objects.requireNonNull(wifiBand, "wifiBand must not be null");
        Objects.requireNonNull(ssid, "ssid must not be null");
    }
}
