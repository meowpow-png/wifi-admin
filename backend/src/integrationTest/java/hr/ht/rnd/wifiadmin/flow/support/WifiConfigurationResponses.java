package hr.ht.rnd.wifiadmin.flow.support;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.WifiConfigurationResponse;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

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

    /**
     * Reads the Wi-Fi configuration collection response
     * from the specified request result.
     *
     * @param result the request result
     *
     * @return the Wi-Fi configuration responses
     */
    public List<WifiConfigurationResponse> retrieveConfigurations(ResultActions result) {
        var response = result.andReturn().getResponse();
        var configurations = objectMapper.readValue(
                response.getContentAsByteArray(),
                WifiConfigurationResponse[].class
        );
        return List.of(configurations);
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
