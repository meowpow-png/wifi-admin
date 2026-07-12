package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.flow.support.WifiConfigurationRequests;
import hr.ht.rnd.wifiadmin.flow.support.WifiConfigurationResponses;

import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WifiConfigurationCollectionFlowTest extends WifiConfigurationFlowTest {

    @Test
    @DisplayName("Returns projected configurations")
    void should_ReturnProjectedConfigurations_when_ConfigurationsAreAvailable() throws Exception {
        var first = TestWifiConfigurations.forCpeId("CPE_COLLECTION_001");
        var second = TestWifiConfigurations.forCpeId("CPE_COLLECTION_002");

        updateConfiguration(first).andExpect(status().isOk());
        updateConfiguration(second).andExpect(status().isOk());

        var result = wifi.requests().retrieveConfigurations(auth.accessToken())
                .andExpect(status().isOk());

        assertThat(wifi.responses().retrieveConfigurations(result)).contains(
                WifiConfigurationResponses.from(first),
                WifiConfigurationResponses.from(second)
        );
    }

    private ResultActions updateConfiguration(WifiConfiguration configuration) throws Exception {
        return wifi.requests().updateConfiguration(
                auth.accessToken(),
                WifiConfigurationRequests.from(configuration)
        );
    }
}
