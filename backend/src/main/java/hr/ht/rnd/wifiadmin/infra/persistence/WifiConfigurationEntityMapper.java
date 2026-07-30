package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.application.outbound.PasswordEncryptor;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import org.springframework.stereotype.Component;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Maps Wi-Fi configurations between
 * domain and persistence models.
 */
@Component
final class WifiConfigurationEntityMapper {

    private final PasswordEncryptor encryptor;

    WifiConfigurationEntityMapper(PasswordEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    /**
     * Converts a Wi-Fi configuration
     * into a persistence entity.
     *
     * @param configuration the configuration to convert
     * @param lastSynchronized the synchronization date, or {@code null} if unknown
     *
     * @throws NullPointerException if {@code configuration} is {@code null}
     * @throws IllegalStateException if the password cannot be encrypted
     */
    WifiConfigurationEntity toEntity(
            WifiConfiguration configuration,
            @Nullable LocalDate lastSynchronized
    ) {
        Objects.requireNonNull(configuration, "configuration must not be null");

        var password = configuration.password();
        var encryptedPassword = password == null ? null : encryptor.encrypt(password);

        return new WifiConfigurationEntity(
                configuration.cpeId(),
                configuration.wifiBand(),
                configuration.ssid(),
                configuration.encryptionType(),
                encryptedPassword,
                lastSynchronized
        );
    }

    /**
     * Reconstructs a Wi-Fi configuration from a persistence entity.
     *
     * @param entity the persistence entity to convert
     *
     * @throws NullPointerException if {@code entity} is {@code null}
     * @throws IllegalArgumentException if {@code entity}
     * contains invalid data or the password cannot be decrypted
     */
    WifiConfiguration toDomain(WifiConfigurationEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");

        var password = entity.getPassword();
        var decryptedPassword = password == null ? null : encryptor.decrypt(password);

        return new WifiConfiguration(
                entity.getCpeId(),
                entity.getWifiBand(),
                entity.getSsid(),
                entity.getEncryptionType(),
                decryptedPassword
        );
    }
}
