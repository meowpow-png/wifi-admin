package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiEncryptionType;
import hr.ht.rnd.wifiadmin.infra.transport.soap.wsdl.EncryptionType;
import hr.ht.rnd.wifiadmin.infra.transport.soap.wsdl.WifiBandType;
import hr.ht.rnd.wifiadmin.infra.transport.soap.wsdl.WifiConfigurationType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoapPlatformMapperTest {

    @Nested
    @DisplayName("toDomain")
    class ToDomainMethodTests {

        @Test
        @DisplayName("Maps platform fields to configuration")
        void should_MapPlatformFieldsToConfiguration_when_PlatformConfigurationIsValid() {
            var source = platformConfiguration();
            var configuration = SoapPlatformMapper.toDomain(source);

            assertThat(configuration.cpeId()).isEqualTo(source.getCpeId());
            assertThat(configuration.wifiBand()).isEqualTo(TestWifiConfigurations.WIFI_BAND);
            assertThat(configuration.ssid()).isEqualTo(source.getSsid());
            assertThat(configuration.encryptionType()).isEqualTo(WifiEncryptionType.WPA2_PSK);
            assertThat(configuration.password()).isEqualTo(TestWifiConfigurations.PASSWORD);
        }

        @Test
        @DisplayName("Maps missing platform password to configuration without password")
        void should_MapMissingPlatformPasswordToConfigurationWithoutPassword_when_PlatformConfigurationHasNoPassword() {
            var source = platformConfiguration();

            source.setEncryptionType(EncryptionType.OPEN);
            source.setPassword(null);

            var configuration = SoapPlatformMapper.toDomain(source);

            assertThat(configuration.password()).isNull();
            assertThat(configuration.encryptionType()).isEqualTo(WifiEncryptionType.OPEN);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when platform configuration is null")
        void should_ThrowNullPointerException_when_PlatformConfigurationIsNull() {
            assertThatThrownBy(() -> SoapPlatformMapper.toDomain(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("toPlatform")
    class ToPlatformMethodTests {

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Maps configuration fields to platform")
        void should_MapConfigurationFieldsToPlatform_when_ConfigurationIsValid() {
            var source = TestWifiConfigurations.builder().build();
            var configuration = SoapPlatformMapper.toPlatform(source);

            assertThat(configuration.getCpeId()).isEqualTo(source.cpeId());
            assertThat(configuration.getWifiBand()).isEqualTo(WifiBandType.BAND_2_4_GHZ);
            assertThat(configuration.getSsid()).isEqualTo(source.ssid());
            assertThat(configuration.getEncryptionType()).isEqualTo(EncryptionType.WPA_2_PSK);
            assertThat(configuration.getPassword()).isEqualTo(source.password().value());
        }

        @Test
        @DisplayName("Maps missing configuration password to null platform password")
        void should_MapMissingConfigurationPasswordToNullPlatformPassword_when_ConfigurationHasNoPassword() {
            var source = TestWifiConfigurations.builder()
                    .withEncryptionType(WifiEncryptionType.OPEN)
                    .withPassword(null)
                    .build();

            var configuration = SoapPlatformMapper.toPlatform(source);

            assertThat(configuration.getPassword()).isNull();
            assertThat(configuration.getEncryptionType()).isEqualTo(EncryptionType.OPEN);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when configuration is null")
        void should_ThrowNullPointerException_when_ConfigurationIsNull() {
            assertThatThrownBy(() -> SoapPlatformMapper.toPlatform(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    private static WifiConfigurationType platformConfiguration() {
        var configuration = new WifiConfigurationType();

        configuration.setCpeId(TestWifiConfigurations.CPE_ID);
        configuration.setWifiBand(WifiBandType.BAND_2_4_GHZ);
        configuration.setSsid(TestWifiConfigurations.SSID);
        configuration.setEncryptionType(EncryptionType.WPA_2_PSK);
        configuration.setPassword(TestWifiConfigurations.PASSWORD.value());

        return configuration;
    }
}
