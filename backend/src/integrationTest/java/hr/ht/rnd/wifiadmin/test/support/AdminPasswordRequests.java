package hr.ht.rnd.wifiadmin.test.support;

import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.ChangePasswordRequest;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Fixture for performing administrator password requests.
 */
@TestComponent
public class AdminPasswordRequests {

    private static final String PASSWORD_ENDPOINT = "/admin/password";
    private static final String BEARER_PREFIX = "Bearer ";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    AdminPasswordRequests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    /**
     * Performs an authenticated password change request.
     *
     * @param token the bearer token
     * @param currentPassword the current password
     * @param newPassword the new password
     *
     * @return the result of performing the password change request
     * @throws Exception if the request cannot be performed
     */
    public ResultActions changePassword(
            String token,
            String currentPassword,
            String newPassword
    ) throws Exception {
        var request = new ChangePasswordRequest(currentPassword, newPassword);

        return changePassword(token, request);
    }

    /**
     * Performs an authenticated password change request.
     *
     * @param token the bearer token
     * @param request the password change request
     *
     * @return the result of performing the password change request
     * @throws Exception if the request cannot be performed
     */
    public ResultActions changePassword(String token, ChangePasswordRequest request) throws Exception {
        return mockMvc.perform(put(PASSWORD_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));
    }

    /**
     * Performs an unauthenticated password change request.
     *
     * @param currentPassword the current password
     * @param newPassword the new password
     *
     * @return the result of performing the password change request
     * @throws Exception if the request cannot be performed
     */
    public ResultActions changePassword(String currentPassword, String newPassword) throws Exception {
        var request = new ChangePasswordRequest(currentPassword, newPassword);

        return mockMvc.perform(put(PASSWORD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));
    }
}
