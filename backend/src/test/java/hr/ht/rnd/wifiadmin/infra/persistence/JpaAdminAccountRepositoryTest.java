package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.domain.account.TestAccounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class JpaAdminAccountRepositoryTest {

    @Mock
    private AdminAccountJpaRepository jpaRepository;

    private JpaAdminAccountRepository repository;

    @BeforeEach
    void setupJpaAdminAccountRepositoryTest() {
        repository = new JpaAdminAccountRepository(jpaRepository);
    }

    @Nested
    @DisplayName("findByUsername")
    class FindByUsernameMethodTests {

        @Test
        @DisplayName("Throws persistence exception when lookup fails")
        void should_ThrowPersistenceException_when_FindByIdFails() {
            var username = TestAccounts.admin().username();
            var cause = new RuntimeException("boom");

            Mockito.when(jpaRepository.findById(username)).thenThrow(cause);

            assertThatThrownBy(() -> repository.findByUsername(username))
                    .isInstanceOf(PersistenceException.class)
                    .hasCause(cause);
        }
    }

    @Nested
    @DisplayName("save")
    class SaveMethodTests {

        @Test
        @DisplayName("Throws persistence exception when save fails")
        void should_ThrowPersistenceException_when_SaveFails() {
            var cause = new RuntimeException("boom");
            var entity = Mockito.any(AdminAccountEntity.class);

            Mockito.when(jpaRepository.save(entity)).thenThrow(cause);

            assertThatThrownBy(() -> repository.save(TestAccounts.admin()))
                    .isInstanceOf(PersistenceException.class)
                    .hasCause(cause);
        }
    }
}
