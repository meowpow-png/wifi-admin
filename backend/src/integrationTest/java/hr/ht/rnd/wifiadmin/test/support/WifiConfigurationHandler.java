package hr.ht.rnd.wifiadmin.test.support;

import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class WifiConfigurationHandler {

    private final WifiConfigurationRequests requests;
    private final WifiConfigurationResponses responses;

    WifiConfigurationHandler(
            WifiConfigurationRequests requests,
            WifiConfigurationResponses responses
    ) {
        this.requests = requests;
        this.responses = responses;
    }

    public WifiConfigurationRequests requests() {
        return requests;
    }

    public WifiConfigurationResponses responses() {
        return responses;
    }
}
