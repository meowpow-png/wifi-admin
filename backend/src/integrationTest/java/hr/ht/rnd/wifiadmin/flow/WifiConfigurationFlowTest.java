package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.flow.support.WifiConfigurationRequests;
import hr.ht.rnd.wifiadmin.flow.support.WifiConfigurationResponses;
import hr.ht.rnd.wifiadmin.test.autoconfigure.DisableAsync;
import hr.ht.rnd.wifiadmin.test.config.TestPlatformClientConfiguration;
import hr.ht.rnd.wifiadmin.test.support.TestPlatformClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import org.junit.jupiter.api.BeforeEach;

@DisableAsync
@Import({
        WifiConfigurationRequests.class,
        WifiConfigurationResponses.class,
        TestPlatformClientConfiguration.class
})
abstract class WifiConfigurationFlowTest extends AuthenticatedFlowTest {

    Wifi wifi;

    @Autowired
    WifiConfigurationRepository repository;

    @Autowired
    TestPlatformClient platformClient;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    void setupWifiConfigurationFixtures(
            WifiConfigurationRequests requests,
            WifiConfigurationResponses responses
    ) {
        wifi = new Wifi(requests, responses);
    }

    @BeforeEach
    @SuppressWarnings("SqlWithoutWhere")
    void setupWifiConfigurationFlowTest() {
        jdbc.update("delete from wifi_configuration");
        platformClient.reset();
    }

    record Wifi(
            WifiConfigurationRequests requests,
            WifiConfigurationResponses responses
    ) {}
}
