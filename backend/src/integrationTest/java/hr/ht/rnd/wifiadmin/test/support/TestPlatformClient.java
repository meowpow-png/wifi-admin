package hr.ht.rnd.wifiadmin.test.support;

import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class TestPlatformClient implements PlatformClient {

    private final Map<String, WifiConfiguration> devices = new HashMap<>();

    private final Map<String, Supplier<WifiConfiguration>> retrieveActions = new HashMap<>();
    private final Map<String, Supplier<WifiConfiguration>> updateActions = new HashMap<>();

    @Override
    public WifiConfiguration retrieveConfiguration(String cpeId) {
        var action = retrieveActions.get(cpeId);
        if (action != null) {
            return action.get();
        }
        return defaultRetrieve(cpeId);
    }

    @Override
    public WifiConfiguration updateConfiguration(WifiConfiguration configuration) {
        var action = updateActions.get(configuration.cpeId());
        if (action != null) {
            return action.get();
        }
        return defaultUpdate(configuration);
    }

    public void onRetrieveConfiguration(
            String cpeId,
            Supplier<WifiConfiguration> action
    ) {
        retrieveActions.put(cpeId, action);
    }

    public void onUpdateConfiguration(
            String cpeId,
            Supplier<WifiConfiguration> action
    ) {
        updateActions.put(cpeId, action);
    }

    public void addConfiguration(WifiConfiguration configuration) {
        devices.put(configuration.cpeId(), configuration);
    }

    public void addConfigurations(WifiConfiguration... configurations) {
        Arrays.stream(configurations).forEach(this::addConfiguration);
    }

    public void reset() {
        devices.clear();
        retrieveActions.clear();
        updateActions.clear();
    }

    private WifiConfiguration defaultRetrieve(String cpeId) {
        var configuration = devices.get(cpeId);
        if (configuration != null) {
            return configuration;
        }
        throw TestPlatformExceptions.cpeNotFound(cpeId);
    }

    private WifiConfiguration defaultUpdate(WifiConfiguration configuration) {
        devices.put(configuration.cpeId(), configuration);
        return configuration;
    }
}
