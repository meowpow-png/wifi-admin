package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.outbound.AdministratorProvider;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Spring-backed {@link AdministratorProvider} implementation.
 * <p>
 * <strong>Implementation Note:</strong>
 * Anonymous authentication is disabled, so the
 * absence of an {@code Authentication} indicates
 * that no administrator is authenticated.
 */
@Component
final class SpringAdministratorProvider implements AdministratorProvider {

    @Override
    public Optional<String> username() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }
        return Optional.of(authentication.getName());
    }
}
