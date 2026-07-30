package hr.ht.rnd.wifiadmin.application.inbound;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import java.util.List;

/**
 * Exposes platform administration operations.
 */
public interface PlatformAdministration {

    /**
     * Retrieves the Wi-Fi configuration of a CPE device.
     *
     * @param cpeId the CPE device identifier
     *
     * @return the Wi-Fi configuration
     * @throws NullPointerException if {@code cpeId} is {@code null}
     */
    WifiConfiguration retrieveConfiguration(String cpeId);

    /**
     * Updates the Wi-Fi configuration of a CPE device.
     *
     * @param configuration the Wi-Fi configuration to update
     *
     * @return the updated Wi-Fi configuration
     * @throws NullPointerException if {@code configuration} is {@code null}
     */
    WifiConfiguration updateConfiguration(WifiConfiguration configuration);

    /**
     * Retrieves all known Wi-Fi configurations.
     *
     * @return the Wi-Fi configurations
     */
    List<WifiConfiguration> retrieveConfigurations();
}
