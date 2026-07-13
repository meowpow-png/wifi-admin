package hr.ht.rnd.wifiadmin.application.inbound;

import java.util.Objects;

/**
 * Authentication token issued for
 * a successfully authenticated account.
 *
 * @param value the authentication token
 */
public record AuthenticationToken(String value) {

    /**
     * Creates a new authentication token.
     *
     * @param value the authentication token
     *
     * @throws NullPointerException if {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code value} is blank
     */
    public AuthenticationToken {
        Objects.requireNonNull(value, "value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
    }
}
