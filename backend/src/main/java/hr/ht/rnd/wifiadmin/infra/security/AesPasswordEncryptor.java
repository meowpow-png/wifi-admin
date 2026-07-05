package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.outbound.PasswordEncryptor;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiPassword;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Encrypts Wi-Fi passwords using AES-GCM.
 * <p>
 * <strong>Implementation Note:</strong>
 * This implementation supports only the {@code enc:v1}
 * ciphertext format. Future ciphertext versions should
 * be introduced by extending the decryptor to parse and
 * dispatch based on the ciphertext version while continuing
 * to encrypt using the latest supported format.
 */
final class AesPasswordEncryptor implements PasswordEncryptor {

    private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    private static final String CIPHERTEXT_PREFIX = "enc:v1:";

    private static final int NONCE_LENGTH = 12;
    private static final int AUTHENTICATION_TAG_LENGTH = 128;

    private final SecretKey key;
    private final SecureRandom secureRandom;

    AesPasswordEncryptor(SecretKey key) {
        Objects.requireNonNull(key, "key must not be null");

        this.key = key;
        this.secureRandom = new SecureRandom();
    }

    @Override
    public String encrypt(WifiPassword password) {
        Objects.requireNonNull(password, "password must not be null");
        try {
            var plaintext = password.value().getBytes(StandardCharsets.UTF_8);
            return encrypt(plaintext, nonce());
        }
        catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to encrypt password", e);
        }
    }

    @Override
    public WifiPassword decrypt(String password) {
        Objects.requireNonNull(password, "password must not be null");
        try {
            var payload = decodeCiphertext(password);

            return new WifiPassword(new String(
                    decrypt(payload),
                    StandardCharsets.UTF_8
            ));
        }
        catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Failed to decrypt password", e);
        }
    }

    private String encrypt(byte[] plaintext, byte[] nonce) throws GeneralSecurityException {
        var cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        var parameters = new GCMParameterSpec(
                AUTHENTICATION_TAG_LENGTH,
                nonce
        );
        cipher.init(Cipher.ENCRYPT_MODE, key, parameters);

        byte[] ciphertext = cipher.doFinal(plaintext);

        var capacity = nonce.length + ciphertext.length;
        byte[] payload = ByteBuffer.allocate(capacity)
                .put(nonce)
                .put(ciphertext)
                .array();

        return encodeCiphertext(payload);
    }

    private byte[] decrypt(byte[] payload) throws GeneralSecurityException {
        var nonce = Arrays.copyOfRange(
                payload,
                0,
                NONCE_LENGTH
        );
        var ciphertext = Arrays.copyOfRange(
                payload,
                NONCE_LENGTH,
                payload.length
        );
        var cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        var parameters = new GCMParameterSpec(
                AUTHENTICATION_TAG_LENGTH,
                nonce
        );
        cipher.init(Cipher.DECRYPT_MODE, key, parameters);

        return cipher.doFinal(ciphertext);
    }

    private byte[] nonce() {
        byte[] nonce = new byte[NONCE_LENGTH];
        secureRandom.nextBytes(nonce);
        return nonce;
    }

    private static String encodeCiphertext(byte[] payload) {
        var encoded = Base64.getEncoder().encodeToString(payload);
        return CIPHERTEXT_PREFIX + encoded;
    }

    private static byte[] decodeCiphertext(String password) {
        if (!password.startsWith(CIPHERTEXT_PREFIX)) {
            var message = "Unsupported ciphertext format, expected %s<base64>";
            throw new IllegalArgumentException(message.formatted(CIPHERTEXT_PREFIX));
        }
        var encoded = password.substring(CIPHERTEXT_PREFIX.length());

        byte[] payload;
        try {
            payload = Base64.getDecoder().decode(encoded);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Malformed ciphertext", e);
        }
        if (payload.length <= NONCE_LENGTH) {
            throw new IllegalArgumentException("Malformed ciphertext");
        }
        return payload;
    }
}
