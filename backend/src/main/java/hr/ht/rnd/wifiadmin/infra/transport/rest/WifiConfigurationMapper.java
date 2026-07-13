package hr.ht.rnd.wifiadmin.infra.transport.rest;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.WifiConfigurationRequest;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.WifiConfigurationResponse;

import java.util.Objects;

/**
 * Maps Wi-Fi configurations between
 * REST API and the domain model.
 */
final class WifiConfigurationMapper {

    private WifiConfigurationMapper() {}

    /**
     * Maps a REST API request to the domain model.
     *
     * @param source the REST API request
     *
     * @return the mapped domain model
     * @throws NullPointerException if {@code source} is {@code null}
     */
    static WifiConfiguration toDomain(WifiConfigurationRequest source) {
        Objects.requireNonNull(source, "source must not be null");
        return new WifiConfiguration(
                source.cpeId(),
                source.wifiBand(),
                source.ssid(),
                source.encryptionType(),
                source.password()
        );
    }

    /**
     * Maps the domain model to a REST API response.
     *
     * @param source the domain model
     *
     * @return the mapped REST API response
     * @throws NullPointerException if {@code source} is {@code null}
     */
    static WifiConfigurationResponse toResponse(WifiConfiguration source) {
        Objects.requireNonNull(source, "source must not be null");
        return new WifiConfigurationResponse(
                source.cpeId(),
                source.wifiBand(),
                source.ssid(),
                source.encryptionType(),
                source.password()
        );
    }
}
