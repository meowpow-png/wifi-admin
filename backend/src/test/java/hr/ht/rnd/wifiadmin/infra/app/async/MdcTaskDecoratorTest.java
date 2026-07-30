package hr.ht.rnd.wifiadmin.infra.app.async;

import org.slf4j.MDC;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MdcTaskDecoratorTest {

    private static final String TRACE_ID = "trace_id";
    private static final String SUBMITTED_TRACE_ID = "submitted-trace-id";
    private static final String EXECUTOR_TRACE_ID = "executor-trace-id";

    @AfterEach
    void cleanupMdcTaskDecoratorTest() {
        MDC.clear();
    }

    @Nested
    @DisplayName("decorate")
    class DecorateMethodTests {

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when runnable is null")
        void should_ThrowNullPointerException_when_RunnableIsNull() {
            assertThatThrownBy(() -> new MdcTaskDecorator().decorate(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Restores submitted context while decorated task runs")
        void should_RestoreSubmittedContext_when_DecoratedTaskRuns() {
            var decorator = new MdcTaskDecorator();
            var observedTraceId = new AtomicReference<String>();

            MDC.put(TRACE_ID, SUBMITTED_TRACE_ID);
            var task = decorator.decorate(() ->
                    observedTraceId.set(MDC.get(TRACE_ID))
            );
            MDC.put(TRACE_ID, EXECUTOR_TRACE_ID);
            task.run();

            assertThat(observedTraceId).hasValue(SUBMITTED_TRACE_ID);
            assertThat(MDC.get(TRACE_ID)).isEqualTo(EXECUTOR_TRACE_ID);
        }

        @Test
        @DisplayName("Clears context after decorated task runs when previous context is empty")
        void should_ClearContextAfterDecoratedTaskRuns_when_PreviousContextIsEmpty() {
            var decorator = new MdcTaskDecorator();
            var observedTraceId = new AtomicReference<String>();

            MDC.put(TRACE_ID, SUBMITTED_TRACE_ID);
            var task = decorator.decorate(() ->
                    observedTraceId.set(MDC.get(TRACE_ID))
            );
            MDC.clear();
            task.run();

            assertThat(observedTraceId).hasValue(SUBMITTED_TRACE_ID);
            assertThat(MDC.getCopyOfContextMap()).isNull();
        }

        @Test
        @DisplayName("Restores previous context when decorated task throws exception")
        void should_RestorePreviousContext_when_DecoratedTaskThrowsException() {
            var decorator = new MdcTaskDecorator();
            var exception = new RuntimeException();

            MDC.put(TRACE_ID, SUBMITTED_TRACE_ID);
            var task = decorator.decorate(() -> {
                throw exception;
            });
            MDC.put(TRACE_ID, EXECUTOR_TRACE_ID);

            assertThatThrownBy(task::run).isSameAs(exception);
            assertThat(MDC.get(TRACE_ID)).isEqualTo(EXECUTOR_TRACE_ID);
        }
    }
}
