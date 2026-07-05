package hr.ht.rnd.wifiadmin.application.outbound;

import java.util.Optional;

/**
 * Provides the currently
 * authenticated administrator.
 */
public interface AdministratorProvider {

    /**
     * Returns the username of the currently
     * authenticated administrator.
     *
     * @return the administrator username
     */
    Optional<String> username();
}
