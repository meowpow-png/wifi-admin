package hr.ht.rnd.wifiadmin.infra.transport.soap.sync;

import hr.ht.rnd.wifiadmin.infra.transport.soap.PlatformProperties;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Provides access to the platform
 * synchronization schedule.
 */
@Component
public final class SynchronizationSchedule {

    private final PlatformProperties properties;
    private final Clock clock;

    SynchronizationSchedule(PlatformProperties properties, Clock clock) {
        Objects.requireNonNull(properties, "properties must not be null");
        Objects.requireNonNull(clock, "clock must not be null");

        this.properties = properties;
        this.clock = clock;
    }

    /**
     * Returns the next scheduled synchronization time.
     */
    public ZonedDateTime nextExecution() {
        var now = ZonedDateTime.now(clock);
        var next = now.with(properties.syncSchedule());

        if (!next.isAfter(now)) {
            next = next.plusDays(1);
        }
        return next;
    }
}
