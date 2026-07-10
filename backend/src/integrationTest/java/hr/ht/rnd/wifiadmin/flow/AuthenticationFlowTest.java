package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.application.outbound.AccessTokenVerifier;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.LoginRequest;
import hr.ht.rnd.wifiadmin.test.autoconfigure.MockMvcIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.AuthenticationRequests;
import hr.ht.rnd.wifiadmin.test.support.AuthenticationResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@MockMvcIntegrationTest
@Import({
        AuthenticationRequests.class,
        AuthenticationResponses.class
})
public class AuthenticationFlowTest {

    @Autowired
    private AuthenticationRequests requests;

    @Autowired
    private AuthenticationResponses responses;

    @Autowired
    private AccessTokenVerifier tokenVerifier;

    @Test
    @DisplayName("Returns JWT when credentials are valid")
    void should_ReturnJwt_when_CredentialsAreValid() throws Exception {
        var request = new LoginRequest(
                AuthenticationRequests.ADMIN_USERNAME,
                AuthenticationRequests.ADMIN_PASSWORD
        );
        var result = requests.login(request).andExpect(status().isOk());

        var response = responses.login(result);

        assertThat(response.token()).isNotBlank();
        assertThat(tokenVerifier.verify(response.token()))
                .isEqualTo(AuthenticationRequests.ADMIN_USERNAME);
    }

    @Test
    @DisplayName("Returns unauthorized for invalid credentials")
    void should_ReturnUnauthorized_when_CredentialsAreInvalid() throws Exception {
        requests.login(AuthenticationRequests.ADMIN_USERNAME, "invalid-password")
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Returns bad request for blank credentials")
    void should_ReturnBadRequest_when_CredentialsAreBlank() throws Exception {
        requests.login("", "")
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns bad request for malformed JSON")
    void should_ReturnBadRequest_when_JsonIsMalformed() throws Exception {
        requests.loginWithBody("{\"username\":\"admin\"")
                .andExpect(status().isBadRequest());
    }
}
