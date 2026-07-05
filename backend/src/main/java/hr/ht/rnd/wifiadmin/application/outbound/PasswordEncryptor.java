package hr.ht.rnd.wifiadmin.application.outbound;

import hr.ht.rnd.wifiadmin.domain.WifiPassword;

/**
 * Encrypts and decrypts Wi-Fi passwords.
 */
public interface PasswordEncryptor {

    /**
     * Encrypts a Wi-Fi password.
     *
     * @param password the plaintext password
     *
     * @return the encrypted password
     *
     * @throws NullPointerException if {@code password} is {@code null}
     * @throws IllegalStateException if the password cannot be encrypted
     */
    String encrypt(WifiPassword password);

    /**
     * Decrypts an encrypted Wi-Fi password.
     *
     * @param password the encrypted password
     *
     * @return the plaintext password
     *
     * @throws NullPointerException if {@code password} is {@code null}
     * @throws IllegalArgumentException if {@code password} cannot be decrypted
     */
    WifiPassword decrypt(String password);
}
