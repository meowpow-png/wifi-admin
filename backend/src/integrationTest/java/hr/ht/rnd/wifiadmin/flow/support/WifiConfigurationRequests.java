package hr.ht.rnd.wifiadmin.flow.support;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.WifiConfigurationRequest;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Fixture for performing Wi-Fi configuration requests.
 */
@TestComponent
public class WifiConfigurationRequests {

    private static final String RETRIEVE_ENDPOINT = "/wifi-parameter/{cpeId}";
    private static final String UPDATE_ENDPOINT = "/wifi-parameter";
    private static final String BEARER_PREFIX = "Bearer ";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    WifiConfigurationRequests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    /**
     * Performs an authenticated Wi-Fi configuration retrieval request.
     *
     * @param token the bearer token
     * @param cpeId the CPE identifier
     *
     * @return the result of performing the request
     * @throws Exception if the request cannot be performed
     */
    public ResultActions retrieveConfiguration(String token, String cpeId) throws Exception {
        return mockMvc.perform(get(RETRIEVE_ENDPOINT, cpeId)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token));
    }

    /**
     * Performs an authenticated Wi-Fi configuration update request.
     *
     * @param token the bearer token
     * @param request the Wi-Fi configuration request
     *
     * @return the result of performing the request
     * @throws Exception if the request cannot be performed
     */
    public ResultActions updateConfiguration(String token, WifiConfigurationRequest request) throws Exception {
        return mockMvc.perform(put(UPDATE_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));
    }

    /**
     * Performs an authenticated Wi-Fi configuration update request
     * using the specified raw request body.
     *
     * @param token the bearer token
     * @param body the raw request body
     *
     * @return the result of performing the request
     * @throws Exception if the request cannot be performed
     */
    public ResultActions updateConfiguration(String token, String body) throws Exception {
        return mockMvc.perform(put(UPDATE_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    public static WifiConfigurationRequest from(WifiConfiguration configuration) {
        var password = configuration.password();
        return new WifiConfigurationRequest(
                configuration.cpeId(),
                configuration.wifiBand(),
                configuration.ssid(),
                configuration.encryptionType(),
                password != null ? password.value() : null
        );
    }
}
