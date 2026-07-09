package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationRetrievedEvent;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationSynchronization;
import hr.ht.rnd.wifiadmin.application.outbound.EventPublisher;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

@Service
class ConfigurationSynchronizationService implements WifiConfigurationSynchronization {

    private final EventPublisher events;
    private final PlatformClient platform;
    private final Clock clock;

    ConfigurationSynchronizationService(
            EventPublisher events,
            PlatformClient platform,
            Clock clock
    ) {
        this.events = events;
        this.platform = platform;
        this.clock = clock;
    }

    @Override
    public void synchronize(String cpeId) {
        Objects.requireNonNull(cpeId, "cpeId must not be null");

        var configuration = platform.retrieveConfiguration(cpeId);
        var event = new PlatformConfigurationRetrievedEvent(
                configuration,
                LocalDate.now(clock)
        );
        events.publish(event);
    }
}
