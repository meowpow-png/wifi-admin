package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.outbound.PasswordEncryptor;

import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

public class TestPasswordEncryptor {

    public static final String CIPHERTEXT_PREFIX = "enc:v1:";

    private TestPasswordEncryptor() {}

    public static byte[] key() {
        return Base64.getDecoder().decode(
                "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY="
        );
    }

    public static PasswordEncryptor aes() {
        var spec = new SecretKeySpec(key(), "AES");
        return new AesPasswordEncryptor(spec, CIPHERTEXT_PREFIX);
    }
}
