package hr.ht.rnd.wifiadmin.application.outbound;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;

/**
 * Issues access tokens for
 * authenticated administrator accounts.
 */
public interface AccessTokenIssuer {

    /**
     * Issues an access token for the
     * administrator with the specified username.
     *
     * @param username the account username
     *
     * @return the issued access token
     * @throws NullPointerException if {@code username} is {@code null}
     * @throws IllegalArgumentException if {@code username} is blank
     * @throws AuthenticationException if the access token cannot be issued
     */
    String issue(String username);
}
