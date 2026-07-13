package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.inbound.WifiAdministration;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Application service coordinating
 * Wi-Fi management use cases.
 */
@Service
public class WifiService implements WifiAdministration {

    private final PlatformClient platformClient;

    WifiService(PlatformClient platformClient) {
        Objects.requireNonNull(platformClient, "platformClient must not be null");
        this.platformClient = platformClient;
    }

    @Override
    public WifiConfiguration retrieveConfiguration(String cpeId) {
        return platformClient.retrieveConfiguration(cpeId);
    }

    @Override
    public WifiConfiguration updateConfiguration(WifiConfiguration configuration) {
        return platformClient.updateConfiguration(configuration);
    }
}
