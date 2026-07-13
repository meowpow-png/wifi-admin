package hr.ht.rnd.wifiadmin.application.outbound;

import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.domain.account.AdminAccount;

import java.util.Optional;

/**
 * Repository for administrator accounts.
 */
public interface AdminAccountRepository {

    /**
     * Retrieves the administrator account with the specified username.
     *
     * @param username the account username
     *
     * @return the administrator account, if found
     * @throws NullPointerException if {@code username} is {@code null}
     * @throws PersistenceException if the administrator account cannot be retrieved
     */
    Optional<AdminAccount> findByUsername(String username);

    /**
     * Saves the specified administrator account.
     *
     * @param account the administrator account
     *
     * @throws NullPointerException if {@code account} is {@code null}
     * @throws PersistenceException if the administrator account cannot be saved
     */
    void save(AdminAccount account);
}
