package hr.ht.rnd.wifiadmin.flow.support;

import hr.ht.rnd.wifiadmin.application.outbound.AdminAccountRepository;
import hr.ht.rnd.wifiadmin.application.outbound.PasswordHasher;
import hr.ht.rnd.wifiadmin.domain.account.AdminAccount;

import org.springframework.boot.test.context.TestComponent;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Fixture for resetting the shared administrator test account.
 */
@TestComponent
public class TestAdminAccount {

    public static final String USERNAME = "test";
    public static final String PASSWORD = "test";

    private final AdminAccountRepository accounts;
    private final PasswordHasher hasher;

    public TestAdminAccount(
            AdminAccountRepository accounts,
            PasswordHasher hasher
    ) {
        this.accounts = accounts;
        this.hasher = hasher;
    }

    public void reset() {
        var account = new AdminAccount(USERNAME, hasher.hash(PASSWORD));
        accounts.save(account);
    }

    public void assertPasswordMatches(String password) {
        var account = accounts.findByUsername(USERNAME);
        assertThat(account).get().satisfies(admin ->
                assertThat(hasher.matches(password, admin.password())).isTrue()
        );
    }
}
