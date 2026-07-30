package hr.ht.rnd.wifiadmin.infra.transport.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request to change the administrator password.
 *
 * @param currentPassword the current account password
 * @param newPassword the new account password
 */
public record ChangePasswordRequest(
        @NotBlank(message = "Current password must not be blank")
        String currentPassword,

        @NotBlank(message = "New password must not be blank")
        String newPassword
) {}
