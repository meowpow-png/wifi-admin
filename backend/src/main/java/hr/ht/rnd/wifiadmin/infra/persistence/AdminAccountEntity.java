package hr.ht.rnd.wifiadmin.infra.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@SuppressWarnings("unused")
@Table(name = "admin_accounts")
class AdminAccountEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    protected AdminAccountEntity() {}

    AdminAccountEntity(String username, String password) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(password, "password must not be null");

        this.username = username;
        this.password = password;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }
}
