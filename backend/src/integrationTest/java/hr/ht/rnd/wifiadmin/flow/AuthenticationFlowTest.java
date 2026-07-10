package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.application.outbound.AccessTokenVerifier;
import hr.ht.rnd.wifiadmin.flow.support.TestAdminAccount;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.LoginRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationFlowTest extends AuthenticatedFlowTest {

    @Autowired
    private AccessTokenVerifier tokenVerifier;

    @Test
    @DisplayName("Returns JWT when credentials are valid")
    void should_ReturnJwt_when_CredentialsAreValid() throws Exception {
        var request = new LoginRequest(
                TestAdminAccount.USERNAME,
                TestAdminAccount.PASSWORD
        );
        var result = auth.requests().login(request).andExpect(status().isOk());

        var response = auth.responses().login(result);

        assertThat(response.token()).isNotBlank();
        assertThat(tokenVerifier.verify(response.token()))
                .isEqualTo(TestAdminAccount.USERNAME);
    }

    @Test
    @DisplayName("Returns unauthorized for invalid credentials")
    void should_ReturnUnauthorized_when_CredentialsAreInvalid() throws Exception {
        var password = "invalid-password";

        auth.requests().login(TestAdminAccount.USERNAME, password)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Returns bad request for blank credentials")
    void should_ReturnBadRequest_when_CredentialsAreBlank() throws Exception {
        auth.requests().login("", "")
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns bad request for malformed JSON")
    void should_ReturnBadRequest_when_JsonIsMalformed() throws Exception {
        auth.requests().loginWithBody("{\"username\":\"admin\"")
                .andExpect(status().isBadRequest());
    }
}
