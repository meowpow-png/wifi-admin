package hr.ht.rnd.wifiadmin.infra.transport.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Authentication request.
 *
 * @param username the account username
 * @param password the account password
 */
public record LoginRequest(
        @NotBlank(message = "Username must not be blank")
        String username,

        @NotBlank(message = "Password must not be blank")
        String password
) {}
