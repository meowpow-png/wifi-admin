package hr.ht.rnd.wifiadmin.domain.wifi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WifiConfigurationTest {

    @Nested
    @DisplayName("constructor")
    class ConstructorTests {

        @Test
        @DisplayName("Creates configuration when required fields are valid")
        void should_CreateConfiguration_when_RequiredFieldsAreValid() {
            var configuration = TestWifiConfigurations.builder()
                    .withCpeId(TestWifiConfigurations.CPE_ID)
                    .withWifiBand(TestWifiConfigurations.WIFI_BAND)
                    .withSsid(TestWifiConfigurations.SSID)
                    .withEncryptionType(TestWifiConfigurations.ENCRYPTION_TYPE)
                    .withPassword(TestWifiConfigurations.PASSWORD)
                    .build();

            assertThat(configuration.cpeId()).isEqualTo(TestWifiConfigurations.CPE_ID);
            assertThat(configuration.wifiBand()).isEqualTo(TestWifiConfigurations.WIFI_BAND);
            assertThat(configuration.ssid()).isEqualTo(TestWifiConfigurations.SSID);
            assertThat(configuration.encryptionType()).isEqualTo(TestWifiConfigurations.ENCRYPTION_TYPE);
            assertThat(configuration.password()).isEqualTo(TestWifiConfigurations.PASSWORD);
        }

        @Test
        @DisplayName("Defaults encryption type when encryption type is null")
        void should_DefaultEncryptionType_when_EncryptionTypeIsNull() {
            var configuration = TestWifiConfigurations.builder()
                    .withEncryptionType(null)
                    .withPassword(null)
                    .build();

            var actual = configuration.encryptionType();
            var expected = WifiEncryptionType.OPEN;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("Creates configuration without password when encryption type does not require password")
        void should_CreateConfigurationWithoutPassword_when_EncryptionTypeDoesNotRequirePassword() {
            var configuration = TestWifiConfigurations.builder()
                    .withEncryptionType(WifiEncryptionType.WPA2_ENTERPRISE)
                    .withPassword(null)
                    .build();

            assertThat(configuration.password()).isNull();
        }

        @Test
        @DisplayName("Throws NullPointerException when CPE ID is null")
        void should_ThrowNullPointerException_when_CpeIdIsNull() {
            assertThatThrownBy(() -> TestWifiConfigurations.forCpeId(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when CPE ID is blank")
        void should_ThrowIllegalArgumentException_when_CpeIdIsBlank() {
            assertThatThrownBy(() -> TestWifiConfigurations.forCpeId(" "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Throws NullPointerException when Wi-Fi band is null")
        void should_ThrowNullPointerException_when_WifiBandIsNull() {
            var builder = TestWifiConfigurations.builder();

            assertThatThrownBy(() -> builder.withWifiBand(null).build())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws NullPointerException when SSID is null")
        void should_ThrowNullPointerException_when_SsidIsNull() {
            var builder = TestWifiConfigurations.builder();

            assertThatThrownBy(() -> builder.withSsid(null).build())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when SSID is blank")
        void should_ThrowIllegalArgumentException_when_SsidIsBlank() {
            var builder = TestWifiConfigurations.builder();

            assertThatThrownBy(() -> builder.withSsid(" ").build())
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Throws NullPointerException when password is required and missing")
        void should_ThrowNullPointerException_when_PasswordIsRequiredAndMissing() {
            var builder = TestWifiConfigurations.builder();

            assertThatThrownBy(() -> builder.withPassword(null).build())
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
