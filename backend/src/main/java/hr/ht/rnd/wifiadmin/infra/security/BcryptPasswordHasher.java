package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.outbound.PasswordHasher;
import hr.ht.rnd.wifiadmin.domain.account.AccountPassword;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
class BcryptPasswordHasher implements PasswordHasher {

    private final PasswordEncoder encoder;

    BcryptPasswordHasher(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public AccountPassword hash(String password) {
        Objects.requireNonNull(password, "password must not be null");
        return new AccountPassword(encoder.encode(password));
    }

    @Override
    public boolean matches(String password, AccountPassword hash) {
        Objects.requireNonNull(password, "password must not be null");
        Objects.requireNonNull(hash, "hash must not be null");

        return encoder.matches(password, hash.value());
    }
}
