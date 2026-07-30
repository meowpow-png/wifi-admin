package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.domain.account.AccountPassword;
import hr.ht.rnd.wifiadmin.domain.account.AdminAccount;

import java.util.Objects;

/**
 * Maps administrator accounts between
 * domain and persistence models.
 */
final class AdminAccountEntityMapper {

    private AdminAccountEntityMapper() {}

    /**
     * Converts an administrator account
     * into a persistence entity.
     *
     * @param account the administrator account to convert
     *
     * @throws NullPointerException if {@code account} is {@code null}
     */
    static AdminAccountEntity toEntity(AdminAccount account) {
        Objects.requireNonNull(account, "account must not be null");

        return new AdminAccountEntity(
                account.username(),
                account.password().value()
        );
    }

    /**
     * Reconstructs an administrator account
     * from a persistence entity.
     *
     * @param entity the persistence entity to convert
     *
     * @throws NullPointerException if {@code entity} is {@code null}
     * @throws IllegalArgumentException if {@code entity} contains invalid data
     */
    static AdminAccount toDomain(AdminAccountEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");

        return new AdminAccount(
                entity.getUsername(),
                new AccountPassword(entity.getPassword())
        );
    }
}
