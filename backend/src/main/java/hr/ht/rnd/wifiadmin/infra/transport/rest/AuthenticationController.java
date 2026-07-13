package hr.ht.rnd.wifiadmin.infra.transport.rest;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.application.inbound.AuthenticateAdmin;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.LoginRequest;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.LoginResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * REST controller exposing authentication endpoints.
 */
@RestController
public final class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticateAdmin authentication;

    AuthenticationController(AuthenticateAdmin authentication) {
        Objects.requireNonNull(authentication, "authentication must not be null");
        this.authentication = authentication;
    }

    /**
     * Authenticates an administrator account.
     *
     * @param request the authentication request
     *
     * @return status {@code 200 (OK)}
     * @throws NullPointerException if {@code request} is {@code null}
     * @throws AuthenticationException if authentication fails
     */
    @PostMapping("/auth/login")
    LoginResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        var token = authentication.authenticate(
                request.username(),
                request.password()
        );
        log.debug("Authentication succeeded for {} {} from {} ({})",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                httpRequest.getRemoteAddr(),
                httpRequest.getHeader(HttpHeaders.USER_AGENT)
        );
        return new LoginResponse(token.value());
    }
}
