package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.flow.support.AdminPasswordRequests;
import hr.ht.rnd.wifiadmin.flow.support.TestAdminAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(AdminPasswordRequests.class)
class AdminPasswordFlowTest extends AuthenticatedFlowTest {

    private static final String NEW_PASSWORD = "new-password";

    @Autowired
    private AdminPasswordRequests passwordRequests;

    @Test
    @DisplayName("Changes password when request is authenticated")
    void should_ChangePassword_when_RequestIsAuthenticated() throws Exception {
        var token = auth.accessToken();
        var password = TestAdminAccount.PASSWORD;

        passwordRequests.changePassword(token, password, NEW_PASSWORD)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Persists password hash when password is changed")
    void should_PersistPasswordHash_when_PasswordIsChanged() throws Exception {
        var token = auth.accessToken();
        var password = TestAdminAccount.PASSWORD;

        passwordRequests.changePassword(token, password, NEW_PASSWORD)
                .andExpect(status().isNoContent());

        adminAccount.assertPasswordMatches(NEW_PASSWORD);
    }

    @Test
    @DisplayName("Returns unauthorized when request is unauthenticated")
    void should_ReturnUnauthorized_when_RequestIsUnauthenticated() throws Exception {
        passwordRequests.changePassword(TestAdminAccount.PASSWORD, NEW_PASSWORD)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Returns unauthorized when current password is incorrect")
    void should_ReturnUnauthorized_when_CurrentPasswordIsIncorrect() throws Exception {
        var token = auth.accessToken();
        var currentPassword = "invalid-password";

        passwordRequests.changePassword(token, currentPassword, NEW_PASSWORD)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Returns bad request when password fields are blank")
    void should_ReturnBadRequest_when_PasswordFieldsAreBlank() throws Exception {
        passwordRequests.changePassword(auth.accessToken(), "", "")
                .andExpect(status().isBadRequest());
    }
}
