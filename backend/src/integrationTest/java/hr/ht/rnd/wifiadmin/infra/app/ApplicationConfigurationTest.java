package hr.ht.rnd.wifiadmin.infra.app;

import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneId;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@WiringIntegrationTest
class ApplicationConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(ApplicationConfiguration.class);

    @Nested
    @DisplayName("beans")
    class BeansTests {

        @Test
        @DisplayName("Registers application infrastructure beans")
        void should_RegisterApplicationInfrastructureBeans_when_ContextStarts() {
            TestApplicationContextRunner.from(runner)
                    .hasBean(Clock.class)
                    .hasBean(LoggingSystem.class)
                    .doesNotFail();
        }
    }

    @Nested
    @DisplayName("clock")
    class ClockMethodTests {

        @Test
        @DisplayName("Uses system default zone")
        void should_UseSystemDefaultZone_when_ClockBeanIsCreated() {
            Consumer<Clock> assertion = clock ->
                    assertThat(clock.getZone()).isEqualTo(ZoneId.systemDefault());

            TestApplicationContextRunner.from(runner)
                    .withBean(Clock.class, assertion)
                    .doesNotFail();
        }
    }
}
