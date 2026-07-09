package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.exception.AccountNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.AuthenticationException;
import hr.ht.rnd.wifiadmin.application.inbound.ChangeAdminPassword;
import hr.ht.rnd.wifiadmin.application.outbound.AdminAccountRepository;
import hr.ht.rnd.wifiadmin.application.outbound.AdministratorProvider;
import hr.ht.rnd.wifiadmin.application.outbound.PasswordHasher;
import hr.ht.rnd.wifiadmin.domain.account.AdminAccount;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
final class AdminPasswordService implements ChangeAdminPassword {

    private final AdministratorProvider administrator;
    private final AdminAccountRepository repository;
    private final PasswordHasher hasher;

    AdminPasswordService(
            AdministratorProvider administrator,
            AdminAccountRepository repository,
            PasswordHasher hasher
    ) {
        this.administrator = administrator;
        this.repository = repository;
        this.hasher = hasher;
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) {
        Objects.requireNonNull(currentPassword, "currentPassword must not be null");
        Objects.requireNonNull(newPassword, "newPassword must not be null");

        if (currentPassword.isBlank()) {
            throw new IllegalArgumentException("currentPassword must not be blank");
        }
        if (newPassword.isBlank()) {
            throw new IllegalArgumentException("newPassword must not be blank");
        }
        var username = administrator.username().orElseThrow(() ->
                new AuthenticationException("No administrator is authenticated")
        );
        var account = repository.findByUsername(username).orElseThrow(() ->
                new AccountNotFoundException("Administrator account not found")
        );
        if (!hasher.matches(currentPassword, account.password())) {
            throw new AuthenticationException("Current password is incorrect");
        }
        if (hasher.matches(newPassword, account.password())) {
            return;
        }
        var passwordHash = hasher.hash(newPassword);
        repository.save(new AdminAccount(
                account.username(),
                passwordHash
        ));
    }
}
