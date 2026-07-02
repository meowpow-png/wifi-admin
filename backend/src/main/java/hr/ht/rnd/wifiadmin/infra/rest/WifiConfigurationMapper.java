package hr.ht.rnd.wifiadmin.infra.rest;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.rest.dto.WifiConfigurationRequest;
import hr.ht.rnd.wifiadmin.infra.rest.dto.WifiConfigurationResponse;

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
     */
    static WifiConfiguration toDomain(WifiConfigurationRequest source) {
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
     */
    static WifiConfigurationResponse toResponse(WifiConfiguration source) {
        return new WifiConfigurationResponse(
                source.cpeId(),
                source.wifiBand(),
                source.ssid(),
                source.encryptionType(),
                source.password()
        );
    }
}
