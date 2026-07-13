package hr.ht.rnd.wifiadmin.domain.wifi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WifiPasswordTest {

    @Nested
    @DisplayName("constructor")
    class ConstructorTests {

        @Test
        @DisplayName("Creates password when value is valid")
        void should_CreatePassword_when_ValueIsValid() {
            var value = TestWifiConfigurations.PASSWORD.value();
            var password = new WifiPassword(value);

            assertThat(password.value()).isEqualTo(value);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when value is null")
        void should_ThrowNullPointerException_when_ValueIsNull() {
            assertThatThrownBy(() -> new WifiPassword(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when value is blank")
        void should_ThrowIllegalArgumentException_when_ValueIsBlank() {
            assertThatThrownBy(() -> new WifiPassword(" "))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringMethodTests {

        @Test
        @DisplayName("Redacts password value")
        void should_RedactPasswordValue_when_Called() {
            var password = TestWifiConfigurations.PASSWORD;

            assertThat(password.toString())
                    .contains("*")
                    .doesNotContain(password.value());
        }
    }
}
