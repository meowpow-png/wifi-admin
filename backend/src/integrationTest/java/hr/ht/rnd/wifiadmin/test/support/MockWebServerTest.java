package hr.ht.rnd.wifiadmin.test.support;

import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

/**
 * Base class for tests that require a {@link MockWebServer}.
 * <p>
 * A single server instance is created for the lifetime
 * of each test class and is automatically started
 * and shut down using the JUnit test lifecycle.
 */
public abstract class MockWebServerTest {

    protected static MockWebServer server;

    @BeforeAll
    static void setupMockWebServerTest() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    static void teardownMockWebServerTest() throws IOException {
        server.shutdown();
    }
}
