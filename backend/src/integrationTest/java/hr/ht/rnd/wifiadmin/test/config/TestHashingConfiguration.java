package hr.ht.rnd.wifiadmin.test.config;

import hr.ht.rnd.wifiadmin.application.outbound.PasswordHasher;
import hr.ht.rnd.wifiadmin.domain.account.AccountPassword;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@TestConfiguration(proxyBeanMethods = false)
public class TestHashingConfiguration {

    @Bean
    @Primary
    PasswordHasher testPasswordHasher() {
        return new NoOpPasswordHasher();
    }

    @Bean
    @Primary
    PasswordEncoder testPasswordEncoder() {
        return new NoOpPasswordEncoder();
    }

    @NullMarked
    private static class NoOpPasswordHasher implements PasswordHasher {

        @Override
        public AccountPassword hash(String password) {
            return new AccountPassword(password);
        }

        @Override
        public boolean matches(String password, AccountPassword hash) {
            return password.equals(hash.value());
        }
    }

    private static class NoOpPasswordEncoder implements PasswordEncoder {

        @Nullable
        @Override
        public String encode(@Nullable CharSequence rawPassword) {
            return rawPassword != null ? rawPassword.toString() : null;
        }

        @Override
        public boolean matches(@Nullable CharSequence rawPassword, @Nullable String encodedPassword) {
            if (rawPassword == null) {
                return encodedPassword == null;
            }
            return rawPassword.equals(encodedPassword);
        }
    }
}
