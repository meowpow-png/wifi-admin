package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.app.TestClock;
import hr.ht.rnd.wifiadmin.infra.app.actuator.ActuatorEndpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "platform.cpe-id-count=2")
class SynchronizationEndpointFlowTest extends WifiConfigurationFlowTest {

    private static final String SYNC_ENDPOINT = "/actuator/" + ActuatorEndpoints.SYNCHRONIZATION;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestClock clock;

    @Test
    @DisplayName("Returns no content when synchronization succeeds")
    void should_ReturnNoContent_when_SynchronizationSucceeds() throws Exception {
        platformClient.addConfigurations(
                TestWifiConfigurations.forCpeId("CPE_001"),
                TestWifiConfigurations.forCpeId("CPE_002")
        );
        synchronize().andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Persists synchronized platform configurations")
    void should_PersistSynchronizedPlatformConfigurations_when_SynchronizationSucceeds() throws Exception {
        var firstConfiguration = TestWifiConfigurations.forCpeId("CPE_001");
        var secondConfiguration = TestWifiConfigurations.forCpeId("CPE_002");

        platformClient.addConfigurations(
                firstConfiguration,
                secondConfiguration
        );
        synchronize().andExpect(status().isNoContent());

        assertThat(findByCpeId(firstConfiguration)).contains(firstConfiguration);
        assertThat(findByCpeId(secondConfiguration)).contains(secondConfiguration);
    }

    @Test
    @DisplayName("Removes stale configurations after synchronization completes")
    void should_RemoveStaleConfigurations_when_SynchronizationCompletes() throws Exception {
        var staleConfiguration = TestWifiConfigurations.forCpeId("CPE_999");
        repository.save(
                staleConfiguration,
                clock.localDate().minusDays(1)
        );
        platformClient.addConfigurations(
                TestWifiConfigurations.forCpeId("CPE_001"),
                TestWifiConfigurations.forCpeId("CPE_002")
        );
        synchronize().andExpect(status().isNoContent());

        assertThat(findByCpeId(staleConfiguration)).isEmpty();
    }

    private ResultActions synchronize() throws Exception {
        return mockMvc.perform(post(SYNC_ENDPOINT));
    }

    private Optional<WifiConfiguration> findByCpeId(WifiConfiguration configuration) {
        return repository.findByCpeId(configuration.cpeId());
    }
}
