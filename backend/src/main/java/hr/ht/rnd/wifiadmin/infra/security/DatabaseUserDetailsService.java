package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.outbound.AdminAccountRepository;
import hr.ht.rnd.wifiadmin.domain.account.AdminAccount;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Loads administrator accounts for
 * authentication by Spring Security.
 */
@Service
final class DatabaseUserDetailsService implements UserDetailsService {

    private static final String ADMIN_ROLE = "ADMIN";

    private final AdminAccountRepository repository;

    DatabaseUserDetailsService(AdminAccountRepository repository) {
        Objects.requireNonNull(repository, "repository must not be null");
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Objects.requireNonNull(username, "username must not be null");

        return repository.findByUsername(username)
                .map(this::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private UserDetails toUserDetails(AdminAccount account) {
        return User.builder()
                .username(account.username())
                .password(account.password().value())
                .roles(ADMIN_ROLE)
                .build();
    }
}
