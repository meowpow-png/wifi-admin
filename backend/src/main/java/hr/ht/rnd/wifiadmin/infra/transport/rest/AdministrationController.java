package hr.ht.rnd.wifiadmin.infra.transport.rest;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.application.inbound.ChangeAdminPassword;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.ChangePasswordRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Objects;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.Event;
import static hr.ht.rnd.wifiadmin.common.StructuredLog.debug;

/**
 * REST controller exposing administration endpoints.
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
public final class AdministrationController {

    private static final Logger log = LoggerFactory.getLogger(AdministrationController.class);

    private final ChangeAdminPassword password;

    AdministrationController(ChangeAdminPassword password) {
        Objects.requireNonNull(password, "password must not be null");
        this.password = password;
    }

    /**
     * Changes the administrator password.
     *
     * @param request the password change request
     *
     * @throws NullPointerException if {@code request} is {@code null}
     * @throws AuthenticationException if authentication fails
     */
    @PutMapping("/admin/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        debug(log).withEvent(Event.ADMINISTRATOR_PASSWORD_CHANGE_ATTEMPT)
                .withRequest(httpRequest)
                .log();

        password.changePassword(
                request.currentPassword(),
                request.newPassword()
        );
        debug(log).withEvent(Event.ADMINISTRATOR_PASSWORD_CHANGED)
                .withRequest(httpRequest)
                .log();
    }
}
