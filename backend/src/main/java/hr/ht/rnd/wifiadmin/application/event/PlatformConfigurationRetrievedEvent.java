package hr.ht.rnd.wifiadmin.application.event;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Published after successfully retrieving
 * a Wi-Fi configuration from the platform.
 *
 * @param configuration the retrieved Wi-Fi configuration
 * @param lastSynchronized the synchronization date, or {@code null} if unknown
 */
public record PlatformConfigurationRetrievedEvent(
        WifiConfiguration configuration,
        @Nullable LocalDate lastSynchronized
) implements ApplicationEvent {

    public PlatformConfigurationRetrievedEvent {
        Objects.requireNonNull(configuration, "configuration must not be null");
    }
}
