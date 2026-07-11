package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.outbound.AccessTokenIssuer;
import hr.ht.rnd.wifiadmin.flow.support.TestAdminAccount;
import hr.ht.rnd.wifiadmin.infra.transport.rest.ErrorBodyDto;
import hr.ht.rnd.wifiadmin.infra.transport.rest.ErrorCode;
import hr.ht.rnd.wifiadmin.test.autoconfigure.MockMvcIntegrationTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcIntegrationTest
@SpringBootTest(properties =
        "security.public-endpoints=/security-test/auth/login"
)
class SecurityFilterChainTest {

    private static final String BEARER_PREFIX = "Bearer ";
    public static final String PUBLIC_ENDPOINT = "/security-test/auth/login";
    public static final String PROTECTED_ENDPOINT = "/security-test/protected";
    public static final String ACTUATOR_ENDPOINT = "/actuator/security-test";
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccessTokenIssuer tokens;

    @Nested
    @DisplayName("authorization")
    class AuthorizationTests {

        @Test
        @DisplayName("Allows public endpoints without bearer token")
        void should_AllowPublicEndpoints_when_BearerTokenIsMissing() throws Exception {
            mockMvc.perform(post(PUBLIC_ENDPOINT))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Rejects protected endpoints without bearer token")
        void should_RejectProtectedEndpoints_when_BearerTokenIsMissing() throws Exception {
            mockMvc.perform(put(PROTECTED_ENDPOINT))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Rejects protected endpoints with invalid bearer token")
        void should_RejectProtectedEndpoints_when_BearerTokenIsInvalid() throws Exception {
            var request = put(PROTECTED_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + "invalid-token");

            mockMvc.perform(request).andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Allows protected endpoints with valid bearer token")
        void should_AllowProtectedEndpoints_when_BearerTokenIsValid() throws Exception {
            var token = tokens.issue(TestAdminAccount.USERNAME);
            var request = put(PROTECTED_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token);

            mockMvc.perform(request).andExpect(status().isOk());
        }

        @Test
        @DisplayName("Allows actuator endpoints without bearer token")
        void should_AllowActuatorEndpoints_when_BearerTokenIsMissing() throws Exception {
            mockMvc.perform(post(ACTUATOR_ENDPOINT))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("authenticationEntryPoint")
    class AuthenticationEntryPointTests {

        @Test
        @DisplayName("Returns unauthorized response JSON when authentication is missing")
        void should_ReturnUnauthorizedResponseJson_when_AuthenticationIsMissing() throws Exception {
            var result = mockMvc.perform(put(PROTECTED_ENDPOINT))
                    .andExpect(status().isUnauthorized())
                    .andReturn()
                    .getResponse();

            var body = objectMapper.readValue(
                    result.getContentAsByteArray(),
                    ErrorBodyDto.class
            );
            assertThat(result.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(body.code()).isEqualTo(ErrorCode.AUTHENTICATION_FAILED.name());
        }
    }

    @RestController
    static class SecurityFilterChainTestController {

        @PostMapping(PUBLIC_ENDPOINT)
        void publicEndpoint() {}

        @PutMapping(PROTECTED_ENDPOINT)
        void protectedEndpoint() {}

        @PostMapping(ACTUATOR_ENDPOINT)
        void actuatorEndpoint() {}
    }
}
