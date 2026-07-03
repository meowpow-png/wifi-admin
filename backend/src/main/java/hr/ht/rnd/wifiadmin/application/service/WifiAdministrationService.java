package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.inbound.WifiAdministration;
import hr.ht.rnd.wifiadmin.application.outbound.PersistenceException;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.application.inbound.WifiPersistence;
import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class WifiAdministrationService implements WifiAdministration {

    private final PlatformClient client;
    private final WifiConfigurationRepository repository;
    private final WifiPersistence persistence;

    WifiAdministrationService(
            PlatformClient client,
            WifiConfigurationRepository repository,
            WifiPersistence persistence
    ) {
        Objects.requireNonNull(client, "client must not be null");
        Objects.requireNonNull(repository, "repository must not be null");
        Objects.requireNonNull(persistence, "persistence must not be null");

        this.client = client;
        this.repository = repository;
        this.persistence = persistence;
    }

    @Override
    public WifiConfiguration retrieveConfiguration(String cpeId) {
        Objects.requireNonNull(cpeId, "cpeId must not be null");

        return findConfigurationByCpeId(cpeId).orElseGet(() -> {
            var configuration = client.retrieveConfiguration(cpeId);
            persistence.persist(configuration);

            return configuration;
        });
    }

    @Override
    public WifiConfiguration updateConfiguration(WifiConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");

        configuration = client.updateConfiguration(configuration);
        persistence.persist(configuration);

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
