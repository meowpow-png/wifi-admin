package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.exception.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformConnectionException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformResponseException;
import hr.ht.rnd.wifiadmin.common.StructuredLog;
import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.test.support.PlatformClientAction;
import hr.ht.rnd.wifiadmin.test.support.RetryableTestPlatformClient;
import hr.ht.rnd.wifiadmin.test.support.TestPlatformExceptions;

import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.retry.RetryTemplate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(OutputCaptureExtension.class)
class ResilientSoapPlatformClientTest {

    private static final int MAX_ATTEMPTS = 3;

    private final RetryableTestPlatformClient delegate = new RetryableTestPlatformClient();
    private final ResilientSoapPlatformClient client =
            new ResilientSoapPlatformClient(retryTemplate(), delegate);

    @Nested
    @DisplayName("retrieveConfiguration")
    class RetrieveConfigurationMethodTests {

        @Test
        @DisplayName("Retries transient platform connection failures")
        void should_RetryTransientPlatformConnectionFailures_when_RetrieveEventuallySucceeds() {
            var configuration = TestWifiConfigurations.builder().build();
            delegate.onRetrieveConfiguration(
                    PlatformClientAction.failConnection(),
                    PlatformClientAction.returnConfiguration(configuration)
            );
            var result = client.retrieveConfiguration(configuration.cpeId());

            assertThat(result).isEqualTo(configuration);
            assertThat(delegate.retrieveAttempts()).isEqualTo(2);
        }

        @Test
        @DisplayName("Propagates connection failure when retries are exhausted")
        void should_PropagateConnectionFailure_when_RetriesAreExhausted(CapturedOutput output) {
            var cpeId = TestWifiConfigurations.CPE_ID;
            delegate.onRetrieveConfiguration(
                    PlatformClientAction.failConnection(),
                    PlatformClientAction.failConnection(),
                    PlatformClientAction.failConnection()
            );
            assertThatThrownBy(() -> client.retrieveConfiguration(cpeId))
                    .isInstanceOf(PlatformConnectionException.class);

            assertThat(delegate.retrieveAttempts()).isEqualTo(MAX_ATTEMPTS);
            assertThat(output)
                    .contains(StructuredLog.Event.PLATFORM_RETRY_EXHAUSTED.name())
                    .contains("GetCpeID")
                    .contains(cpeId);
        }

        @Test
        @DisplayName("Does not retry platform response failures")
        void should_NotRetry_when_PlatformResponseFails() {
            var cpeId = TestWifiConfigurations.CPE_ID;
            delegate.onRetrieveConfiguration(
                    PlatformClientAction.returnInvalidResponse()
            );
            assertThatThrownBy(() -> client.retrieveConfiguration(cpeId))
                    .isInstanceOf(PlatformResponseException.class);

            assertThat(delegate.retrieveAttempts()).isEqualTo(1);
        }

        @Test
        @DisplayName("Does not retry missing CPE failures")
        void should_NotRetry_when_CpeIsMissing() {
            var cpeId = TestWifiConfigurations.CPE_ID;
            delegate.onRetrieveConfiguration(
                    PlatformClientAction.failFindingCpeId(cpeId)
            );
            assertThatThrownBy(() -> client.retrieveConfiguration(cpeId))
                    .isInstanceOf(CpeNotFoundException.class);

            assertThat(delegate.retrieveAttempts()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("updateConfiguration")
    class UpdateConfigurationMethodTests {

        @Test
        @DisplayName("Retries transient platform connection failures")
        void should_RetryTransientPlatformConnectionFailures_when_UpdateEventuallySucceeds() {
            var configuration = TestWifiConfigurations.builder().build();
            delegate.onUpdateConfiguration(
                    PlatformClientAction.failConnection(),
                    PlatformClientAction.returnConfiguration(configuration)
            );
            var result = client.updateConfiguration(configuration);

            assertThat(result).isEqualTo(configuration);
            assertThat(delegate.updateAttempts()).isEqualTo(2);
        }

        @Test
        @DisplayName("Does not retry platform response failures")
        void should_NotRetry_when_PlatformResponseFails() {
            var configuration = TestWifiConfigurations.builder().build();
            delegate.onUpdateConfiguration(() -> {
                throw TestPlatformExceptions.invalidResponse();
            });
            assertThatThrownBy(() -> client.updateConfiguration(configuration))
                    .isInstanceOf(PlatformResponseException.class);

            assertThat(delegate.updateAttempts()).isEqualTo(1);
        }
    }

    private static RetryTemplate retryTemplate() {
        var properties = TestPlatformProperties.builder()
                .withRetryMaxAttempts(MAX_ATTEMPTS)
                .withRetryDelay(Duration.ofMillis(100))
                .withRetryMaxDelay(Duration.ofMillis(100))
                .withRetryDelayMultiplier(2.0)
                .build();

        var configuration = new PlatformConfiguration();
        var retryPolicy = configuration.platformRetryPolicy(properties);

        return configuration.platformRetryTemplate(retryPolicy);
    }
}
