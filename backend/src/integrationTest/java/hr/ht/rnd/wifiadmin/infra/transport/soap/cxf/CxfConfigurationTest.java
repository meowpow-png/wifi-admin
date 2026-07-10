package hr.ht.rnd.wifiadmin.infra.transport.soap.cxf;

import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import org.apache.cxf.Bus;
import org.apache.cxf.logging.FaultListener;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@WiringIntegrationTest
class CxfConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(CxfConfiguration.class, CxfTestConfiguration.class)
            .withPropertyValues("cxf.log-faults=true");

    @Nested
    @DisplayName("beans")
    class BeansTests {

        @Test
        @DisplayName("Registers CXF infrastructure beans when properties are valid")
        void should_RegisterCxfInfrastructureBeans_when_PropertiesAreValid() {
            TestApplicationContextRunner.from(runner)
                    .hasBean(Bus.class)
                    .hasBean(CxfProperties.class)
                    .doesNotFail();
        }
    }

    @Nested
    @DisplayName("properties")
    class PropertiesTests {

        @Test
        @DisplayName("Binds configured CXF properties")
        void should_BindConfiguredCxfProperties_when_ContextStarts() {
            Consumer<CxfProperties> assertion = properties ->
                    assertThat(properties.logFaults()).isTrue();

            TestApplicationContextRunner.from(runner)
                    .withBean(CxfProperties.class, assertion)
                    .doesNotFail();
        }
    }

    @Nested
    @DisplayName("cxfBus")
    class CxfBusMethodTests {

        @Test
        @DisplayName("Registers fault listener on CXF bus")
        void should_RegisterFaultListener_when_CxfBusIsCreated() {
            Consumer<Bus> assertion = bus -> {
                var listener = faultListener(bus);

                assertThat(listener).isNotNull();
                assertThat(faultOccurred(listener)).isTrue();
            };

            TestApplicationContextRunner.from(runner)
                    .withBean(Bus.class, assertion)
                    .doesNotFail();
        }

        @Test
        @DisplayName("Disables fault logging when configured")
        void should_DisableFaultLogging_when_LogFaultsIsFalse() {
            Consumer<Bus> assertion = bus -> {
                var listener = faultListener(bus);
                assertThat(faultOccurred(listener)).isFalse();
            };

            TestApplicationContextRunner.from(runner)
                    .withPropertyValues("cxf.log-faults=false")
                    .withBean(Bus.class, assertion)
                    .doesNotFail();
        }

        private static boolean faultOccurred(FaultListener listener) {
            return listener.faultOccurred(
                    new Exception("fault"),
                    "description",
                    null
            );
        }
    }

    private static FaultListener faultListener(Bus bus) {
        return (FaultListener) bus.getProperty(FaultListener.class.getName());
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class CxfTestConfiguration {

        @Bean
        CxfFaultLoggingPolicy cxfFaultLoggingPolicy() {
            return (exception, description, message) -> true;
        }
    }
}
