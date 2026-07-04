package hr.ht.rnd.wifiadmin.application.outbound;

import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
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
     * @param lastSynchronized the synchronization date, or {@code null} if unknown
     *
     * @throws NullPointerException if {@code configuration} is {@code null}
     * @throws PersistenceException if the configuration cannot be persisted
     */
    void save(WifiConfiguration configuration, @Nullable LocalDate lastSynchronized);

    /**
     * Removes all Wi-Fi configurations last
     * synchronized before the specified date.
     *
     * @param lastSynchronized the synchronization date
     *
     * @throws NullPointerException if {@code lastSynchronized} is {@code null}
     * @throws PersistenceException if the configurations cannot be deleted
     */
    void deleteOlderThan(LocalDate lastSynchronized);
}
