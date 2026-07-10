package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.application.outbound.PasswordEncryptor;
import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

@WiringIntegrationTest
class JpaWifiConfigurationRepositoryWiringTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(RepositoryTestConfiguration.class);

    @Nested
    @DisplayName("deleteOlderThan")
    class DeleteOlderThanMethodTests {

        @Test
        @DisplayName("Uses transactional boundary")
        void should_UseTransactionalBoundary_when_MethodIsInvoked() {
            TestApplicationContextRunner.from(runner)
                    .withTransactionManagement()
                    .hasTransactionalMethod(JpaWifiConfigurationRepository.class, "deleteOlderThan")
                    .doesNotFail();
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class RepositoryTestConfiguration {

        @Bean
        WifiConfigurationJpaRepository wifiConfigurationJpaRepository() {
            return mock(WifiConfigurationJpaRepository.class);
        }

        @Bean
        WifiConfigurationEntityMapper wifiConfigurationEntityMapper() {
            return new WifiConfigurationEntityMapper(mock(PasswordEncryptor.class));
        }

        @Bean
        JpaWifiConfigurationRepository jpaWifiConfigurationRepository(
                WifiConfigurationJpaRepository repository,
                WifiConfigurationEntityMapper mapper
        ) {
            return new JpaWifiConfigurationRepository(repository, mapper);
        }
    }
}
