package hr.ht.rnd.wifiadmin.infra.rest;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

/**
 * Maps Wi-Fi configurations between
 * REST API and the domain model.
 */
final class WifiConfigurationMapper {

    private WifiConfigurationMapper() {}

    /**
     * Maps a REST API model to the domain model.
     *
     * @param source the REST API model
     *
     * @return the mapped domain model
     * @throws NullPointerException if {@code source} is {@code null}
     */
    static WifiConfiguration toDomain(WifiConfigurationDto source) {
        return new WifiConfiguration(
                source.cpeId(),
                source.wifiBand(),
                source.ssid(),
                source.encryptionType(),
                source.password()
        );
    }

    /**
     * Maps the domain model to a REST API model.
     *
     * @param source the domain model
     *
     * @return the mapped REST API model
     * @throws NullPointerException if {@code source} is {@code null}
     */
    static WifiConfigurationDto toDto(WifiConfiguration source) {
        return new WifiConfigurationDto(
                source.cpeId(),
                source.wifiBand(),
                source.ssid(),
                source.encryptionType(),
                source.password()
        );
    }
}
