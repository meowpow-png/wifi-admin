package hr.ht.rnd.wifiadmin.application.inbound;

import hr.ht.rnd.wifiadmin.application.outbound.PersistenceException;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

/**
 * Persist Wi-Fi configurations.
 */
public interface WifiPersistence {

    /**
     * Persists the specified Wi-Fi configuration.
     *
     * @param configuration the Wi-Fi configuration to persist
     * @throws NullPointerException if {@code configuration} is {@code null}
     * @throws PersistenceException if the configuration cannot be persisted
     */
    void persist(WifiConfiguration configuration);
}
