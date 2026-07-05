package hr.ht.rnd.wifiadmin.application.inbound;

import hr.ht.rnd.wifiadmin.application.exception.AccountNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;

/**
 * Changes the password of the currently
 * authenticated administrator.
 */
public interface ChangeAdminPassword {

    /**
     * Changes the administrator password.
     * <p>
     * If the new password matches the current
     * password, this operation has no effect.
     *
     * @param currentPassword the current account password
     * @param newPassword the new account password
     *
     * @throws NullPointerException if any argument is {@code null}
     * @throws IllegalArgumentException if any argument is blank
     * @throws AuthenticationException if authentication fails
     * @throws AccountNotFoundException if the administrator account cannot be found
     * @throws PersistenceException if the password cannot be updated
     */
    void changePassword(String currentPassword, String newPassword);
}
