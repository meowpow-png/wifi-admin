package hr.ht.rnd.wifiadmin.application.outbound;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

/**
 * Persist Wi-Fi configurations.
 */
public interface WifiConfigurationPersistence {

    /**
     * Persists the specified Wi-Fi configuration.
     *
     * @param configuration the Wi-Fi configuration to persist
     * @throws NullPointerException if {@code configuration} is {@code null}
     * @throws PersistenceException if the configuration cannot be persisted
     */
    void persist(WifiConfiguration configuration);
}
