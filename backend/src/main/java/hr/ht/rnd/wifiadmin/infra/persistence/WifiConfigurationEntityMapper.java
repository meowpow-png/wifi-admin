package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Maps Wi-Fi configurations between
 * domain and persistence models.
 */
final class WifiConfigurationEntityMapper {

    private WifiConfigurationEntityMapper() {}

    /**
     * Converts a Wi-Fi configuration
     * into a persistence entity.
     *
     * @param configuration the configuration to convert
     * @param lastSynchronized the synchronization date, or {@code null} if unknown
     *
     * @throws NullPointerException if {@code configuration} is {@code null}
     */
    static WifiConfigurationEntity toEntity(
            WifiConfiguration configuration,
            @Nullable LocalDate lastSynchronized
    ) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        return new WifiConfigurationEntity(
                configuration.cpeId(),
                configuration.wifiBand(),
                configuration.ssid(),
                configuration.encryptionType(),
                configuration.password(),
                lastSynchronized
        );
    }

    /**
     * Reconstructs a Wi-Fi configuration from a persistence entity.
     *
     * @param entity the persistence entity to convert
     *
     * @throws NullPointerException if {@code entity} is {@code null}
     * @throws IllegalArgumentException if {@code entity} contains invalid data
     */
    static WifiConfiguration toDomain(WifiConfigurationEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new WifiConfiguration(
                entity.getCpeId(),
                entity.getWifiBand(),
                entity.getSsid(),
                entity.getEncryptionType(),
                entity.getPassword()
        );
    }
}
