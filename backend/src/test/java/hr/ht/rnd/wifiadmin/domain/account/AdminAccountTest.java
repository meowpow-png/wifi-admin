package hr.ht.rnd.wifiadmin.domain.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminAccountTest {

    @Nested
    @DisplayName("constructor")
    class ConstructorTests {

        @Test
        @DisplayName("Creates account when required fields are valid")
        void should_CreateAccount_when_RequiredFieldsAreValid() {
            var password = TestAccounts.password();
            var username = TestAccounts.admin().username();
            var account = new AdminAccount(username, password);

            assertThat(account.username()).isEqualTo(username);
            assertThat(account.password()).isEqualTo(password);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when username is null")
        void should_ThrowNullPointerException_when_UsernameIsNull() {
            var password = TestAccounts.password();

            assertThatThrownBy(() -> new AdminAccount(null, password))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when username is blank")
        void should_ThrowIllegalArgumentException_when_UsernameIsBlank() {
            var password = TestAccounts.password();

            assertThatThrownBy(() -> new AdminAccount(" ", password))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when password is null")
        void should_ThrowNullPointerException_when_PasswordIsNull() {
            var username = TestAccounts.admin().username();

            assertThatThrownBy(() -> new AdminAccount(username, null))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
