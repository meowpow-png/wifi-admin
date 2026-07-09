package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AesPasswordEncryptorTest {

    @Nested
    @DisplayName("constructor")
    class ConstructorTests {

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when key is null")
        void should_ThrowNullPointerException_when_KeyIsNull() {
            assertThatThrownBy(() -> new AesPasswordEncryptor(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("encrypt")
    class EncryptMethodTests {

        @Test
        @DisplayName("Returns encrypted password when password is valid")
        void should_ReturnEncryptedPassword_when_PasswordIsValid() {
            var password = TestWifiConfigurations.PASSWORD;
            var encryptor = TestPasswordEncryptor.aes();

            var encrypted = encryptor.encrypt(password);

            assertThat(encrypted)
                    .startsWith(AesPasswordEncryptor.ciphertextPrefix())
                    .doesNotContain(password.value());
        }

        @Test
        @DisplayName("Returns unique encrypted passwords when password is encrypted repeatedly")
        void should_ReturnUniqueEncryptedPasswords_when_PasswordIsEncryptedRepeatedly() {
            var password = TestWifiConfigurations.PASSWORD;
            var encryptor = TestPasswordEncryptor.aes();

            var firstEncrypted = encryptor.encrypt(password);
            var secondEncrypted = encryptor.encrypt(password);

            assertThat(firstEncrypted).isNotEqualTo(secondEncrypted);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when password is null")
        void should_ThrowNullPointerException_when_PasswordIsNull() {
            assertThatThrownBy(() -> TestPasswordEncryptor.aes().encrypt(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("decrypt")
    class DecryptMethodTests {

        @Test
        @DisplayName("Returns plaintext password when encrypted password is valid")
        void should_ReturnPlaintextPassword_when_EncryptedPasswordIsValid() {
            var password = TestWifiConfigurations.PASSWORD;
            var encryptor = TestPasswordEncryptor.aes();

            var encrypted = encryptor.encrypt(password);
            var decrypted = encryptor.decrypt(encrypted);

            assertThat(decrypted).isEqualTo(password);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when password is null")
        void should_ThrowNullPointerException_when_PasswordIsNull() {
            assertThatThrownBy(() -> TestPasswordEncryptor.aes().decrypt(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when ciphertext format is unsupported")
        void should_ThrowIllegalArgumentException_when_CiphertextFormatIsUnsupported() {
            assertThatThrownBy(() -> TestPasswordEncryptor.aes().decrypt("password"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when ciphertext is malformed")
        void should_ThrowIllegalArgumentException_when_CiphertextIsMalformed() {
            var prefix = AesPasswordEncryptor.ciphertextPrefix();
            assertThatThrownBy(() -> TestPasswordEncryptor.aes().decrypt(prefix + "invalid"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when ciphertext cannot be decrypted")
        void should_ThrowIllegalArgumentException_when_CiphertextCannotBeDecrypted() {
            var password = TestWifiConfigurations.PASSWORD;
            var encryptor = TestPasswordEncryptor.aes();

            var encrypted = encryptor.encrypt(password);
            var tampered = encrypted.substring(0, encrypted.length() - 1) + "A";

            assertThatThrownBy(() -> encryptor.decrypt(tampered))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
