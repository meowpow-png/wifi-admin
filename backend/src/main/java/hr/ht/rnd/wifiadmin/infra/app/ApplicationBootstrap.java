package hr.ht.rnd.wifiadmin.infra.app;

import hr.ht.rnd.wifiadmin.common.DateTimeFormats;
import hr.ht.rnd.wifiadmin.infra.transport.soap.PlatformProperties;
import hr.ht.rnd.wifiadmin.infra.transport.soap.sync.PlatformSynchronizer;
import hr.ht.rnd.wifiadmin.infra.transport.soap.sync.SynchronizationSchedule;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.util.Objects;

@Component
final class ApplicationBootstrap {

    private static final Logger log = LoggerFactory.getLogger(ApplicationBootstrap.class);

    private final PlatformProperties properties;
    private final PlatformSynchronizer synchronizer;
    private final SynchronizationSchedule schedule;

    ApplicationBootstrap(
            PlatformProperties properties,
            PlatformSynchronizer synchronizer,
            SynchronizationSchedule schedule
    ) {
        Objects.requireNonNull(properties, "properties must not be null");
        Objects.requireNonNull(synchronizer, "synchronizer must not be null");
        Objects.requireNonNull(schedule, "schedule must not be null");

        this.properties = properties;
        this.synchronizer = synchronizer;
        this.schedule = schedule;
    }

    @EventListener(ApplicationReadyEvent.class)
    void bootstrap() {
        log.info("Starting application bootstrap");
        log.info("Application time zone: {}", ZoneId.systemDefault());
        if (properties.syncOnStartup()) {
            synchronizer.synchronize();
        }
        log.info("Next platform synchronization scheduled at {}",
                DateTimeFormats.LONG.format(schedule.nextExecution())
        );
        log.info("Application bootstrap completed");
    }
}
