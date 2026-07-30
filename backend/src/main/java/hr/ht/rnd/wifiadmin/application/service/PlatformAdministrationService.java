package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationRetrievedEvent;
import hr.ht.rnd.wifiadmin.application.event.PlatformConfigurationUpdatedEvent;
import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.application.inbound.PlatformAdministration;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationView;
import hr.ht.rnd.wifiadmin.application.outbound.EventPublisher;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

@Service
public class PlatformAdministrationService implements PlatformAdministration {

    private static final Logger log = LoggerFactory.getLogger(PlatformAdministrationService.class);

    private final PlatformClient client;
    private final WifiConfigurationRepository repository;
    private final WifiConfigurationView view;
    private final EventPublisher events;

    PlatformAdministrationService(
            PlatformClient client,
            WifiConfigurationRepository repository,
            WifiConfigurationView view,
            EventPublisher events
    ) {
        this.client = client;
        this.repository = repository;
        this.view = view;
        this.events = events;
    }

    @Override
    public WifiConfiguration retrieveConfiguration(String cpeId) {
        Objects.requireNonNull(cpeId, "cpeId must not be null");

        info(log).withEvent(Event.RETRIEVE_WIFI_CONFIGURATION_STARTED)
                .withField(Field.CPE_ID, cpeId)
                .log();

        return findConfigurationByCpeId(cpeId).orElseGet(() -> {
            debug(log).withEvent(Event.WIFI_CONFIGURATION_NOT_FOUND)
                    .withField(Field.CPE_ID, cpeId)
                    .log();

            var configuration = client.retrieveConfiguration(cpeId);
            events.publish(new PlatformConfigurationRetrievedEvent(
                    configuration,
                    null
            ));
            return configuration;
        });
    }

    @Override
    public WifiConfiguration updateConfiguration(WifiConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");

        info(log).withEvent(Event.UPDATE_WIFI_CONFIGURATION_STARTED)
                .withField(Field.CPE_ID, configuration.cpeId())
                .log();

        configuration = client.updateConfiguration(configuration);
        events.publish(new PlatformConfigurationUpdatedEvent(configuration));

        return configuration;
    }

    @Override
    public List<WifiConfiguration> retrieveConfigurations() {
        return view.findAll();
    }

    private Optional<WifiConfiguration> findConfigurationByCpeId(String cpeId) {
        try {
            return repository.findByCpeId(cpeId);
        }
        catch (PersistenceException e) {
            warn(log).withEvent(Event.RETRIEVE_WIFI_CONFIGURATION_FAILED)
                    .withField(Field.CPE_ID, cpeId)
                    .withCause(e)
                    .log();

            return Optional.empty();
        }
    }
}
