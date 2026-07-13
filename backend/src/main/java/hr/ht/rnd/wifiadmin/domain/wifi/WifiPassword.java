package hr.ht.rnd.wifiadmin.domain.wifi;

import java.util.Objects;

/**
 * Represents a Wi-Fi password.
 * <p>
 * <strong>Implementation Note:</strong>
 * The password is redacted from string representations
 * to prevent accidental disclosure through logging.
 *
 * @param value the plaintext password
 */
public record WifiPassword(String value) {

    /**
     * Creates a Wi-Fi password.
     *
     * @param value the plaintext password
     *
     * @throws NullPointerException if {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code value} is blank
     */
    public WifiPassword {
        Objects.requireNonNull(value, "Password value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Password value must not be blank");
        }
    }

    @Override
    public String toString() {
        return "********";
    }
}
