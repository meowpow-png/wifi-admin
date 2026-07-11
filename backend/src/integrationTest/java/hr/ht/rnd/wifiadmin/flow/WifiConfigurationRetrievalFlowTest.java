package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.flow.support.WifiConfigurationResponses;
import hr.ht.rnd.wifiadmin.infra.app.TestClock;
import hr.ht.rnd.wifiadmin.test.support.TestPlatformExceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WifiConfigurationRetrievalFlowTest extends WifiConfigurationFlowTest {

    @Autowired
    private TestClock clock;

    @Test
    @DisplayName("Returns cached configuration when configuration exists")
    void should_ReturnCachedConfiguration_when_ConfigurationExists() throws Exception {
        var configuration = TestWifiConfigurations.builder().build();
        repository.save(configuration, clock.localDate());

        var result = retrieveConfiguration(configuration)
                .andExpect(status().isOk());

        assertThat(wifi.responses().retrieveConfiguration(result)).
                isEqualTo(WifiConfigurationResponses.from(configuration));
    }

    @Test
    @DisplayName("Returns platform configuration when cache is missing")
    void should_ReturnPlatformConfiguration_when_CacheIsMissing() throws Exception {
        var configuration = TestWifiConfigurations.builder().build();
        platformClient.addConfiguration(configuration);

        var result = retrieveConfiguration(configuration)
                .andExpect(status().isOk());

        assertThat(wifi.responses().retrieveConfiguration(result))
                .isEqualTo(WifiConfigurationResponses.from(configuration));
    }

    @Test
    @DisplayName("Persists retrieved configuration when cache is missing")
    void should_PersistRetrievedConfiguration_when_CacheIsMissing() throws Exception {
        var configuration = TestWifiConfigurations.builder().build();
        platformClient.addConfiguration(configuration);

        retrieveConfiguration(configuration).andExpect(status().isOk());

        assertThat(repository.findByCpeId(configuration.cpeId()))
                .contains(configuration);
    }

    @Test
    @DisplayName("Returns not found when platform reports missing CPE")
    void should_ReturnNotFound_when_PlatformReportsMissingCpe() throws Exception {
        wifi.requests().retrieveConfiguration(auth.accessToken(), TestWifiConfigurations.CPE_ID)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Returns bad gateway when platform response is invalid")
    void should_ReturnBadGateway_when_PlatformResponseIsInvalid() throws Exception {
        var cpeId = TestWifiConfigurations.CPE_ID;

        platformClient.onRetrieveConfiguration(cpeId, () -> {
                    throw TestPlatformExceptions.invalidResponse();
                }
        );
        wifi.requests().retrieveConfiguration(auth.accessToken(), cpeId)
                .andExpect(status().isBadGateway());
    }

    @Test
    @DisplayName("Returns bad gateway when platform transport fails")
    void should_ReturnBadGateway_when_PlatformTransportFails() throws Exception {
        var cpeId = TestWifiConfigurations.CPE_ID;

        platformClient.onRetrieveConfiguration(cpeId, () -> {
                    throw TestPlatformExceptions.failedConnection();
                }
        );
        wifi.requests().retrieveConfiguration(auth.accessToken(), cpeId)
                .andExpect(status().isBadGateway());
    }

    private ResultActions retrieveConfiguration(WifiConfiguration configuration) throws Exception {
        return wifi.requests().retrieveConfiguration(
                auth.accessToken(),
                configuration.cpeId()
        );
    }
}
