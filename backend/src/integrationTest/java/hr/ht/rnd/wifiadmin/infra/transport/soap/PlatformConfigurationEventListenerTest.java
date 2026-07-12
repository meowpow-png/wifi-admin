package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationRetrievedEvent;
import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationUpdatedEvent;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationPersistence;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationProjection;
import hr.ht.rnd.wifiadmin.application.outbound.ConfigurationChangeNotifier;
import hr.ht.rnd.wifiadmin.infra.transport.soap.sync.SynchronizationTracker;
import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

@WiringIntegrationTest
class PlatformConfigurationEventListenerTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(
                    PlatformConfigurationEventListener.class,
                    EventListenerTestConfiguration.class
            );

    @Nested
    @DisplayName("on")
    class OnMethodTests {

        @Test
        @DisplayName("Declares async event listener for retrieved configuration events")
        void should_DeclareAsyncEventListener_when_EventIsPlatformConfigurationRetrieved() {
            TestApplicationContextRunner.from(runner)
                    .hasEventListenerMethod(
                            PlatformConfigurationEventListener.class,
                            PlatformConfigurationRetrievedEvent.class
                    )
                    .hasAsyncMethod(
                            PlatformConfigurationEventListener.class,
                            PlatformConfigurationRetrievedEvent.class
                    )
                    .doesNotFail();
        }

        @Test
        @DisplayName("Declares async event listener for updated configuration events")
        void should_DeclareAsyncEventListener_when_EventIsPlatformConfigurationUpdated() {
            TestApplicationContextRunner.from(runner)
                    .hasEventListenerMethod(
                            PlatformConfigurationEventListener.class,
                            PlatformConfigurationUpdatedEvent.class
                    )
                    .hasAsyncMethod(
                            PlatformConfigurationEventListener.class,
                            PlatformConfigurationUpdatedEvent.class
                    )
                    .doesNotFail();
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class EventListenerTestConfiguration {

        @Bean
        WifiConfigurationPersistence wifiConfigurationPersistence() {
            return mock(WifiConfigurationPersistence.class);
        }

        @Bean
        WifiConfigurationProjection wifiConfigurationProjection() {
            return mock(WifiConfigurationProjection.class);
        }

        @Bean
        ConfigurationChangeNotifier configurationChangeNotifier() {
            return mock(ConfigurationChangeNotifier.class);
        }

        @Bean
        SynchronizationTracker synchronizationTracker() {
            return mock(SynchronizationTracker.class);
        }
    }
}
