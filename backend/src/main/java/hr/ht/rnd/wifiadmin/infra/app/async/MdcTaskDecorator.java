package hr.ht.rnd.wifiadmin.infra.app.async;

import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

import org.slf4j.MDC;

import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * Propagates the current MDC logging context
 * to asynchronously executed tasks.
 * <p>
 * The logging context is captured when the task
 * is submitted and restored for the duration of
 * its execution. The restored context is
 * cleared after execution completes.
 */
@Component
final class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable must not be null");

        var context = MDC.getCopyOfContextMap();

        return () -> {
            var previous = MDC.getCopyOfContextMap();
            try {
                restore(context);
                runnable.run();
            }
            finally {
                restore(previous);
            }
        };
    }

    private static void restore(@Nullable Map<String, String> context) {
        if (context != null) {
            MDC.setContextMap(context);
        }
        else {
            MDC.clear();
        }
    }
}