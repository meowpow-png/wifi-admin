package hr.ht.rnd.wifiadmin.domain.account;

import java.util.Objects;

/**
 * Password associated with an application account.
 *
 * @param value the account password
 */
public record AccountPassword(String value) {

    /**
     * Creates a new account password.
     *
     * @param value the account password
     *
     * @throws NullPointerException if {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code value} is blank
     */
    public AccountPassword {
        Objects.requireNonNull(value, "value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
    }

    @Override
    public String toString() {
        return "********";
    }
}
