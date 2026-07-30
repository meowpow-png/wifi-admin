package hr.ht.rnd.wifiadmin.infra.transport.soap.support;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Facade for controlling the mock SOAP platform.
 */
public final class MockSoapPlatform {

    public static final String PLATFORM_PATH = "/platform";

    private final MockWebServer server;

    public MockSoapPlatform(MockWebServer server) {
        this.server = server;
    }

    public static MockSoapPlatform create(MockWebServer server) {
        return new MockSoapPlatform(server);
    }

    public String endpoint() {
        return server.url(PLATFORM_PATH).toString();
    }

    public void enqueueRetrieved(WifiConfiguration configuration) {
        enqueue(SoapPlatformResponses.retrieved(configuration));
    }

    public void enqueueRetrievedWithLeadingWhitespace(WifiConfiguration configuration) {
        enqueue(SoapPlatformResponses.retrievedWithLeadingWhitespace(configuration));
    }

    public void enqueueUpdated(WifiConfiguration configuration) {
        enqueue(SoapPlatformResponses.updated(configuration));
    }

    public void enqueueNotFoundFault() {
        enqueue(SoapPlatformResponses.notFoundFault());
    }

    public void enqueueClientFault() {
        enqueue(SoapPlatformResponses.clientFault());
    }

    public void enqueueInvalidRetrieveResponse() {
        enqueue(SoapPlatformResponses.invalidRetrieveResponse());
    }

    public void enqueueInvalidUpdateResponse() {
        enqueue(SoapPlatformResponses.invalidUpdateResponse());
    }

    public void enqueueDisconnectAtStart() {
        enqueue(SoapPlatformResponses.disconnectAtStart());
    }

    public void assertRetrieveRequest(String cpeId) throws InterruptedException {
        SoapPlatformRequests.assertRetrieveRequest(takeRequestBody(), cpeId);
    }

    public void assertUpdateRequest(WifiConfiguration configuration) throws InterruptedException {
        SoapPlatformRequests.assertUpdateRequest(takeRequestBody(), configuration);
    }

    public String takeRequestBody() throws InterruptedException {
        var request = server.takeRequest(1, TimeUnit.SECONDS);

        assertThat(request).isNotNull();
        assertThat(request.getPath()).isEqualTo(PLATFORM_PATH);

        return request.getBody().readUtf8();
    }

    public void drainRequests() throws InterruptedException {
        var request = server.takeRequest(10, TimeUnit.MILLISECONDS);
        while (request != null) {
            request = server.takeRequest(10, TimeUnit.MILLISECONDS);
        }
    }

    private void enqueue(MockResponse response) {
        server.enqueue(response);
    }
}
