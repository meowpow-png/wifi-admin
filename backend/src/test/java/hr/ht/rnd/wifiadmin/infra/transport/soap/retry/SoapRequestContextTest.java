package hr.ht.rnd.wifiadmin.infra.transport.soap.retry;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoapRequestContextTest {

    private static final String OPERATION = "getConfiguration";

    @Nested
    @DisplayName("constructor")
    class ConstructorTests {

        @Test
        @DisplayName("Creates request context when values are valid")
        void should_CreateRequestContext_when_ValuesAreValid() {
            var context = new SoapRequestContext(OPERATION, TestWifiConfigurations.CPE_ID);

            assertThat(context.operation()).isEqualTo(OPERATION);
            assertThat(context.cpeId()).isEqualTo(TestWifiConfigurations.CPE_ID);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when operation is null")
        void should_ThrowNullPointerException_when_OperationIsNull() {
            assertThatThrownBy(() -> new SoapRequestContext(null, TestWifiConfigurations.CPE_ID))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when CPE ID is null")
        void should_ThrowNullPointerException_when_CpeIdIsNull() {
            assertThatThrownBy(() -> new SoapRequestContext(OPERATION, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when operation is blank")
        void should_ThrowIllegalArgumentException_when_OperationIsBlank() {
            assertThatThrownBy(() -> new SoapRequestContext(" ", TestWifiConfigurations.CPE_ID))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when CPE ID is blank")
        void should_ThrowIllegalArgumentException_when_CpeIdIsBlank() {
            assertThatThrownBy(() -> new SoapRequestContext(OPERATION, " "))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
