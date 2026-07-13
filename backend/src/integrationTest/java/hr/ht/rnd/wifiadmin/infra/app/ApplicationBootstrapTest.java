package hr.ht.rnd.wifiadmin.infra.app;

import hr.ht.rnd.wifiadmin.infra.transport.soap.PlatformProperties;
import hr.ht.rnd.wifiadmin.infra.transport.soap.TestPlatformProperties;
import hr.ht.rnd.wifiadmin.infra.transport.soap.sync.PlatformSynchronizer;
import hr.ht.rnd.wifiadmin.infra.transport.soap.sync.SynchronizationSchedule;
import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

@WiringIntegrationTest
class ApplicationBootstrapTest {

    private static final PlatformProperties PLATFORM_PROPERTIES =
            TestPlatformProperties.builder()
                    .withSyncOnStartup(true)
                    .build();

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(ApplicationBootstrap.class, BootstrapTestConfiguration.class);

    @Nested
    @DisplayName("bootstrap")
    class BootstrapMethodTests {

        @Test
        @DisplayName("Declares event listener for application ready events")
        void should_DeclareEventListener_when_EventIsApplicationReady() {
            TestApplicationContextRunner.from(runner)
                    .hasEventListenerMethod(ApplicationBootstrap.class, ApplicationReadyEvent.class)
                    .doesNotFail();
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class BootstrapTestConfiguration {

        @Bean
        PlatformProperties platformProperties() {
            return PLATFORM_PROPERTIES;
        }

        @Bean
        PlatformSynchronizer platformSynchronizer() {
            return mock(PlatformSynchronizer.class);
        }

        @Bean
        SynchronizationSchedule synchronizationSchedule() {
            return mock(SynchronizationSchedule.class);
        }
    }
}
