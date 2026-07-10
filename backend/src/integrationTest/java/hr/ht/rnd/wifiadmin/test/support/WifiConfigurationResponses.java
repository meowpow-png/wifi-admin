package hr.ht.rnd.wifiadmin.test.support;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.WifiConfigurationResponse;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.ResultActions;

import tools.jackson.databind.ObjectMapper;

/**
 * Fixture for reading Wi-Fi configuration responses.
 */
@TestComponent
public class WifiConfigurationResponses {

    private final ObjectMapper objectMapper;

    WifiConfigurationResponses(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Reads the Wi-Fi configuration response
     * from the specified request result.
     *
     * @param result the request result
     *
     * @return the Wi-Fi configuration response
     */
    public WifiConfigurationResponse retrieveConfiguration(ResultActions result) {
        var response = result.andReturn().getResponse();
        return objectMapper.readValue(
                response.getContentAsByteArray(),
                WifiConfigurationResponse.class
        );
    }

    public static WifiConfigurationResponse from(WifiConfiguration configuration) {
        var password = configuration.password();
        return new WifiConfigurationResponse(
                configuration.cpeId(),
                configuration.wifiBand(),
                configuration.ssid(),
                configuration.encryptionType(),
                password != null ? password.value() : null
        );
    }
}
