package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.application.outbound.AdminAccountRepository;
import hr.ht.rnd.wifiadmin.application.outbound.PasswordHasher;
import hr.ht.rnd.wifiadmin.domain.account.AdminAccount;
import hr.ht.rnd.wifiadmin.test.autoconfigure.MockMvcIntegrationTest;
import hr.ht.rnd.wifiadmin.test.config.AuthenticationTestConfiguration;
import hr.ht.rnd.wifiadmin.test.support.AdminPasswordRequests;
import hr.ht.rnd.wifiadmin.test.support.AuthenticationHandler;
import hr.ht.rnd.wifiadmin.test.support.AuthenticationRequests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@MockMvcIntegrationTest
@Import({
        AuthenticationTestConfiguration.class,
        AdminPasswordRequests.class
})
class AdminPasswordFlowTest {

    private static final String NEW_PASSWORD = "new-password";

    @Autowired
    private AuthenticationHandler auth;

    @Autowired
    private AdminPasswordRequests passwordRequests;

    @Autowired
    private AdminAccountRepository accounts;

    @Autowired
    private PasswordHasher hasher;

    @BeforeEach
    void setupAdminPasswordFlowTest() {
        var account = new AdminAccount(
                AuthenticationRequests.ADMIN_USERNAME,
                hasher.hash(AuthenticationRequests.ADMIN_PASSWORD)
        );
        accounts.save(account);
    }

    @Test
    @DisplayName("Changes password when request is authenticated")
    void should_ChangePassword_when_RequestIsAuthenticated() throws Exception {
        var token = auth.accessToken();
        var password = AuthenticationRequests.ADMIN_PASSWORD;

        passwordRequests.changePassword(token, password, NEW_PASSWORD)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Persists password hash when password is changed")
    void should_PersistPasswordHash_when_PasswordIsChanged() throws Exception {
        var token = auth.accessToken();
        var password = AuthenticationRequests.ADMIN_PASSWORD;

        passwordRequests.changePassword(token, password, NEW_PASSWORD)
                .andExpect(status().isNoContent());

        var account = accounts.findByUsername(
                AuthenticationRequests.ADMIN_USERNAME
        );
        assertThat(account).get().satisfies(admin ->
                assertThat(hasher.matches(NEW_PASSWORD, admin.password())).isTrue()
        );
    }

    @Test
    @DisplayName("Returns unauthorized when request is unauthenticated")
    void should_ReturnUnauthorized_when_RequestIsUnauthenticated() throws Exception {
        passwordRequests.changePassword(AuthenticationRequests.ADMIN_PASSWORD, NEW_PASSWORD)
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
