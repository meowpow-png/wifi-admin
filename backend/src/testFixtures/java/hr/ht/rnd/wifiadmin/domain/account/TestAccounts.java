package hr.ht.rnd.wifiadmin.domain.account;

public final class TestAccounts {

    private TestAccounts() {}

    public static AccountPassword password() {
        return new AccountPassword("admin");
    }

    public static AdminAccount admin() {
        return new AdminAccount("admin", password());
    }

    public static AdminAccount admin(AccountPassword password) {
        return new AdminAccount("admin", password);
    }
}
