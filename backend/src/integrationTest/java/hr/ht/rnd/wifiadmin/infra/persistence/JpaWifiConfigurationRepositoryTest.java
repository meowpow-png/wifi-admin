package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.test.autoconfigure.DisableEncryption;
import hr.ht.rnd.wifiadmin.test.autoconfigure.JpaIntegrationTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisableEncryption
@JpaIntegrationTest
@Import({
        JpaWifiConfigurationRepository.class,
        WifiConfigurationEntityMapper.class
})
class JpaWifiConfigurationRepositoryTest {

    @Autowired
    private WifiConfigurationRepository repository;

    @Nested
    @DisplayName("findByCpeId")
    class FindByCpeIdMethodTests {

        @Test
        @DisplayName("Returns persisted configuration")
        void should_ReturnPersistedConfiguration_when_ConfigurationExists() {
            var expected = TestWifiConfigurations.builder().build();
            var date = LocalDate.of(2025, 10, 26);

            repository.save(expected, date);

            var result = repository.findByCpeId(expected.cpeId());
            assertThat(result).contains(expected);
        }

        @Test
        @DisplayName("Returns empty optional when configuration does not exist")
        void should_ReturnEmptyOptional_when_ConfigurationDoesNotExist() {
            assertThat(repository.findByCpeId("unknown-cpe")).isEmpty();
        }
    }

    @Nested
    @DisplayName("save")
    class SaveMethodTests {

        @Test
        @DisplayName("Persists configuration")
        void should_PersistConfiguration_when_ConfigurationDoesNotExist() {
            var expected = TestWifiConfigurations.builder().build();
            var date = LocalDate.of(2025, 10, 26);

            repository.save(expected, date);

            var result = repository.findByCpeId(expected.cpeId());
            assertThat(result).contains(expected);
        }
    }

    @Nested
    @DisplayName("deleteOlderThan")
    class DeleteOlderThanMethodTests {

        @Autowired
        private TestEntityManager entityManager;

        @Test
        @DisplayName("Deletes older configurations")
        void should_DeleteOlderConfigurations_when_ConfigurationsExist() {
            var oldConfig = TestWifiConfigurations.builder()
                    .withCpeId("CPE_001")
                    .build();
            var newConfig = TestWifiConfigurations.builder()
                    .withCpeId("CPE_002")
                    .build();

            var cutoffDate = LocalDate.of(2025, 10, 26);
            var olderDate = cutoffDate.minusDays(1);

            repository.save(oldConfig, olderDate);
            repository.save(newConfig, cutoffDate);

            entityManager.flush();
            entityManager.clear();

            repository.deleteOlderThan(cutoffDate);

            assertThat(repository.findByCpeId(oldConfig.cpeId())).isEmpty();
            assertThat(repository.findByCpeId(newConfig.cpeId())).contains(newConfig);
        }

        @Test
        @DisplayName("Preserves newer configurations")
        void should_PreserveNewerConfigurations_when_DeletingOlderConfigurations() {
            var configuration = TestWifiConfigurations.builder().build();
            var cutoffDate = LocalDate.of(2025, 10, 26);

            repository.save(configuration, cutoffDate);
            repository.deleteOlderThan(cutoffDate);

            var result = repository.findByCpeId(configuration.cpeId());
            assertThat(result).contains(configuration);
        }
    }
}
