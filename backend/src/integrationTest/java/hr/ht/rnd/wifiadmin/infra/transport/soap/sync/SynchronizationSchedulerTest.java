package hr.ht.rnd.wifiadmin.infra.transport.soap.sync;

import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

@WiringIntegrationTest
class SynchronizationSchedulerTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(SynchronizationScheduler.class, SchedulerTestConfiguration.class);

    @Nested
    @DisplayName("synchronize")
    class SynchronizeMethodTests {

        @Test
        @DisplayName("Registers scheduled synchronization method")
        void should_RegisterScheduledSynchronizationMethod_when_SchedulingIsEnabled() {
            TestApplicationContextRunner.from(runner)
                    .withSchedulingEnabled()
                    .hasScheduledMethod(SynchronizationScheduler.class, "synchronize")
                    .usesCronFrom("platformSyncCronExpression")
                    .doesNotFail();
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class SchedulerTestConfiguration {

        @Bean
        PlatformSynchronizer platformSynchronizer() {
            return mock(PlatformSynchronizer.class);
        }

        @Bean
        SynchronizationSchedule synchronizationSchedule() {
            return mock(SynchronizationSchedule.class);
        }

        @Bean
        String platformSyncCronExpression() {
            return "0 0 2 * * *";
        }
    }
}
