package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.exception.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformCommunicationException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformResponseException;
import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.infra.transport.soap.support.MockSoapPlatform;
import hr.ht.rnd.wifiadmin.test.autoconfigure.IntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.MockWebServerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
@SpringBootTest(classes = {
        PlatformConfiguration.class,
        SoapPlatformClient.class
})
class SoapPlatformClientTest extends MockWebServerTest {

    @Autowired
    private SoapPlatformClient client;

    @DynamicPropertySource
    static void registerSoapPlatformClientProperties(DynamicPropertyRegistry registry) {
        registry.add("platform.soap-endpoint", () -> platform().endpoint());
    }

    @BeforeEach
    void setupSoapPlatformClientTest() throws InterruptedException {
        platform().drainRequests();
    }

    @Nested
    @DisplayName("retrieveConfiguration")
    class RetrieveConfigurationMethodTests {

        @Test
        @DisplayName("Returns configuration when platform returns configuration")
        void should_ReturnConfiguration_when_PlatformReturnsConfiguration() {
            var configuration = TestWifiConfigurations.builder().build();

            platform().enqueueRetrieved(configuration);

            var result = client.retrieveConfiguration(configuration.cpeId());
            assertThat(result).isEqualTo(configuration);
        }

        @Test
        @DisplayName("Returns configuration when platform response has leading whitespace")
        void should_ReturnConfiguration_when_PlatformResponseHasLeadingWhitespace() {
            var configuration = TestWifiConfigurations.builder().build();

            platform().enqueueRetrievedWithLeadingWhitespace(configuration);

            var result = client.retrieveConfiguration(configuration.cpeId());
            assertThat(result).isEqualTo(configuration);
        }

        @Test
        @DisplayName("Sends GetCpeId request when retrieving configuration")
        void should_SendGetCpeIdRequest_when_RetrievingConfiguration() throws InterruptedException {
            var configuration = TestWifiConfigurations.builder().build();

            platform().enqueueRetrieved(configuration);
            client.retrieveConfiguration(configuration.cpeId());

            platform().assertRetrieveRequest(configuration.cpeId());
        }

        @Test
        @DisplayName("Throws CpeNotFoundException when platform returns not found fault")
        void should_ThrowCpeNotFoundException_when_PlatformReturnsNotFoundFault() {
            platform().enqueueNotFoundFault();

            assertThatThrownBy(() -> client.retrieveConfiguration(TestWifiConfigurations.CPE_ID))
                    .isInstanceOf(CpeNotFoundException.class);
        }

        @Test
        @DisplayName("Throws PlatformResponseException when response is invalid")
        void should_ThrowPlatformResponseException_when_ResponseIsInvalid() {
            platform().enqueueInvalidRetrieveResponse();

            assertThatThrownBy(() -> client.retrieveConfiguration(TestWifiConfigurations.CPE_ID))
                    .isInstanceOf(PlatformResponseException.class);
        }

        @Test
        @DisplayName("Throws PlatformCommunicationException when platform disconnects")
        void should_ThrowPlatformCommunicationException_when_PlatformDisconnects() {
            platform().enqueueDisconnectAtStart();

            assertThatThrownBy(() -> client.retrieveConfiguration(TestWifiConfigurations.CPE_ID))
                    .isInstanceOf(PlatformCommunicationException.class);
        }
    }

    @Nested
    @DisplayName("updateConfiguration")
    class UpdateConfigurationMethodTests {

        @Test
        @DisplayName("Returns configuration when platform updates configuration")
        void should_ReturnConfiguration_when_PlatformUpdatesConfiguration() {
            var configuration = TestWifiConfigurations.builder().build();

            platform().enqueueUpdated(configuration);

            var result = client.updateConfiguration(configuration);
            assertThat(result).isEqualTo(configuration);
        }

        @Test
        @DisplayName("Sends UpdateCpeId request when updating configuration")
        void should_SendUpdateCpeIdRequest_when_UpdatingConfiguration() throws InterruptedException {
            var configuration = TestWifiConfigurations.builder().build();

            platform().enqueueUpdated(configuration);
            client.updateConfiguration(configuration);

            platform().assertUpdateRequest(configuration);
        }

        @Test
        @DisplayName("Throws CpeNotFoundException when platform returns not found fault")
        void should_ThrowCpeNotFoundException_when_PlatformReturnsNotFoundFault() {
            var configuration = TestWifiConfigurations.builder().build();

            platform().enqueueNotFoundFault();

            assertThatThrownBy(() -> client.updateConfiguration(configuration))
                    .isInstanceOf(CpeNotFoundException.class);
        }

        @Test
        @DisplayName("Throws PlatformResponseException when response is invalid")
        void should_ThrowPlatformResponseException_when_ResponseIsInvalid() {
            var configuration = TestWifiConfigurations.builder().build();

            platform().enqueueInvalidUpdateResponse();

            assertThatThrownBy(() -> client.updateConfiguration(configuration))
                    .isInstanceOf(PlatformResponseException.class);
        }
    }

    private static MockSoapPlatform platform() {
        return MockSoapPlatform.create(server());
    }
}
