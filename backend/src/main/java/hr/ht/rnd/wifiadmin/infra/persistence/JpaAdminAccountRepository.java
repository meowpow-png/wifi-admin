package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.application.outbound.AdminAccountRepository;
import hr.ht.rnd.wifiadmin.domain.account.AdminAccount;

import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
class JpaAdminAccountRepository implements AdminAccountRepository {

    private final AdminAccountJpaRepository repository;

    JpaAdminAccountRepository(AdminAccountJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AdminAccount> findByUsername(String username) {
        Objects.requireNonNull(username, "username must not be null");
        return findEntityById(username).map(AdminAccountEntityMapper::toDomain);
    }

    @Override
    public void save(AdminAccount account) {
        Objects.requireNonNull(account, "account must not be null");
        saveEntity(AdminAccountEntityMapper.toEntity(account));
    }

    private Optional<AdminAccountEntity> findEntityById(String username) {
        try {
            return repository.findById(username);
        }
        catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    private void saveEntity(AdminAccountEntity entity) {
        try {
            repository.save(entity);
        }
        catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
