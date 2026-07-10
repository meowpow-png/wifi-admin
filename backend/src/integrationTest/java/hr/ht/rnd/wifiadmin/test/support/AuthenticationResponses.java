package hr.ht.rnd.wifiadmin.test.support;

import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.LoginResponse;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.ResultActions;

import tools.jackson.databind.ObjectMapper;

/**
 * Fixture for reading authentication responses.
 */
@TestComponent
public class AuthenticationResponses {

    private final ObjectMapper objectMapper;

    AuthenticationResponses(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Reads the login response from
     * the specified request result.
     *
     * @param result the request result
     *
     * @return the login response
     */
    public LoginResponse login(ResultActions result) {
        var response = result.andReturn().getResponse();
        return objectMapper.readValue(
                response.getContentAsByteArray(),
                LoginResponse.class
        );
    }
}
