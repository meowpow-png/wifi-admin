package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.exception.AccountNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.application.outbound.AdminAccountRepository;
import hr.ht.rnd.wifiadmin.application.outbound.AdministratorProvider;
import hr.ht.rnd.wifiadmin.application.outbound.PasswordHasher;
import hr.ht.rnd.wifiadmin.domain.account.AccountPassword;
import hr.ht.rnd.wifiadmin.domain.account.AdminAccount;
import hr.ht.rnd.wifiadmin.domain.account.TestAccounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AdminPasswordServiceTest {

    private static final String CURRENT_PASSWORD = "current-password";
    private static final String NEW_PASSWORD = "new-password";

    @Mock
    private AdministratorProvider administrator;

    @Mock
    private AdminAccountRepository repository;

    @Mock
    private PasswordHasher hasher;

    private AdminPasswordService service;

    @BeforeEach
    void setupAdminPasswordServiceTest() {
        service = new AdminPasswordService(administrator, repository, hasher);
    }

    @Nested
    @DisplayName("changePassword")
    class ChangePasswordMethodTests {

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when current password is null")
        void should_ThrowNullPointerException_when_CurrentPasswordIsNull() {
            assertThatThrownBy(() -> service.changePassword(null, NEW_PASSWORD))
                    .isInstanceOf(NullPointerException.class);

            Mockito.verifyNoInteractions(administrator, repository, hasher);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when new password is null")
        void should_ThrowNullPointerException_when_NewPasswordIsNull() {
            assertThatThrownBy(() -> service.changePassword(CURRENT_PASSWORD, null))
                    .isInstanceOf(NullPointerException.class);

            Mockito.verifyNoInteractions(administrator, repository, hasher);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when current password is blank")
        void should_ThrowIllegalArgumentException_when_CurrentPasswordIsBlank() {
            assertThatThrownBy(() -> service.changePassword(" ", NEW_PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class);

            Mockito.verifyNoInteractions(administrator, repository, hasher);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when new password is blank")
        void should_ThrowIllegalArgumentException_when_NewPasswordIsBlank() {
            assertThatThrownBy(() -> service.changePassword(CURRENT_PASSWORD, " "))
                    .isInstanceOf(IllegalArgumentException.class);

            Mockito.verifyNoInteractions(administrator, repository, hasher);
        }

        @Test
        @DisplayName("Throws AuthenticationException when no administrator is authenticated")
        void should_ThrowAuthenticationException_when_AdministratorIsNotAuthenticated() {
            Mockito.when(administrator.username()).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.changePassword(CURRENT_PASSWORD, NEW_PASSWORD))
                    .isInstanceOf(AuthenticationException.class);

            Mockito.verifyNoInteractions(repository, hasher);
        }

        @Test
        @DisplayName("Throws AccountNotFoundException when administrator account is missing")
        void should_ThrowAccountNotFoundException_when_AdministratorAccountIsMissing() {
            var username = TestAccounts.admin().username();

            Mockito.when(administrator.username()).thenReturn(Optional.of(username));
            Mockito.when(repository.findByUsername(username)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.changePassword(CURRENT_PASSWORD, NEW_PASSWORD))
                    .isInstanceOf(AccountNotFoundException.class);

            Mockito.verifyNoInteractions(hasher);
        }

        @Test
        @DisplayName("Throws AuthenticationException when current password does not match")
        void should_ThrowAuthenticationException_when_CurrentPasswordDoesNotMatch() {
            var account = TestAccounts.admin();

            mockAuthenticatedAdministrator(account);
            Mockito.when(hasher.matches(CURRENT_PASSWORD, account.password())).thenReturn(false);

            assertThatThrownBy(() -> service.changePassword(CURRENT_PASSWORD, NEW_PASSWORD))
                    .isInstanceOf(AuthenticationException.class);

            Mockito.verify(repository, Mockito.never()).save(Mockito.any());
            Mockito.verify(hasher, Mockito.never()).hash(Mockito.anyString());
        }

        @Test
        @DisplayName("Does not save account when new password matches current password")
        void should_NotSaveAccount_when_NewPasswordMatchesCurrentPassword() {
            var account = TestAccounts.admin();

            mockAuthenticatedAdministrator(account);
            Mockito.when(hasher.matches(CURRENT_PASSWORD, account.password())).thenReturn(true);
            Mockito.when(hasher.matches(NEW_PASSWORD, account.password())).thenReturn(true);

            service.changePassword(CURRENT_PASSWORD, NEW_PASSWORD);

            Mockito.verify(repository, Mockito.never()).save(Mockito.any());
            Mockito.verify(hasher, Mockito.never()).hash(Mockito.anyString());
        }

        @Test
        @DisplayName("Saves account with hashed password when new password differs")
        void should_SaveAccountWithHashedPassword_when_NewPasswordDiffers() {
            var account = TestAccounts.admin();
            var newPasswordHash = new AccountPassword("new-password-hash");
            var savedAccount = ArgumentCaptor.forClass(AdminAccount.class);

            mockAuthenticatedAdministrator(account);
            Mockito.when(hasher.matches(CURRENT_PASSWORD, account.password())).thenReturn(true);
            Mockito.when(hasher.matches(NEW_PASSWORD, account.password())).thenReturn(false);
            Mockito.when(hasher.hash(NEW_PASSWORD)).thenReturn(newPasswordHash);

            service.changePassword(CURRENT_PASSWORD, NEW_PASSWORD);

            Mockito.verify(repository).save(savedAccount.capture());
            assertThat(savedAccount.getValue()).isEqualTo(
                    new AdminAccount(account.username(), newPasswordHash)
            );
        }
    }

    private void mockAuthenticatedAdministrator(AdminAccount account) {
        Mockito.when(administrator.username()).thenReturn(Optional.of(account.username()));
        Mockito.when(repository.findByUsername(account.username())).thenReturn(Optional.of(account));
    }
}
