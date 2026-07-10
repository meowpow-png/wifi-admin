package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.infra.app.TestClock;

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
class JpaWifiConfigurationRepositoryTest {

    @Mock
    private WifiConfigurationJpaRepository jpaRepository;

    @Mock
    private WifiConfigurationEntityMapper mapper;

    private JpaWifiConfigurationRepository repository;

    @BeforeEach
    void setupJpaWifiConfigurationRepositoryTest() {
        repository = new JpaWifiConfigurationRepository(jpaRepository, mapper);
    }

    @Nested
    @DisplayName("findByCpeId")
    class FindByCpeIdMethodTests {

        @Test
        @DisplayName("Throws PersistenceException when lookup fails")
        void should_ThrowPersistenceException_when_FindByIdFails() {
            var cpeId = TestWifiConfigurations.CPE_ID;
            var cause = new RuntimeException("boom");

            Mockito.when(jpaRepository.findById(cpeId)).thenThrow(cause);

            assertThatThrownBy(() -> repository.findByCpeId(cpeId))
                    .isInstanceOf(PersistenceException.class)
                    .hasCause(cause);
        }
    }

    @Nested
    @DisplayName("save")
    class SaveMethodTests {

        @Test
        @DisplayName("Throws PersistenceException when save fails")
        void should_ThrowPersistenceException_when_SaveFails() {
            var configuration = TestWifiConfigurations.builder().build();
            var date = TestClock.create().localDate();
            var entity = Mockito.mock(WifiConfigurationEntity.class);
            var cause = new RuntimeException("boom");

            Mockito.when(mapper.toEntity(configuration, date)).thenReturn(entity);
            Mockito.when(jpaRepository.save(entity)).thenThrow(cause);

            assertThatThrownBy(() -> repository.save(configuration, date))
                    .isInstanceOf(PersistenceException.class)
                    .hasCause(cause);
        }
    }

    @Nested
    @DisplayName("deleteOlderThan")
    class DeleteOlderThanMethodTests {

        @Test
        @DisplayName("Throws PersistenceException when delete fails")
        void should_ThrowPersistenceException_when_DeleteFails() {
            var date = TestClock.create().localDate();
            var cause = new RuntimeException("boom");

            Mockito.doThrow(cause).when(jpaRepository).deleteOlderThan(date);

            assertThatThrownBy(() -> repository.deleteOlderThan(date))
                    .isInstanceOf(PersistenceException.class)
                    .hasCause(cause);
        }
    }
}
