package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.infra.transport.soap.cxf.CxfFaultLoggingPolicy;
import hr.ht.rnd.wifiadmin.infra.transport.soap.wsdl.WifiPlatformPortType;
import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;
import hr.ht.rnd.wifiadmin.test.support.TestPlatformExceptions;

import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.RetryTemplate;

import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@WiringIntegrationTest
class PlatformConfigurationTest {

    private static final LocalTime SYNC_SCHEDULE = LocalTime.of(2, 0);
    private static final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration RECEIVE_TIMEOUT = Duration.ofSeconds(30);

    private static final PlatformProperties PLATFORM_PROPERTIES =
            TestPlatformProperties.builder()
                    .withSyncSchedule(SYNC_SCHEDULE)
                    .withConnectionTimeout(CONNECTION_TIMEOUT)
                    .withReceiveTimeout(RECEIVE_TIMEOUT)
                    .withRetryMaxAttempts(3)
                    .build();

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(PlatformConfiguration.class)
            .withPropertyValues(TestPlatformProperties.propertyValues(PLATFORM_PROPERTIES));

    @Nested
    @DisplayName("beans")
    class BeansTests {

        @Test
        @DisplayName("Registers platform infrastructure beans when properties are valid")
        void should_RegisterPlatformInfrastructureBeans_when_PropertiesAreValid() {
            TestApplicationContextRunner.from(runner)
                    .hasBean(WifiPlatformPortType.class)
                    .hasBean(CxfFaultLoggingPolicy.class)
                    .hasBean("platformSyncCronExpression")
                    .hasBean(HTTPClientPolicy.class)
                    .hasBean(RetryPolicy.class)
                    .hasBean(RetryTemplate.class)
                    .hasBean(SoapPlatformClient.class)
                    .hasBean("platformClient")
                    .doesNotFail();
        }

        @Test
        @DisplayName("Does not register resilient platform client when retry attempts are disabled")
        void should_NotRegisterResilientPlatformClient_when_RetryAttemptsAreDisabled() {
            runner.withPropertyValues("platform.retry.max-attempts=1")
                    .run(context -> {
                        assertThat(context).hasNotFailed();
                        assertThat(context.containsBean("platformClient")).isFalse();
                        assertThat(context.getBeansOfType(PlatformClient.class))
                                .containsOnlyKeys("soapPlatformClient");
                    });
        }
    }

    @Nested
    @DisplayName("properties")
    class PropertiesTests {

        @Test
        @DisplayName("Binds configured platform properties")
        void should_BindConfiguredPlatformProperties_when_ContextStarts() {
            Consumer<PlatformProperties> assertion = properties -> {
                assertThat(properties.soapEndpoint()).isEqualTo(PLATFORM_PROPERTIES.soapEndpoint());
                assertThat(properties.cpeIdFormat()).isEqualTo(PLATFORM_PROPERTIES.cpeIdFormat());
                assertThat(properties.cpeIdCount()).isEqualTo(PLATFORM_PROPERTIES.cpeIdCount());
                assertThat(properties.syncOnStartup()).isEqualTo(PLATFORM_PROPERTIES.syncOnStartup());
                assertThat(properties.syncSchedule()).isEqualTo(PLATFORM_PROPERTIES.syncSchedule());
                assertThat(properties.connectionTimeout()).isEqualTo(
                        PLATFORM_PROPERTIES.connectionTimeout()
                );
                assertThat(properties.receiveTimeout()).isEqualTo(
                        PLATFORM_PROPERTIES.receiveTimeout()
                );
                assertThat(properties.retry()).isEqualTo(PLATFORM_PROPERTIES.retry());
            };
            TestApplicationContextRunner.from(runner)
                    .withBean(PlatformProperties.class, assertion)
                    .doesNotFail();
        }

        @Test
        @DisplayName("Fails when platform properties violate validation constraints")
        void should_Fail_when_PlatformPropertiesAreInvalid() {
            TestApplicationContextRunner.from(runner)
                    .withPropertyValues("platform.cpe-id-count=0")
                    .failsWithException(BindValidationException.class);
        }
    }

    @Nested
    @DisplayName("platformSyncCronExpression")
    class PlatformSyncCronExpressionMethodTests {

        @Test
        @DisplayName("Uses sync schedule from platform properties")
        void should_UseSyncScheduleFromPlatformProperties_when_CronExpressionIsCreated() {
            Consumer<String> assertion = cron ->
                    assertThat(cron).isEqualTo("0 0 2 * * *");

            TestApplicationContextRunner.from(runner)
                    .withBean(String.class, assertion)
                    .doesNotFail();
        }
    }

    @Nested
    @DisplayName("platformHttpClientPolicy")
    class PlatformHttpClientPolicyMethodTests {

        @Test
        @DisplayName("Uses timeout values from platform properties")
        void should_UseTimeoutValuesFromPlatformProperties_when_HttpClientPolicyIsCreated() {
            Consumer<HTTPClientPolicy> assertion = policy -> {
                assertThat(policy.getConnectionTimeout()).isEqualTo(
                        PLATFORM_PROPERTIES.connectionTimeout().toMillis()
                );
                assertThat(policy.getReceiveTimeout()).isEqualTo(
                        PLATFORM_PROPERTIES.receiveTimeout().toMillis()
                );
            };
            TestApplicationContextRunner.from(runner)
                    .withBean(HTTPClientPolicy.class, assertion)
                    .doesNotFail();
        }
    }

    @Nested
    @DisplayName("platformRetryTemplate")
    class PlatformRetryTemplateMethodTests {

        @Test
        @DisplayName("Retries platform connection failures")
        void should_RetryPlatformConnectionFailures_when_RetryTemplateIsCreated() {
            Consumer<RetryTemplate> assertion = template -> {
                var policy = template.getRetryPolicy();

                assertThat(policy.shouldRetry(TestPlatformExceptions.failedConnection())).isTrue();
                assertThat(policy.shouldRetry(new IllegalStateException("failure"))).isFalse();
            };
            TestApplicationContextRunner.from(runner)
                    .withBean(RetryTemplate.class, assertion)
                    .doesNotFail();
        }
    }
}
