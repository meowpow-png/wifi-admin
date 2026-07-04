package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Repository
class JpaWifiConfigurationRepository implements WifiConfigurationRepository {

    private final WifiConfigurationJpaRepository repository;

    JpaWifiConfigurationRepository(WifiConfigurationJpaRepository repository) {
        Objects.requireNonNull(repository, "repository must not be null");
        this.repository = repository;
    }

    @Override
    public Optional<WifiConfiguration> findByCpeId(String cpeId) {
        Objects.requireNonNull(cpeId, "cpeId must not be null");
        return findEntityById(cpeId).map(WifiConfigurationEntityMapper::toDomain);
    }

    @Override
    public void save(WifiConfiguration configuration, @Nullable LocalDate lastSynchronized) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        saveEntity(WifiConfigurationEntityMapper.toEntity(
                configuration,
                lastSynchronized
        ));
    }

    @Override
    @Transactional
    public void deleteOlderThan(LocalDate lastSynchronized) {
        Objects.requireNonNull(lastSynchronized, "lastSynchronized must not be null");
        try {
            repository.deleteOlderThan(lastSynchronized);
        }
        catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    private Optional<WifiConfigurationEntity> findEntityById(String cpeId) {
        try {
            return repository.findById(cpeId);
        }
        catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    private void saveEntity(WifiConfigurationEntity entity) {
        try {
            repository.save(entity);
        }
        catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
