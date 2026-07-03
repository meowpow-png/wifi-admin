package hr.ht.rnd.wifiadmin.infra.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;

/**
 * Handles uncaught exceptions
 * thrown by asynchronous methods.
 */
@NullMarked
public final class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(
            Throwable exception,
            Method method,
            @Nullable Object... parameters
    ) {
        log.error("Unhandled exception in async method '{}.{}'",
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                exception
        );
    }
}
