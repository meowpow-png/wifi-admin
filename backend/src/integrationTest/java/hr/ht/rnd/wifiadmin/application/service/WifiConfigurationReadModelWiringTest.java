package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationProjection;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationView;
import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@WiringIntegrationTest
class WifiConfigurationReadModelWiringTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(WifiConfigurationReadModel.class);

    @Nested
    @DisplayName("beans")
    class BeansTests {

        @Test
        @DisplayName("Registers WiFi configuration read model ports")
        void should_RegisterWifiConfigurationReadModelPorts_when_ContextStarts() {
            TestApplicationContextRunner.from(runner)
                    .hasBean(WifiConfigurationView.class)
                    .hasBean(WifiConfigurationProjection.class)
                    .withBean(WifiConfigurationView.class, view ->
                            assertThat(view).isInstanceOf(WifiConfigurationProjection.class)
                    )
                    .doesNotFail();
        }
    }
}
