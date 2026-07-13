package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationRetrievedEvent;
import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationUpdatedEvent;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationPersistence;
import hr.ht.rnd.wifiadmin.infra.transport.soap.sync.SynchronizationTracker;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

/**
 * Spring-backed listener for
 * platform configuration events.
 */
@Component
class PlatformConfigurationEventListener {

    private static final Logger log = LoggerFactory.getLogger(PlatformConfigurationEventListener.class);

    private final WifiConfigurationPersistence persistence;
    private final SynchronizationTracker tracker;

    PlatformConfigurationEventListener(
            WifiConfigurationPersistence persistence,
            SynchronizationTracker tracker
    ) {
        Objects.requireNonNull(persistence, "persistence must not be null");
        Objects.requireNonNull(tracker, "tracker must not be null");

        this.persistence = persistence;
        this.tracker = tracker;
    }

    @Async
    @EventListener
    void on(PlatformConfigurationRetrievedEvent event) {
        debug(log).withEvent(Event.RETRIEVED_CONFIGURATION_PERSISTENCE_STARTED)
                .withField(Field.CPE_ID, event.configuration().cpeId())
                .log();

        try {
            persistence.persist(
                    event.configuration(),
                    event.lastSynchronized()
            );
            if (event.lastSynchronized() != null) {
                tracker.complete(event.lastSynchronized());
            }
        }
        catch (Exception e) {
            tracker.abort();
            throw e;
        }
    }

    @Async
    @EventListener
    void on(PlatformConfigurationUpdatedEvent event) {
        debug(log).withEvent(Event.UPDATED_CONFIGURATION_PERSISTENCE_STARTED)
                .withField(Field.CPE_ID, event.configuration().cpeId())
                .log();

        persistence.persist(
                event.configuration(),
                null
        );
    }
}
