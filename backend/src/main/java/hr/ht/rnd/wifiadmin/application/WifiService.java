package hr.ht.rnd.wifiadmin.application;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public final class WifiService {

    private final PlatformClient platformClient;

    WifiService(PlatformClient platformClient) {
        Objects.requireNonNull(platformClient, "platformClient must not be null");
        this.platformClient = platformClient;
    }

    public WifiConfiguration retrieveConfiguration(String cpeId) {
        return platformClient.retrieveConfiguration(cpeId);
    }

    public WifiConfiguration updateConfiguration(WifiConfiguration configuration) {
        return platformClient.updateConfiguration(configuration);
    }
}
