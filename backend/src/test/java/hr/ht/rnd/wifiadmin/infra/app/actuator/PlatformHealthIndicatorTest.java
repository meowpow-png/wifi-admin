package hr.ht.rnd.wifiadmin.infra.app.actuator;

import hr.ht.rnd.wifiadmin.application.exception.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformConnectionException;
import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.transport.soap.PlatformProperties;
import hr.ht.rnd.wifiadmin.infra.transport.soap.SoapPlatformClient;
import hr.ht.rnd.wifiadmin.infra.transport.soap.TestPlatformProperties;

import org.springframework.boot.health.contributor.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PlatformHealthIndicatorTest {

    @Nested
    @DisplayName("health")
    @SuppressWarnings("DataFlowIssue")
    class HealthMethodTests {

        @Mock
        private SoapPlatformClient client;

        private PlatformProperties properties;

        @BeforeEach
        void setupHealthMethodTest() {
            properties = TestPlatformProperties.builder().build();
        }

        @Test
        @DisplayName("Returns up when platform configuration can be retrieved")
        void should_ReturnUp_when_PlatformConfigurationCanBeRetrieved() {
            var indicator = new PlatformHealthIndicator(client, properties);
            var configuration = TestWifiConfigurations.builder().build();

            mockClientRetrieves(configuration);

            var health = indicator.health();

            assertThat(health.getStatus()).isEqualTo(Status.UP);
            assertThat(health.getDetails()).isEmpty();
        }

        @Test
        @DisplayName("Returns up when health check CPE is not found")
        void should_ReturnUp_when_HealthCheckCpeIsNotFound() {
            var indicator = new PlatformHealthIndicator(client, properties);

            mockClientThrows(new CpeNotFoundException(
                    TestWifiConfigurations.CPE_ID,
                    new RuntimeException()
            ));
            var health = indicator.health();

            assertThat(health.getStatus()).isEqualTo(Status.UP);
            assertThat(health.getDetails()).isEmpty();
        }

        @Test
        @DisplayName("Returns down when platform communication fails")
        void should_ReturnDown_when_PlatformCommunicationFails() {
            var indicator = new PlatformHealthIndicator(client, properties);
            var exception = new PlatformConnectionException("Connection failed", new RuntimeException());

            mockClientThrows(exception);

            var health = indicator.health();
            var status = health.getStatus();
            var details = health.getDetails();

            assertThat(status).isEqualTo(Status.DOWN);
            assertThat(details).hasEntrySatisfying("error", value ->
                    assertThat(value).isInstanceOf(String.class).isEqualTo(exception.toString())
            );
        }

        private void mockClientThrows(Exception exception) {
            Mockito.when(client.retrieveConfiguration(Mockito.anyString()))
                    .thenThrow(exception);
        }

        private void mockClientRetrieves(WifiConfiguration configuration) {
            Mockito.when(client.retrieveConfiguration(Mockito.anyString()))
                    .thenReturn(configuration);
        }
    }
}
