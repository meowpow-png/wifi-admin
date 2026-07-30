package hr.ht.rnd.wifiadmin.infra.transport.soap.sync;

import hr.ht.rnd.wifiadmin.infra.transport.soap.PlatformProperties;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZonedDateTime;

/**
 * Provides access to the platform
 * synchronization schedule.
 */
@Component
public final class SynchronizationSchedule {

    private final PlatformProperties properties;
    private final Clock clock;

    SynchronizationSchedule(PlatformProperties properties, Clock clock) {
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
