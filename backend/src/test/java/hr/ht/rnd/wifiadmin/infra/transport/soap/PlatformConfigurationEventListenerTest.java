package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationRetrievedEvent;
import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationUpdatedEvent;
import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationPersistence;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationProjection;
import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.infra.transport.soap.sync.SynchronizationTracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PlatformConfigurationEventListenerTest {

    @Mock
    private WifiConfigurationPersistence persistence;

    @Mock
    private WifiConfigurationProjection projection;

    @Mock
    private SynchronizationTracker tracker;

    private PlatformConfigurationEventListener listener;

    @BeforeEach
    void setupPlatformConfigurationEventListenerTest() {
        listener = new PlatformConfigurationEventListener(
                persistence,
                projection,
                tracker
        );
    }

    @Nested
    @DisplayName("on")
    class OnMethodTests {

        @Test
        @DisplayName("Persists and projects retrieved configuration")
        void should_PersistAndProjectConfiguration_when_EventIsPlatformConfigurationRetrieved() {
            var configuration = TestWifiConfigurations.builder().build();
            var event = new PlatformConfigurationRetrievedEvent(configuration, null);

            listener.on(event);

            Mockito.verify(persistence).persist(configuration, null);
            Mockito.verify(projection).put(configuration);
            Mockito.verifyNoInteractions(tracker);
        }

        @Test
        @DisplayName("Completes synchronization for retrieved synchronized configuration")
        void should_CompleteSynchronization_when_RetrievedConfigurationIsSynchronized() {
            var configuration = TestWifiConfigurations.builder().build();
            var lastSynchronized = LocalDate.parse("2026-07-12");
            var event = new PlatformConfigurationRetrievedEvent(
                    configuration,
                    lastSynchronized
            );
            listener.on(event);

            Mockito.verify(persistence).persist(configuration, lastSynchronized);
            Mockito.verify(projection).put(configuration);
            Mockito.verify(tracker).complete(lastSynchronized);
        }

        @Test
        @DisplayName("Aborts synchronization when retrieved configuration cannot be persisted")
        void should_AbortSynchronization_when_RetrievedConfigurationCannotBePersisted() {
            var configuration = TestWifiConfigurations.builder().build();
            var event = new PlatformConfigurationRetrievedEvent(configuration, null);
            var failure = new PersistenceException(
                    new RuntimeException("Persistence failed")
            );
            Mockito.doThrow(failure).when(persistence).persist(configuration, null);

            assertThatThrownBy(() -> listener.on(event)).isSameAs(failure);
            Mockito.verify(tracker).abort();
            Mockito.verifyNoInteractions(projection);
        }

        @Test
        @DisplayName("Aborts synchronization when retrieved configuration cannot be projected")
        void should_AbortSynchronization_when_RetrievedConfigurationCannotBeProjected() {
            var configuration = TestWifiConfigurations.builder().build();
            var event = new PlatformConfigurationRetrievedEvent(configuration, null);
            var failure = new IllegalStateException("Projection failed");

            Mockito.doThrow(failure).when(projection).put(configuration);

            assertThatThrownBy(() -> listener.on(event)).isSameAs(failure);
            Mockito.verify(persistence).persist(configuration, null);
            Mockito.verify(tracker).abort();
        }

        @Test
        @DisplayName("Persists and projects updated configuration")
        void should_PersistAndProjectConfiguration_when_EventIsPlatformConfigurationUpdated() {
            var configuration = TestWifiConfigurations.builder().build();
            var event = new PlatformConfigurationUpdatedEvent(configuration);

            listener.on(event);

            Mockito.verify(persistence).persist(configuration, null);
            Mockito.verify(projection).put(configuration);
            Mockito.verifyNoInteractions(tracker);
        }

        @Test
        @DisplayName("Does not project updated configuration when persistence fails")
        void should_NotProjectConfiguration_when_UpdatedConfigurationCannotBePersisted() {
            var configuration = TestWifiConfigurations.builder().build();
            var event = new PlatformConfigurationUpdatedEvent(configuration);
            var failure = new PersistenceException(
                    new RuntimeException("Persistence failed")
            );
            Mockito.doThrow(failure).when(persistence).persist(configuration, null);

            assertThatThrownBy(() -> listener.on(event)).isSameAs(failure);
            Mockito.verifyNoInteractions(projection, tracker);
        }
    }
}
