package hr.ht.rnd.wifiadmin.infra.app.actuator;

import hr.ht.rnd.wifiadmin.common.LoggerNames;
import hr.ht.rnd.wifiadmin.test.autoconfigure.MockMvcIntegrationTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@MockMvcIntegrationTest
class ActuatorLoggingEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoggingSystem loggingSystem;

    @Nested
    @DisplayName("/actuator/" + ActuatorEndpoints.LOGGING)
    class ApplicationLoggingEndpointTests {

        @Test
        @DisplayName("Updates application logger level")
        void should_UpdateApplicationLoggerLevel_when_LevelIsConfigured() throws Exception {
            configureApplicationLoggingDebug()
                    .andExpect(status().isNoContent());

            assertThat(configuredLevel(LoggerNames.APPLICATION))
                    .isEqualTo(LogLevel.DEBUG);
        }
    }

    @Nested
    @DisplayName("/actuator/" + ActuatorEndpoints.PAYLOAD_LOGGING)
    class PayloadLoggingEndpointTests {

        @Test
        @DisplayName("Enables SOAP payload logging")
        void should_EnableSoapPayloadLogging_when_EndpointIsEnabled() throws Exception {
            configurePayloadLogging(true)
                    .andExpect(status().isNoContent());

            assertThat(configuredLevel(LoggerNames.SOAP_PAYLOAD))
                    .isEqualTo(LogLevel.TRACE);
        }

        @Test
        @DisplayName("Disables SOAP payload logging")
        void should_DisableSoapPayloadLogging_when_EndpointIsDisabled() throws Exception {
            configurePayloadLogging(false)
                    .andExpect(status().isNoContent());

            assertThat(configuredLevel(LoggerNames.SOAP_PAYLOAD)).
                    isEqualTo(LogLevel.OFF);
        }
    }

    private ResultActions configureApplicationLoggingDebug() throws Exception {
        return mockMvc.perform(post("/actuator/{endpoint}/{level}",
                ActuatorEndpoints.LOGGING,
                LogLevel.DEBUG
        ));
    }

    private ResultActions configurePayloadLogging(boolean enabled) throws Exception {
        return mockMvc.perform(post(
                "/actuator/{endpoint}/{enabled}",
                ActuatorEndpoints.PAYLOAD_LOGGING,
                enabled
        ));
    }

    private LogLevel configuredLevel(String loggerName) {
        var configuration = loggingSystem.getLoggerConfiguration(loggerName);
        return Objects.requireNonNull(configuration).getConfiguredLevel();
    }
}
