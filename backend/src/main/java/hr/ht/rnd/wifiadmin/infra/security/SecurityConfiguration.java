package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.outbound.PasswordEncryptor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfiguration {

    @Bean
    PasswordEncryptor passwordEncryptor(SecurityProperties properties) {
        var key = aesKey(properties.aesKey());
        return new AesPasswordEncryptor(key);
    }

    private static SecretKey aesKey(String encodedKey) {
        byte[] key;
        try {
            key = Base64.getDecoder().decode(encodedKey);
        }
        catch (IllegalArgumentException e) {
            var message = "AES key must be a valid Base64-encoded value";
            throw new IllegalStateException(message, e);
        }
        if (key.length != 32) {
            throw new IllegalStateException("AES key must be 256 bits");
        }
        return new SecretKeySpec(key, "AES");
    }
}
