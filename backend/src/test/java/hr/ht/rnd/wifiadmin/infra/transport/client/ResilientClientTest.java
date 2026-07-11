package hr.ht.rnd.wifiadmin.infra.transport.client;

import org.springframework.core.retry.RetryException;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.core.retry.Retryable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ResilientClientTest {

    private static final String CONTEXT = "context";
    private static final String RESULT = "result";
    private static final String RECOVERED_RESULT = "recovered-result";

    @Mock
    private RetryTemplate retryTemplate;

    private TestResilientClient client;

    @BeforeEach
    void setupResilientClientTest() {
        client = new TestResilientClient(retryTemplate);
    }

    @Test
    @DisplayName("Throws NullPointerException when retry template is null")
    void should_ThrowNullPointerException_when_RetryTemplateIsNull() {
        assertThatThrownBy(() -> new TestResilientClient(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Nested
    @DisplayName("executeWithRetry")
    class ExecuteWithRetryMethodTests {

        @Test
        @DisplayName("Returns result when retry execution succeeds")
        void should_ReturnResult_when_RetryExecutionSucceeds() throws RetryException {
            Retryable<String> action = () -> RESULT;

            Mockito.when(retryTemplate.execute(action)).thenReturn(RESULT);

            var result = client.execute(action, CONTEXT);

            assertThat(result).isEqualTo(RESULT);
            assertThat(client.recoveryCause()).isNull();
            assertThat(client.recoveryContext()).isNull();
            assertThat(client.onRecoveryCalled()).isFalse();
        }

        @Test
        @DisplayName("Returns recovered result when retry execution is exhausted")
        void should_ReturnRecoveredResult_when_RetryExecutionIsExhausted() throws RetryException {
            Retryable<String> action = () -> RESULT;
            var cause = new IllegalStateException("boom");

            Mockito.when(retryTemplate.execute(action)).thenThrow(
                    new RetryException("retries exhausted", cause)
            );
            client.recoverWith(RECOVERED_RESULT);

            var result = client.execute(action, CONTEXT);

            assertThat(result).isEqualTo(RECOVERED_RESULT);
            assertThat(client.recoveryCause()).isSameAs(cause);
            assertThat(client.recoveryContext()).isEqualTo(CONTEXT);
            assertThat(client.onRecoveryCalled()).isTrue();
            assertThat(client.onRecoveryCause()).isSameAs(cause);
            assertThat(client.onRecoveryContext()).isEqualTo(CONTEXT);
        }

        @Test
        @DisplayName("Throws recovery exception when recovery fails")
        void should_ThrowRecoveryException_when_RecoveryFails() throws RetryException {
            Retryable<String> action = () -> RESULT;
            var cause = new IllegalStateException("boom");
            var recoveryFailure = new UnsupportedOperationException("cannot recover");

            Mockito.when(retryTemplate.execute(action)).thenThrow(
                    new RetryException("retries exhausted", cause)
            );
            client.failRecoveryWith(recoveryFailure);

            assertThatThrownBy(() -> client.execute(action, CONTEXT))
                    .isSameAs(recoveryFailure);

            assertThat(client.recoveryCause()).isSameAs(cause);
            assertThat(client.recoveryContext()).isEqualTo(CONTEXT);
            assertThat(client.onRecoveryCalled()).isFalse();
        }
    }
}
