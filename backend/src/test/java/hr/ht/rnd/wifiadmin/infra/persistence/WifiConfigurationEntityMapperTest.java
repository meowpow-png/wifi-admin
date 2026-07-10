package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiEncryptionType;
import hr.ht.rnd.wifiadmin.infra.app.TestClock;
import hr.ht.rnd.wifiadmin.infra.security.TestPasswordEncryptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WifiConfigurationEntityMapperTest {

    @Nested
    @DisplayName("toEntity")
    class ToEntityMethodTests {

        @Test
        @DisplayName("Maps configuration fields to entity")
        void should_MapConfigurationFieldsToEntity_when_ConfigurationIsValid() {
            var password = TestWifiConfigurations.PASSWORD;
            var configuration = TestWifiConfigurations.builder()
                    .withPassword(password)
                    .build();

            var lastSynchronized = TestClock.create().localDate();
            var entity = entityMapper().toEntity(
                    configuration,
                    lastSynchronized
            );
            assertThat(entity.getCpeId()).isEqualTo(configuration.cpeId());
            assertThat(entity.getWifiBand()).isEqualTo(configuration.wifiBand());
            assertThat(entity.getSsid()).isEqualTo(configuration.ssid());
            assertThat(entity.getEncryptionType()).isEqualTo(configuration.encryptionType());
            assertThat(entity.getPassword()).isNotEqualTo(password.value());
            assertThat(entity.getLastSynchronized()).isEqualTo(lastSynchronized);
        }

        @Test
        @DisplayName("Maps missing password to null entity password")
        void should_MapMissingPasswordToNullEntityPassword_when_ConfigurationHasNoPassword() {
            var configuration = TestWifiConfigurations.builder()
                    .withEncryptionType(WifiEncryptionType.OPEN)
                    .withPassword(null)
                    .build();

            var entity = entityMapper().toEntity(
                    configuration,
                    null
            );
            assertThat(entity.getPassword()).isNull();
            assertThat(entity.getLastSynchronized()).isNull();
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when configuration is null")
        void should_ThrowNullPointerException_when_ConfigurationIsNull() {
            assertThatThrownBy(() -> entityMapper().toEntity(null, null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("toDomain")
    class ToDomainMethodTests {

        @Test
        @DisplayName("Maps entity fields to configuration")
        void should_MapEntityFieldsToConfiguration_when_EntityIsValid() {
            var password = TestWifiConfigurations.PASSWORD;
            var encryptor = TestPasswordEncryptor.aes();
            var entity = new WifiConfigurationEntity(
                    TestWifiConfigurations.CPE_ID,
                    TestWifiConfigurations.WIFI_BAND,
                    TestWifiConfigurations.SSID,
                    WifiEncryptionType.WPA2_PSK,
                    encryptor.encrypt(password),
                    TestClock.create().localDate()
            );
            var configuration = entityMapper().toDomain(entity);

            assertThat(configuration.cpeId()).isEqualTo(entity.getCpeId());
            assertThat(configuration.wifiBand()).isEqualTo(entity.getWifiBand());
            assertThat(configuration.ssid()).isEqualTo(entity.getSsid());
            assertThat(configuration.encryptionType()).isEqualTo(entity.getEncryptionType());
            assertThat(configuration.password()).isEqualTo(password);
        }

        @Test
        @DisplayName("Maps null entity password to configuration without password")
        void should_MapNullEntityPasswordToConfigurationWithoutPassword_when_EntityHasNoPassword() {
            var entity = new WifiConfigurationEntity(
                    TestWifiConfigurations.CPE_ID,
                    TestWifiConfigurations.WIFI_BAND,
                    TestWifiConfigurations.SSID,
                    WifiEncryptionType.OPEN,
                    null,
                    TestClock.create().localDate()
            );
            var configuration = entityMapper().toDomain(entity);

            assertThat(configuration.password()).isNull();
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when entity is null")
        void should_ThrowNullPointerException_when_EntityIsNull() {
            assertThatThrownBy(() -> entityMapper().toDomain(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when entity contains invalid data")
        void should_ThrowIllegalArgumentException_when_EntityContainsInvalidData() {
            var entity = new WifiConfigurationEntity(
                    " ",
                    TestWifiConfigurations.WIFI_BAND,
                    TestWifiConfigurations.SSID,
                    WifiEncryptionType.OPEN,
                    null,
                    TestClock.create().localDate()
            );
            assertThatThrownBy(() -> entityMapper().toDomain(entity))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when entity password cannot be decrypted")
        void should_ThrowIllegalArgumentException_when_EntityPasswordCannotBeDecrypted() {
            var entity = new WifiConfigurationEntity(
                    TestWifiConfigurations.CPE_ID,
                    TestWifiConfigurations.WIFI_BAND,
                    TestWifiConfigurations.SSID,
                    TestWifiConfigurations.ENCRYPTION_TYPE,
                    TestWifiConfigurations.PASSWORD.value(),
                    TestClock.create().localDate()
            );
            assertThatThrownBy(() -> entityMapper().toDomain(entity))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private static WifiConfigurationEntityMapper entityMapper() {
        var encryptor = TestPasswordEncryptor.aes();
        return new WifiConfigurationEntityMapper(encryptor);
    }
}
