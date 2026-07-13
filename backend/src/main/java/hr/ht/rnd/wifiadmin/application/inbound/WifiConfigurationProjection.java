package hr.ht.rnd.wifiadmin.application.inbound;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

/**
 * Maintains the application view
 * of known Wi-Fi configurations.
 */
public interface WifiConfigurationProjection {

    /**
     * Adds or updates a Wi-Fi
     * configuration in the application view.
     *
     * @param configuration the Wi-Fi configuration
     *
     * @throws NullPointerException if {@code configuration} is {@code null}
     */
    void put(WifiConfiguration configuration);
}
