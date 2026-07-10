package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.application.exception.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformConnectionException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformResponseException;
import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiEncryptionType;
import hr.ht.rnd.wifiadmin.flow.support.WifiConfigurationRequests;
import hr.ht.rnd.wifiadmin.flow.support.WifiConfigurationResponses;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.WifiConfigurationRequest;

import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WifiConfigurationUpdateFlowTest extends WifiConfigurationFlowTest {

    @Test
    @DisplayName("Returns updated configuration when request is valid")
    void should_ReturnUpdatedConfiguration_when_RequestIsValid() throws Exception {
        var configuration = TestWifiConfigurations.builder().build();

        var result = updateConfiguration(configuration)
                .andExpect(status().isOk());

        assertThat(wifi.responses().retrieveConfiguration(result))
                .isEqualTo(WifiConfigurationResponses.from(configuration));
    }

    @Test
    @DisplayName("Updates platform configuration when request is valid")
    void should_UpdatePlatformConfiguration_when_RequestIsValid() throws Exception {
        var configuration = TestWifiConfigurations.builder().build();

        updateConfiguration(configuration).andExpect(status().isOk());

        assertThat(platformClient.retrieveConfiguration(configuration.cpeId()))
                .isEqualTo(configuration);
    }

    @Test
    @DisplayName("Persists updated configuration when request is valid")
    void should_PersistUpdatedConfiguration_when_RequestIsValid() throws Exception {
        var configuration = TestWifiConfigurations.builder().build();

        updateConfiguration(configuration).andExpect(status().isOk());

        assertThat(repository.findByCpeId(configuration.cpeId()))
                .contains(configuration);
    }

    @Test
    @DisplayName("Returns bad request when request body is invalid")
    void should_ReturnBadRequest_when_RequestBodyIsInvalid() throws Exception {
        var body = "{\"cpeId\":\"CPE_001\"";

        wifi.requests().updateConfiguration(auth.accessToken(), body)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns bad request when enum value is invalid")
    void should_ReturnBadRequest_when_EnumValueIsInvalid() throws Exception {
        //@formatter:off
        var body = """
        {
          "cpeId": "CPE_001",
          "wifiBand": "INVALID",
          "ssid": "Test Wi-Fi",
          "encryptionType": "WPA2_PSK",
          "password": "password"
        }
        """;
        //@formatter:on
        wifi.requests().updateConfiguration(auth.accessToken(), body)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns bad request when Wi-Fi data is invalid")
    void should_ReturnBadRequest_when_WifiDataIsInvalid() throws Exception {
        var request = new WifiConfigurationRequest(
                TestWifiConfigurations.CPE_ID,
                TestWifiConfigurations.WIFI_BAND,
                TestWifiConfigurations.SSID,
                WifiEncryptionType.WPA2_PSK,
                null
        );
        wifi.requests().updateConfiguration(auth.accessToken(), request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns not found when platform reports missing CPE")
    void should_ReturnNotFound_when_PlatformReportsMissingCpe() throws Exception {
        var configuration = TestWifiConfigurations.builder().build();
        platformClient.onUpdateConfiguration(configuration.cpeId(), () -> {
                    throw new CpeNotFoundException(
                            configuration.cpeId(),
                            new RuntimeException("missing")
                    );
                }
        );
        updateConfiguration(configuration)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Returns bad gateway when platform response is invalid")
    void should_ReturnBadGateway_when_PlatformResponseIsInvalid() throws Exception {
        var configuration = TestWifiConfigurations.builder().build();
        platformClient.onUpdateConfiguration(configuration.cpeId(), () -> {
                    throw new PlatformResponseException(new RuntimeException("invalid"));
                }
        );
        updateConfiguration(configuration)
                .andExpect(status().isBadGateway());
    }

    @Test
    @DisplayName("Returns bad gateway when platform transport fails")
    void should_ReturnBadGateway_when_PlatformTransportFails() throws Exception {
        var configuration = TestWifiConfigurations.builder().build();
        platformClient.onUpdateConfiguration(configuration.cpeId(), () -> {
                    throw new PlatformConnectionException(
                            "Connection failed",
                            new RuntimeException("failure")
                    );
                }
        );
        updateConfiguration(configuration)
                .andExpect(status().isBadGateway());
    }

    private ResultActions updateConfiguration(WifiConfiguration configuration) throws Exception {
        return wifi.requests().updateConfiguration(
                auth.accessToken(),
                WifiConfigurationRequests.from(configuration)
        );
    }
}
