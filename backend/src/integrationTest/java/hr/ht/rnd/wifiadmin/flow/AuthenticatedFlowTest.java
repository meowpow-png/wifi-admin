package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.flow.support.AuthenticationHandler;
import hr.ht.rnd.wifiadmin.flow.support.AuthenticationRequests;
import hr.ht.rnd.wifiadmin.flow.support.AuthenticationResponses;
import hr.ht.rnd.wifiadmin.flow.support.TestAdminAccount;
import hr.ht.rnd.wifiadmin.test.autoconfigure.MockMvcIntegrationTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
@MockMvcIntegrationTest
@Import({
        AuthenticationHandler.class,
        AuthenticationRequests.class,
        AuthenticationResponses.class,
        TestAdminAccount.class
})
abstract class AuthenticatedFlowTest {

    @Autowired
    AuthenticationHandler auth;

    @Autowired
    TestAdminAccount adminAccount;

    @BeforeEach
    void setupAuthenticatedFlowTest() {
        adminAccount.reset();
    }
}
