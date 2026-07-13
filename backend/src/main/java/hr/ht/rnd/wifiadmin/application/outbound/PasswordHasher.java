package hr.ht.rnd.wifiadmin.application.outbound;

import hr.ht.rnd.wifiadmin.domain.account.AccountPassword;

/**
 * Hashes and verifies account passwords.
 */
public interface PasswordHasher {

    /**
     * Hashes the specified password.
     *
     * @param password the account password
     *
     * @return the hashed password
     * @throws NullPointerException if {@code password} is {@code null}
     * @throws IllegalArgumentException if {@code password} is blank
     */
    AccountPassword hash(String password);

    /**
     * Verifies whether the specified password
     * matches the specified password hash.
     *
     * @param password the account password
     * @param hash the password hash
     *
     * @return {@code true} if the password matches the hash, otherwise {@code false}
     * @throws NullPointerException if {@code password} or {@code hash} is {@code null}
     * @throws IllegalArgumentException if {@code password} is blank
     */
    boolean matches(String password, AccountPassword hash);
}
