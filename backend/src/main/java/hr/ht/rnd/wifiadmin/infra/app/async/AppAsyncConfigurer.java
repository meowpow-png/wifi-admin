package hr.ht.rnd.wifiadmin.infra.app.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * Configures the application's asynchronous
 * execution infrastructure.
 * <p>
 * <strong>Implementation Note:</strong>
 * The executor is injected by qualifier rather
 * than type to allow test configurations to provide
 * an alternative implementation under the same
 * bean name, enabling asynchronous execution
 * to be disabled during integration testing.
 */
@Component
public class AppAsyncConfigurer implements AsyncConfigurer {

    private final Executor executor;

    AppAsyncConfigurer(@Qualifier("asyncExecutor") Executor executor) {
        this.executor = executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
}
