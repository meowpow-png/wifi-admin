package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.test.autoconfigure.DisableAsync;
import hr.ht.rnd.wifiadmin.test.autoconfigure.MockMvcIntegrationTest;
import hr.ht.rnd.wifiadmin.test.config.AuthenticationTestConfiguration;
import hr.ht.rnd.wifiadmin.test.config.TestPlatformClientConfiguration;
import hr.ht.rnd.wifiadmin.test.support.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import org.junit.jupiter.api.BeforeEach;

@DisableAsync
@SpringBootTest
@MockMvcIntegrationTest
@Import({
        WifiConfigurationHandler.class,
        WifiConfigurationRequests.class,
        WifiConfigurationResponses.class,
        TestPlatformClientConfiguration.class,
        AuthenticationTestConfiguration.class
})
abstract class WifiConfigurationFlowTest {

    @Autowired
    AuthenticationHandler auth;

    @Autowired
    WifiConfigurationHandler wifi;

    @Autowired
    WifiConfigurationRepository repository;

    @Autowired
    TestPlatformClient platformClient;

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    @SuppressWarnings("SqlWithoutWhere")
    void setupWifiConfigurationRetrievalFlowTest() {
        jdbc.update("delete from wifi_configuration");
        platformClient.reset();
    }
}
