package hr.ht.rnd.wifiadmin.infra.transport.rest.dto;

import java.util.Objects;

/**
 * Authentication response.
 *
 * @param token the JWT access token
 */
public record LoginResponse(String token) {

    /**
     * Creates a new authentication response.
     *
     * @param token the JWT access token
     *
     * @throws NullPointerException if {@code token} is {@code null}
     * @throws IllegalArgumentException if {@code token} is blank
     */
    public LoginResponse {
        Objects.requireNonNull(token, "token must not be null");
        if (token.isBlank()) {
            throw new IllegalArgumentException("token must not be blank");
        }
    }
}
