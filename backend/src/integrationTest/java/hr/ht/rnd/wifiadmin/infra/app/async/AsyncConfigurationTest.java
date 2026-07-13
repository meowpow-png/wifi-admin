package hr.ht.rnd.wifiadmin.infra.app.async;

import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;

@WiringIntegrationTest
class AsyncConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(AsyncConfiguration.class);

    @Nested
    @DisplayName("beans")
    class BeansTests {

        @Test
        @DisplayName("Registers async infrastructure beans")
        void should_RegisterAsyncInfrastructureBeans_when_ContextStarts() {
            TestApplicationContextRunner.from(runner)
                    .hasBean("asyncExecutor")
                    .doesNotFail();
        }
    }

    @Nested
    @DisplayName("asyncExecutor")
    class AsyncExecutorMethodTests {

        @Test
        @DisplayName("Creates executor bean")
        void should_CreateExecutorBean_when_ContextStarts() {
            TestApplicationContextRunner.from(runner)
                    .hasBean(Executor.class)
                    .doesNotFail();
        }
    }
}
