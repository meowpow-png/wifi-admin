package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.domain.WifiBand;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;
import hr.ht.rnd.wifiadmin.domain.WifiEncryptionType;
import hr.ht.rnd.wifiadmin.infra.soap.wsdl.EncryptionType;
import hr.ht.rnd.wifiadmin.infra.soap.wsdl.WifiBandType;
import hr.ht.rnd.wifiadmin.infra.soap.wsdl.WifiConfigurationType;

/**
 * Maps Wi-Fi configurations between
 * the domain model and SOAP platform model.
 */
final class SoapPlatformMapper {

    private SoapPlatformMapper() {}

    /**
     * Maps a SOAP platform model to the domain model.
     *
     * @param source the SOAP platform model
     *
     * @return the mapped domain model
     * @throws NullPointerException if {@code source} is {@code null},
     * or the SOAP model is missing required properties
     * @throws IllegalArgumentException if SOAP model contains unsupported enum values
     */
    static WifiConfiguration toDomain(WifiConfigurationType source) {
        return new WifiConfiguration(
                source.getCpeId(),
                toDomain(source.getWifiBand()),
                source.getSsid(),
                toDomain(source.getEncryptionType()),
                source.getPassword()
        );
    }

    /**
     * Maps the domain model to a SOAP platform model.
     *
     * @param source the domain model
     *
     * @return the mapped SOAP platform model
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws IllegalArgumentException if the domain model contains unsupported enum values
     */
    static WifiConfigurationType toPlatform(WifiConfiguration source) {
        var target = new WifiConfigurationType();

        target.setCpeId(source.cpeId());
        target.setWifiBand(toPlatform(source.wifiBand()));
        target.setSsid(source.ssid());
        target.setEncryptionType(toPlatform(source.encryptionType()));
        target.setPassword(source.password());

        return target;
    }

    private static WifiBand toDomain(WifiBandType source) {
        return WifiBand.valueOf(source.name());
    }

    private static WifiBandType toPlatform(WifiBand source) {
        return WifiBandType.valueOf(source.name());
    }

    private static WifiEncryptionType toDomain(EncryptionType source) {
        return switch (source) {
            case OPEN -> WifiEncryptionType.OPEN;
            case WEP -> WifiEncryptionType.WEP;
            case WPA_PSK -> WifiEncryptionType.WPA_PSK;
            case WPA_2_PSK -> WifiEncryptionType.WPA2_PSK;
            case WPA_3_SAE -> WifiEncryptionType.WPA3_SAE;
            case WPA_2_ENTERPRISE -> WifiEncryptionType.WPA2_ENTERPRISE;
        };
    }

    private static EncryptionType toPlatform(WifiEncryptionType source) {
        return switch (source) {
            case OPEN -> EncryptionType.OPEN;
            case WEP -> EncryptionType.WEP;
            case WPA_PSK -> EncryptionType.WPA_PSK;
            case WPA2_PSK -> EncryptionType.WPA_2_PSK;
            case WPA3_SAE -> EncryptionType.WPA_3_SAE;
            case WPA2_ENTERPRISE -> EncryptionType.WPA_2_ENTERPRISE;
        };
    }
}
