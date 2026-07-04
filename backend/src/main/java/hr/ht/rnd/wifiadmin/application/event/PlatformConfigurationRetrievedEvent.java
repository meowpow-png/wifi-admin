package hr.ht.rnd.wifiadmin.application.event;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import java.util.Objects;

/**
 * Published after successfully retrieving
 * a Wi-Fi configuration from the platform.
 *
 * @param configuration the retrieved Wi-Fi configuration
 */
public record PlatformConfigurationRetrievedEvent(
        WifiConfiguration configuration
) implements ApplicationEvent {

    public PlatformConfigurationRetrievedEvent {
        Objects.requireNonNull(configuration, "configuration must not be null");
    }
}
