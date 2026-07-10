package hr.ht.rnd.wifiadmin.test.support;

import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.LoginRequest;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Fixture for performing authentication requests.
 */
@TestComponent
public class AuthenticationRequests {

    private static final String LOGIN_ENDPOINT = "/auth/login";

    public static final String ADMIN_USERNAME = "test";
    public static final String ADMIN_PASSWORD = "test";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    AuthenticationRequests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    /**
     * Performs an authentication request
     * using the specified credentials.
     *
     * @param username the account username
     * @param password the account password
     *
     * @return the result of performing the authentication request
     * @throws Exception if the request cannot be performed
     */
    public ResultActions login(String username, String password) throws Exception {
        var request = new LoginRequest(username, password);

        return mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));
    }

    /**
     * Performs an authentication request
     * using the default account credentials.
     *
     * @return the result of performing the authentication request
     * @throws Exception if the request cannot be performed
     */
    public ResultActions login() throws Exception {
        return login(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    /**
     * Performs an authentication request
     * using the specified login request.
     *
     * @param request the login request
     *
     * @return the result of performing the authentication request
     * @throws NullPointerException if {@code request} is {@code null}
     * @throws Exception if the request cannot be performed
     */
    public ResultActions login(LoginRequest request) throws Exception {
        return mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));
    }

    /**
     * Performs an authentication request
     * using the specified request body.
     *
     * @param body the raw request body
     *
     * @return the result of performing the authentication request
     * @throws NullPointerException if {@code body} is {@code null}
     * @throws Exception if the request cannot be performed
     */
    public ResultActions loginWithBody(String body) throws Exception {
        return mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }
}
