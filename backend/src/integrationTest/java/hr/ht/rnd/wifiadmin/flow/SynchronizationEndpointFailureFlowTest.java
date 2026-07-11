package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.app.TestClock;
import hr.ht.rnd.wifiadmin.infra.app.actuator.ActuatorEndpoints;
import hr.ht.rnd.wifiadmin.test.support.TestPlatformExceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestPropertySource(properties = "platform.cpe-id-count=2")
class SynchronizationEndpointFailureFlowTest extends WifiConfigurationFlowTest {

    private static final String SYNC_ENDPOINT = "/actuator/" + ActuatorEndpoints.SYNCHRONIZATION;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestClock clock;

    @Test
    @DisplayName("Propagates failure when platform transport fails")
    void should_PropagateFailure_when_PlatformTransportFails() {
        var exception = TestPlatformExceptions.connectionException();

        platformClient.addConfiguration(TestWifiConfigurations.forCpeId("CPE_001"));
        platformClient.onRetrieveConfiguration("CPE_002", () -> {
                    throw exception;
                }
        );
        assertThatThrownBy(this::synchronize).hasCause(exception);
    }

    @Test
    @DisplayName("Aborts synchronization when platform transport fails")
    void should_AbortSynchronization_when_PlatformTransportFails() {
        var exception = TestPlatformExceptions.connectionException();
        var staleConfiguration = TestWifiConfigurations.forCpeId("CPE_999");

        repository.save(
                staleConfiguration,
                clock.localDate().minusDays(1)
        );
        platformClient.addConfiguration(TestWifiConfigurations.forCpeId("CPE_001"));
        platformClient.onRetrieveConfiguration("CPE_002", () -> {
                    throw exception;
                }
        );
        assertThatThrownBy(this::synchronize).hasCause(exception);
        assertThat(findByCpeId(staleConfiguration)).contains(staleConfiguration);
    }

    @Test
    @DisplayName("Aborts synchronization when platform reports missing CPE")
    void should_AbortSynchronization_when_PlatformReportsMissingCpe() {
        var exception = TestPlatformExceptions.cpeNotFound("CPE_002");
        var staleConfiguration = TestWifiConfigurations.forCpeId("CPE_999");

        repository.save(
                staleConfiguration,
                clock.localDate().minusDays(1)
        );
        platformClient.addConfiguration(TestWifiConfigurations.forCpeId("CPE_001"));
        platformClient.onRetrieveConfiguration("CPE_002", () -> {
                    throw exception;
                }
        );
        assertThatThrownBy(this::synchronize).hasCause(exception);
        assertThat(findByCpeId(staleConfiguration)).contains(staleConfiguration);
    }

    private void synchronize() throws Exception {
        mockMvc.perform(post(SYNC_ENDPOINT));
    }

    private Optional<WifiConfiguration> findByCpeId(WifiConfiguration configuration) {
        return repository.findByCpeId(configuration.cpeId());
    }
}
