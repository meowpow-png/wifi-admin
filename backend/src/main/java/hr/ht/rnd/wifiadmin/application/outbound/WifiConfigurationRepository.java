package hr.ht.rnd.wifiadmin.application.outbound;

import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import java.util.Optional;

/**
 * Repository for locally persisted Wi-Fi configurations.
 */
public interface WifiConfigurationRepository {

    /**
     * Retrieves the persisted Wi-Fi configuration
     * for the specified CPE device.
     *
     * @param cpeId the CPE device identifier
     *
     * @return the Wi-Fi configuration, or an empty optional if no configuration exists
     * @throws NullPointerException if {@code cpeId} is {@code null}
     * @throws PersistenceException if the configuration cannot be retrieved
     */
    Optional<WifiConfiguration> findByCpeId(String cpeId);

    /**
     * Persists the specified Wi-Fi configuration,
     * creating or updating it as needed.
     *
     * @param configuration the Wi-Fi configuration to persist
     *
     * @throws NullPointerException if {@code configuration} is {@code null}
     * @throws PersistenceException if the configuration cannot be persisted
     */
    void save(WifiConfiguration configuration);
}
