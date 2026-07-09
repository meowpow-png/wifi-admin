package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.domain.account.TestAccounts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminAccountEntityMapperTest {

    @Nested
    @DisplayName("toEntity")
    class ToEntityMethodTests {

        @Test
        @DisplayName("Maps account fields to entity")
        void should_MapAccountFieldsToEntity_when_AccountIsValid() {
            var password = TestAccounts.password();
            var account = TestAccounts.admin(password);

            var entity = AdminAccountEntityMapper.toEntity(account);

            assertThat(entity.getUsername()).isEqualTo(account.username());
            assertThat(entity.getPassword()).isEqualTo(password.value());
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when account is null")
        void should_ThrowNullPointerException_when_AccountIsNull() {
            assertThatThrownBy(() -> AdminAccountEntityMapper.toEntity(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("toDomain")
    class ToDomainMethodTests {

        @Test
        @DisplayName("Maps entity fields to account")
        void should_MapEntityFieldsToAccount_when_EntityIsValid() {
            var accountFixture = TestAccounts.admin();
            var entity = new AdminAccountEntity(
                    accountFixture.username(),
                    accountFixture.password().value()
            );
            var account = AdminAccountEntityMapper.toDomain(entity);

            assertThat(account.username()).isEqualTo(accountFixture.username());
            assertThat(account.password()).isEqualTo(accountFixture.password());
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when entity is null")
        void should_ThrowNullPointerException_when_EntityIsNull() {
            assertThatThrownBy(() -> AdminAccountEntityMapper.toDomain(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when entity contains invalid data")
        void should_ThrowIllegalArgumentException_when_EntityContainsInvalidData() {
            var entity = new AdminAccountEntity(
                    " ",
                    TestAccounts.password().value()
            );
            assertThatThrownBy(() -> AdminAccountEntityMapper.toDomain(entity))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
