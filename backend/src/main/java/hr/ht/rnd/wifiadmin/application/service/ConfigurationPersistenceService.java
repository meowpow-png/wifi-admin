package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationPersistence;
import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
class ConfigurationPersistenceService implements WifiConfigurationPersistence {

    private final WifiConfigurationRepository repository;

    ConfigurationPersistenceService(WifiConfigurationRepository repository) {
        Objects.requireNonNull(repository, "repository must not be null");
        this.repository = repository;
    }

    @Override
    public void persist(WifiConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        repository.save(configuration);
    }
}
