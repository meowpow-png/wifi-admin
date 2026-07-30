package hr.ht.rnd.wifiadmin.infra.transport.rest.dto;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiBand;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiEncryptionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.jspecify.annotations.Nullable;

/**
 * REST request for updating a Wi-Fi configuration.
 *
 * @param cpeId the CPE device identifier
 * @param wifiBand the Wi-Fi frequency band
 * @param ssid the Wi-Fi network name
 * @param encryptionType the Wi-Fi encryption type
 * @param password the Wi-Fi password
 */
public record WifiConfigurationRequest(
        @NotBlank(message = "CPE ID must not be blank")
        String cpeId,

        @NotNull(message = "wifi band must not be null")
        WifiBand wifiBand,

        @NotBlank(message = "SSID must not be blank")
        String ssid,

        @Nullable
        WifiEncryptionType encryptionType,

        @Nullable
        String password
) {}
