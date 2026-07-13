package hr.ht.rnd.wifiadmin.infra.transport.rest;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiEncryptionType;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.WifiConfigurationRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WifiConfigurationMapperTest {

    @Nested
    @DisplayName("toDomain")
    class ToDomainMethodTests {

        @Test
        @DisplayName("Maps request fields to configuration")
        void should_MapRequestFieldsToConfiguration_when_RequestIsValid() {
            var request = new WifiConfigurationRequest(
                    TestWifiConfigurations.CPE_ID,
                    TestWifiConfigurations.WIFI_BAND,
                    TestWifiConfigurations.SSID,
                    TestWifiConfigurations.ENCRYPTION_TYPE,
                    TestWifiConfigurations.PASSWORD.value()
            );
            var configuration = WifiConfigurationMapper.toDomain(request);

            assertThat(configuration.cpeId()).isEqualTo(request.cpeId());
            assertThat(configuration.wifiBand()).isEqualTo(request.wifiBand());
            assertThat(configuration.ssid()).isEqualTo(request.ssid());
            assertThat(configuration.encryptionType()).isEqualTo(request.encryptionType());
            assertThat(configuration.password()).isEqualTo(TestWifiConfigurations.PASSWORD);
        }

        @Test
        @DisplayName("Maps null request password to configuration without password")
        void should_MapNullRequestPasswordToConfigurationWithoutPassword_when_RequestHasNoPassword() {
            var request = new WifiConfigurationRequest(
                    TestWifiConfigurations.CPE_ID,
                    TestWifiConfigurations.WIFI_BAND,
                    TestWifiConfigurations.SSID,
                    WifiEncryptionType.OPEN,
                    null
            );
            var configuration = WifiConfigurationMapper.toDomain(request);

            assertThat(configuration.password()).isNull();
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when request is null")
        void should_ThrowNullPointerException_when_RequestIsNull() {
            assertThatThrownBy(() -> WifiConfigurationMapper.toDomain(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseMethodTests {

        @Test
        @DisplayName("Maps configuration fields to response")
        void should_MapConfigurationFieldsToResponse_when_ConfigurationIsValid() {
            var password = TestWifiConfigurations.PASSWORD;
            var configuration = TestWifiConfigurations.builder()
                    .withPassword(password)
                    .build();

            var response = WifiConfigurationMapper.toResponse(configuration);

            assertThat(response.cpeId()).isEqualTo(configuration.cpeId());
            assertThat(response.wifiBand()).isEqualTo(configuration.wifiBand());
            assertThat(response.ssid()).isEqualTo(configuration.ssid());
            assertThat(response.encryptionType()).isEqualTo(configuration.encryptionType());
            assertThat(response.password()).isEqualTo(password.value());
        }

        @Test
        @DisplayName("Maps missing configuration password to null response password")
        void should_MapMissingConfigurationPasswordToNullResponsePassword_when_ConfigurationHasNoPassword() {
            var configuration = TestWifiConfigurations.builder()
                    .withEncryptionType(WifiEncryptionType.OPEN)
                    .withPassword(null)
                    .build();

            var response = WifiConfigurationMapper.toResponse(configuration);
            assertThat(response.password()).isNull();
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when configuration is null")
        void should_ThrowNullPointerException_when_ConfigurationIsNull() {
            assertThatThrownBy(() -> WifiConfigurationMapper.toResponse(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
