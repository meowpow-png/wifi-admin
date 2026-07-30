package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationView;
import hr.ht.rnd.wifiadmin.application.outbound.EventPublisher;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PlatformAdministrationServiceTest {

    @Mock
    private PlatformClient client;

    @Mock
    private WifiConfigurationRepository repository;

    @Mock
    private WifiConfigurationView view;

    @Mock
    private EventPublisher events;

    private PlatformAdministrationService service;

    @BeforeEach
    void setupPlatformAdministrationServiceTest() {
        service = new PlatformAdministrationService(client, repository, view, events);
    }

    @Nested
    @DisplayName("retrieveConfigurations")
    class RetrieveConfigurationsMethodTests {

        @Test
        @DisplayName("Returns configurations from view")
        void should_ReturnConfigurationsFromView_when_ConfigurationsAreRetrieved() {
            var configurations = List.of(
                    TestWifiConfigurations.forCpeId("CPE_001"),
                    TestWifiConfigurations.forCpeId("CPE_002")
            );
            Mockito.when(view.findAll()).thenReturn(configurations);

            assertThat(service.retrieveConfigurations()).isEqualTo(configurations);
            Mockito.verifyNoInteractions(client, repository, events);
        }
    }
}
