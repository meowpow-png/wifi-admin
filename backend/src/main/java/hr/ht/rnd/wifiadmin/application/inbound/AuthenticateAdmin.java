package hr.ht.rnd.wifiadmin.application.inbound;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;

/**
 * Authenticates administrator accounts.
 */
public interface AuthenticateAdmin {

    /**
     * Authenticates the administrator
     * with the specified credentials.
     *
     * @param username the account username
     * @param password the account password
     *
     * @return the authentication token
     * @throws NullPointerException if {@code username} or {@code password} is {@code null}
     * @throws AuthenticationException if authentication fails
     */
    AuthenticationToken authenticate(String username, String password);
}
