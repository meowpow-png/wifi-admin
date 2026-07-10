package hr.ht.rnd.wifiadmin.infra.app.async;

import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@WiringIntegrationTest
class AppAsyncConfigurerTest {

    private static final Executor ASYNC_EXECUTOR = Runnable::run;

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(AppAsyncConfigurer.class, AppAsyncTestConfiguration.class);

    @Nested
    @DisplayName("getAsyncExecutor")
    class GetAsyncExecutorMethodTests {

        @Test
        @DisplayName("Uses asyncExecutor bean")
        void should_UseAsyncExecutorBean_when_ConfigurerIsCreated() {
            Consumer<AppAsyncConfigurer> assertion = configurer ->
                    assertThat(configurer.getAsyncExecutor()).isSameAs(ASYNC_EXECUTOR);

            TestApplicationContextRunner.from(runner)
                    .withBean(AppAsyncConfigurer.class, assertion)
                    .doesNotFail();
        }
    }

    @Nested
    @DisplayName("getAsyncUncaughtExceptionHandler")
    class GetAsyncUncaughtExceptionHandlerMethodTests {

        @Test
        @DisplayName("Creates async exception handler")
        void should_CreateAsyncExceptionHandler_when_ConfigurerIsCreated() {
            Consumer<AppAsyncConfigurer> assertion = configurer ->
                    assertThat(configurer.getAsyncUncaughtExceptionHandler())
                            .isInstanceOf(AsyncExceptionHandler.class);

            TestApplicationContextRunner.from(runner)
                    .withBean(AppAsyncConfigurer.class, assertion)
                    .doesNotFail();
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class AppAsyncTestConfiguration {

        @Bean("asyncExecutor")
        Executor asyncExecutor() {
            return ASYNC_EXECUTOR;
        }
    }
}
