package hr.ht.rnd.wifiadmin.infra.app.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.Event;
import static hr.ht.rnd.wifiadmin.common.StructuredLog.error;

/**
 * Handles uncaught exceptions
 * thrown by asynchronous methods.
 */
public final class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(
            Throwable exception,
            Method method,
            @Nullable Object... parameters
    ) {
        error(log).withEvent(Event.UNHANDLED_ASYNC_EXCEPTION)
                .withField("class", method.getDeclaringClass().getSimpleName())
                .withField("method", method.getName())
                .withCause(exception)
                .log();
    }
}
