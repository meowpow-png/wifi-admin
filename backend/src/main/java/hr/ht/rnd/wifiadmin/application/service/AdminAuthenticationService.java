package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.application.inbound.AuthenticateAdmin;
import hr.ht.rnd.wifiadmin.application.inbound.AuthenticationToken;
import hr.ht.rnd.wifiadmin.application.outbound.AccessTokenIssuer;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
final class AdminAuthenticationService implements AuthenticateAdmin {

    private final AuthenticationManager authenticationManager;
    private final AccessTokenIssuer tokenIssuer;

    AdminAuthenticationService(
            AuthenticationManager authenticationManager,
            AccessTokenIssuer tokenIssuer
    ) {
        Objects.requireNonNull(authenticationManager, "authenticationManager must not be null");
        Objects.requireNonNull(tokenIssuer, "tokenIssuer must not be null");

        this.authenticationManager = authenticationManager;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public AuthenticationToken authenticate(String username, String password) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(password, "password must not be null");
        try {
            var authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(username, password)
            );
            var token = tokenIssuer.issue(authentication.getName());

            return new AuthenticationToken(token);
        }
        catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException(e);
        }
    }
}
