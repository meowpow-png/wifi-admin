package hr.ht.rnd.wifiadmin.domain.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountPasswordTest {

    @Nested
    @DisplayName("constructor")
    class ConstructorTests {

        @Test
        @DisplayName("Creates password when value is valid")
        void should_CreatePassword_when_ValueIsValid() {
            var value = TestAccounts.password().value();
            var password = new AccountPassword(value);

            assertThat(password.value()).isEqualTo(value);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when value is null")
        void should_ThrowNullPointerException_when_ValueIsNull() {
            assertThatThrownBy(() -> new AccountPassword(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when value is blank")
        void should_ThrowIllegalArgumentException_when_ValueIsBlank() {
            assertThatThrownBy(() -> new AccountPassword(" "))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringMethodTests {

        @Test
        @DisplayName("Redacts password value")
        void should_RedactPasswordValue_when_Called() {
            var password = TestAccounts.password();

            assertThat(password.toString())
                    .contains("*")
                    .doesNotContain(password.value());
        }
    }
}
