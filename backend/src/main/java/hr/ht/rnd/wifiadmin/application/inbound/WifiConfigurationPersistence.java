package hr.ht.rnd.wifiadmin.application.inbound;

import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;

/**
 * Persist Wi-Fi configurations.
 */
public interface WifiConfigurationPersistence {

    /**
     * Persists the specified Wi-Fi configuration.
     *
     * @param configuration the Wi-Fi configuration to persist
     * @param lastSynchronized the synchronization date, or {@code null} if unknown
     *
     * @throws NullPointerException if {@code configuration} is {@code null}
     * @throws PersistenceException if the configuration cannot be persisted
     */
    void persist(WifiConfiguration configuration, @Nullable LocalDate lastSynchronized);
}
