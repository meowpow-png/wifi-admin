package hr.ht.rnd.wifiadmin.application.service;

import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationProjection;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationView;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory Wi-Fi configuration read model.
 */
@Service
class WifiConfigurationReadModel implements WifiConfigurationView, WifiConfigurationProjection {

    private final ConcurrentHashMap<String, WifiConfiguration> configurations =
            new ConcurrentHashMap<>();

    @Override
    public List<WifiConfiguration> findAll() {
        return List.copyOf(configurations.values());
    }

    @Override
    public Optional<WifiConfiguration> findByCpeId(String cpeId) {
        Objects.requireNonNull(cpeId, "cpeId must not be null");
        return Optional.ofNullable(configurations.get(cpeId));
    }

    @Override
    public void put(WifiConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        configurations.put(configuration.cpeId(), configuration);
    }
}
