package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WifiConfigurationReadModelTest {

    private WifiConfigurationReadModel readModel;

    @BeforeEach
    void setupWifiConfigurationReadModelTest() {
        readModel = new WifiConfigurationReadModel();
    }

    @Nested
    @DisplayName("findAll")
    class FindAllMethodTests {

        @Test
        @DisplayName("Returns all projected configurations")
        void should_ReturnAllProjectedConfigurations_when_ConfigurationsArePresent() {
            var first = TestWifiConfigurations.forCpeId("CPE_001");
            var second = TestWifiConfigurations.forCpeId("CPE_002");

            readModel.put(first);
            readModel.put(second);

            assertThat(readModel.findAll())
                    .containsExactlyInAnyOrder(first, second);
        }

        @Test
        @DisplayName("Returns immutable configuration list")
        void should_ReturnImmutableConfigurationList_when_ConfigurationsArePresent() {
            var configuration = TestWifiConfigurations.builder().build();

            readModel.put(configuration);
            var configurations = readModel.findAll();

            assertThatThrownBy(configurations::clear).isInstanceOf(
                    UnsupportedOperationException.class
            );
            assertThat(readModel.findAll()).containsExactly(configuration);
        }
    }

    @Nested
    @DisplayName("findByCpeId")
    class FindByCpeIdMethodTests {

        @Test
        @DisplayName("Returns projected configuration")
        void should_ReturnProjectedConfiguration_when_ConfigurationIsPresent() {
            var configuration = TestWifiConfigurations.builder().build();

            readModel.put(configuration);

            assertThat(readModel.findByCpeId(configuration.cpeId()))
                    .contains(configuration);
        }

        @Test
        @DisplayName("Returns empty result when configuration is missing")
        void should_ReturnEmptyResult_when_ConfigurationIsMissing() {
            assertThat(readModel.findByCpeId(TestWifiConfigurations.CPE_ID)).isEmpty();
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when CPE ID is null")
        void should_ThrowNullPointerException_when_CpeIdIsNull() {
            assertThatThrownBy(() -> readModel.findByCpeId(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("put")
    class PutMethodTests {

        @Test
        @DisplayName("Replaces projected configuration")
        void should_ReplaceProjectedConfiguration_when_CpeIdAlreadyExists() {
            var cpeId = TestWifiConfigurations.CPE_ID;
            var original = TestWifiConfigurations.builder()
                    .withCpeId(cpeId)
                    .withSsid("Original Wi-Fi")
                    .build();
            var replacement = TestWifiConfigurations.builder()
                    .withCpeId(cpeId)
                    .withSsid("Updated Wi-Fi")
                    .build();

            readModel.put(original);
            readModel.put(replacement);

            assertThat(readModel.findByCpeId(cpeId)).contains(replacement);
            assertThat(readModel.findAll()).containsExactly(replacement);
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when configuration is null")
        void should_ThrowNullPointerException_when_ConfigurationIsNull() {
            assertThatThrownBy(() -> readModel.put(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
