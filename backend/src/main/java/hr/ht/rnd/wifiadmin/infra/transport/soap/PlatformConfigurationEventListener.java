package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationRetrievedEvent;
import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationUpdatedEvent;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationPersistence;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Spring-backed listener for
 * platform configuration events.
 */
@Component
class PlatformConfigurationEventListener {

    private static final Logger log = LoggerFactory.getLogger(PlatformConfigurationEventListener.class);

    private final WifiConfigurationPersistence persistence;

    PlatformConfigurationEventListener(WifiConfigurationPersistence persistence) {
        Objects.requireNonNull(persistence, "persistence must not be null");
        this.persistence = persistence;
    }

    @Async
    @EventListener
    void on(PlatformConfigurationRetrievedEvent event) {
        log.debug("Persisting retrieved configuration for '{}'",
                event.configuration().cpeId()
        );
        persistence.persist(event.configuration());
    }

    @Async
    @EventListener
    void on(PlatformConfigurationUpdatedEvent event) {
        log.debug("Persisting updated configuration for '{}'",
                event.configuration().cpeId()
        );
        persistence.persist(event.configuration());
    }
}
