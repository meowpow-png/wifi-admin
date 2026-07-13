package hr.ht.rnd.wifiadmin.infra.rest;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

final class WifiConfigurationMapper {

    private WifiConfigurationMapper() {}

    static WifiConfiguration toDomain(WifiConfigurationDto source) {
        return new WifiConfiguration(
                source.cpeId(),
                source.wifiBand(),
                source.ssid(),
                source.encryptionType(),
                source.password()
        );
    }

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
