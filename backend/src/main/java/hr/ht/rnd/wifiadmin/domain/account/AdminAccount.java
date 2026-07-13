package hr.ht.rnd.wifiadmin.domain.account;

import java.util.Objects;

/**
 * Administrator account authorized
 * to access the application.
 *
 * @param username the account username
 * @param password the account password
 */
public record AdminAccount(String username, AccountPassword password) {

    /**
     * Creates a new administrator account.
     *
     * @param username the account username
     * @param password the account password
     *
     * @throws NullPointerException if {@code username} or {@code password} is {@code null}
     * @throws IllegalArgumentException if {@code username} is blank
     */
    public AdminAccount {
        Objects.requireNonNull(username, "username must not be null");
        if (username.isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }
        Objects.requireNonNull(password, "password must not be null");
    }
}
