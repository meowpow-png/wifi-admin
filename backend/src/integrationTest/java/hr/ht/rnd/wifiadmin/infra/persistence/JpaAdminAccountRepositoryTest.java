package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.application.outbound.AdminAccountRepository;
import hr.ht.rnd.wifiadmin.domain.account.TestAccounts;
import hr.ht.rnd.wifiadmin.test.autoconfigure.JpaIntegrationTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@JpaIntegrationTest
@Import({
        JpaAdminAccountRepository.class,
        AdminAccountEntityMapper.class
})
class JpaAdminAccountRepositoryTest {

    @Autowired
    private AdminAccountRepository repository;

    @Nested
    @DisplayName("findByUsername")
    class FindByUsernameMethodTests {

        @Test
        @DisplayName("Returns persisted account")
        void should_ReturnPersistedAccount_when_AccountExists() {
            var expected = TestAccounts.admin();

            repository.save(expected);

            var result = repository.findByUsername(expected.username());
            assertThat(result).contains(expected);
        }

        @Test
        @DisplayName("Returns empty optional when account does not exist")
        void should_ReturnEmptyOptional_when_AccountDoesNotExist() {
            assertThat(repository.findByUsername("unknown")).isEmpty();
        }
    }

    @Nested
    @DisplayName("save")
    class SaveMethodTests {

        @Test
        @DisplayName("Persists account")
        void should_PersistAccount_when_AccountDoesNotExist() {
            var expected = TestAccounts.admin();

            repository.save(expected);

            var result = repository.findByUsername(expected.username());
            assertThat(result).contains(expected);
        }
    }
}
