package hr.ht.rnd.wifiadmin.application.event;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import java.util.Objects;

/**
 * Published after successfully updating
 * a Wi-Fi configuration on the platform.
 *
 * @param configuration the updated Wi-Fi configuration
 */
public record PlatformConfigurationUpdatedEvent(
        WifiConfiguration configuration
) implements ApplicationEvent {

    public PlatformConfigurationUpdatedEvent {
        Objects.requireNonNull(configuration, "configuration must not be null");
    }
}
