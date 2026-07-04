package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationRetrievedEvent;
import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationUpdatedEvent;
import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.application.inbound.PlatformAdministration;
import hr.ht.rnd.wifiadmin.application.outbound.EventPublisher;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

@Service
public class PlatformAdministrationService implements PlatformAdministration {

    private static final Logger log = LoggerFactory.getLogger(PlatformAdministrationService.class);

    private final PlatformClient client;
    private final WifiConfigurationRepository repository;
    private final EventPublisher events;

    PlatformAdministrationService(
            PlatformClient client,
            WifiConfigurationRepository repository,
            EventPublisher events
    ) {
        Objects.requireNonNull(client, "client must not be null");
        Objects.requireNonNull(repository, "repository must not be null");
        Objects.requireNonNull(events, "events must not be null");

        this.client = client;
        this.repository = repository;
        this.events = events;
    }

    @Override
    public WifiConfiguration retrieveConfiguration(String cpeId) {
        Objects.requireNonNull(cpeId, "cpeId must not be null");

        log.info("Retrieving Wi-Fi configuration for '{}'", cpeId);

        return findConfigurationByCpeId(cpeId).orElseGet(() -> {
            log.debug("Wi-Fi configuration for '{}' not found", cpeId);

            var configuration = client.retrieveConfiguration(cpeId);
            events.publish(new PlatformConfigurationRetrievedEvent(configuration));

            return configuration;
        });
    }

    @Override
    public WifiConfiguration updateConfiguration(WifiConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");

        log.info("Updating Wi-Fi configuration for '{}'",
                configuration.cpeId()
        );
        configuration = client.updateConfiguration(configuration);
        events.publish(new PlatformConfigurationUpdatedEvent(configuration));

        return configuration;
    }

    private Optional<WifiConfiguration> findConfigurationByCpeId(String cpeId) {
        try {
            return repository.findByCpeId(cpeId);
        }
        catch (PersistenceException ignored) {
            return Optional.empty();
        }
    }
}
