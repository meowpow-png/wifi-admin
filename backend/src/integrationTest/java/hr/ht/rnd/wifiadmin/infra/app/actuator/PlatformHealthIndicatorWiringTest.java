package hr.ht.rnd.wifiadmin.infra.app.actuator;

import hr.ht.rnd.wifiadmin.infra.transport.soap.PlatformProperties;
import hr.ht.rnd.wifiadmin.infra.transport.soap.SoapPlatformClient;
import hr.ht.rnd.wifiadmin.infra.transport.soap.TestPlatformProperties;
import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

@WiringIntegrationTest
class PlatformHealthIndicatorWiringTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(
                    PlatformHealthIndicator.class,
                    HealthIndicatorTestConfiguration.class
            );

    @Nested
    @DisplayName("beans")
    class BeansTests {

        @Test
        @DisplayName("Registers platform health indicator")
        void should_RegisterPlatformHealthIndicator_when_ContextStarts() {
            TestApplicationContextRunner.from(runner)
                    .hasBean(ActuatorEndpoints.HEALTH)
                    .hasBean(HealthIndicator.class)
                    .hasBean(PlatformHealthIndicator.class)
                    .doesNotFail();
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class HealthIndicatorTestConfiguration {

        @Bean
        SoapPlatformClient soapPlatformClient() {
            return mock(SoapPlatformClient.class);
        }

        @Bean
        PlatformProperties platformProperties() {
            return TestPlatformProperties.builder().build();
        }
    }
}
