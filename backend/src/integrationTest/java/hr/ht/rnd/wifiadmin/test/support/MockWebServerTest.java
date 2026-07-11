package hr.ht.rnd.wifiadmin.test.support;

import okhttp3.mockwebserver.MockWebServer;
import org.jspecify.annotations.NullUnmarked;

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
@NullUnmarked
public abstract class MockWebServerTest {

    protected static MockWebServer server;

    @BeforeAll
    @SuppressWarnings("resource")
    static void setupMockWebServerTest() {
        server();
    }

    @AfterAll
    static void teardownMockWebServerTest() throws IOException {
        if (server != null) {
            server.shutdown();
            server = null;
        }
    }

    protected static synchronized MockWebServer server() {
        if (server == null) {
            var candidate = new MockWebServer();
            try {
                candidate.start();
                server = candidate;
            }
            catch (IOException e) {
                try {
                    candidate.shutdown();
                }
                catch (IOException exception) {
                    e.addSuppressed(exception);
                }
                throw new IllegalStateException("Failed to start MockWebServer", e);
            }
        }
        return server;
    }
}
