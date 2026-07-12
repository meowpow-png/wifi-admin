package hr.ht.rnd.wifiadmin.application.inbound;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import java.util.List;
import java.util.Optional;

/**
 * Represents the current application
 * view of known Wi-Fi configurations.
 */
public interface WifiConfigurationView {

    /**
     * Returns all known Wi-Fi configurations.
     *
     * @return the Wi-Fi configurations
     */
    List<WifiConfiguration> findAll();

    /**
     * Returns the Wi-Fi configuration
     * for the specified CPE device.
     *
     * @param cpeId the CPE device identifier
     *
     * @return the Wi-Fi configuration, if found
     * @throws NullPointerException if {@code cpeId} is {@code null}
     */
    Optional<WifiConfiguration> findByCpeId(String cpeId);
}
