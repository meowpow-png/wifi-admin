package hr.ht.rnd.wifiadmin.application.outbound;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;

/**
 * Verifies access tokens.
 */
public interface AccessTokenVerifier {

    /**
     * Verifies the specified access token.
     *
     * @param token the access token
     *
     * @return the username contained in the access token
     * @throws NullPointerException if {@code token} is {@code null}
     * @throws IllegalArgumentException if {@code token} is blank
     * @throws AuthenticationException if the access token is invalid
     */
    String verify(String token);
}
