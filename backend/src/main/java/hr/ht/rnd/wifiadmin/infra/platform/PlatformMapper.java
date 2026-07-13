package hr.ht.rnd.wifiadmin.infra.platform;

import hr.ht.rnd.wifiadmin.domain.WifiBand;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;
import hr.ht.rnd.wifiadmin.domain.WifiEncryptionType;
import hr.ht.rnd.wifiadmin.infra.platform.wsdl.EncryptionType;
import hr.ht.rnd.wifiadmin.infra.platform.wsdl.WifiBandType;
import hr.ht.rnd.wifiadmin.infra.platform.wsdl.WifiConfigurationType;

final class PlatformMapper {

    private PlatformMapper() {}

    static WifiConfiguration toDomain(WifiConfigurationType source) {
        return new WifiConfiguration(
                source.getCpeId(),
                toDomain(source.getWifiBand()),
                source.getSsid(),
                toDomain(source.getEncryptionType()),
                source.getPassword()
        );
    }

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
