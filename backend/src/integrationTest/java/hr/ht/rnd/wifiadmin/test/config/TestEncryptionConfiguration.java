package hr.ht.rnd.wifiadmin.test.config;

import hr.ht.rnd.wifiadmin.application.outbound.PasswordEncryptor;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiPassword;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import org.jspecify.annotations.NullMarked;

@TestConfiguration(proxyBeanMethods = false)
public class TestEncryptionConfiguration {

    @Bean
    @Primary
    PasswordEncryptor testPasswordEncryptor() {
        return new NoOpPasswordEncryptor();
    }

    @NullMarked
    private static class NoOpPasswordEncryptor implements PasswordEncryptor {

        @Override
        public String encrypt(WifiPassword password) {
            return password.value();
        }

        @Override
        public WifiPassword decrypt(String password) {
            return new WifiPassword(password);
        }
    }
}
