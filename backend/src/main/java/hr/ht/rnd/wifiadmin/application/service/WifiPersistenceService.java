package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationPersistence;
import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Application service coordinating
 * Wi-Fi configuration persistence.
 */
@Service
class WifiPersistenceService implements WifiConfigurationPersistence {

    private final WifiConfigurationRepository repository;

    WifiPersistenceService(WifiConfigurationRepository repository) {
        Objects.requireNonNull(repository, "repository must not be null");
        this.repository = repository;
    }

    @Async
    @Override
    public void persist(WifiConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        repository.save(configuration);
    }
}
