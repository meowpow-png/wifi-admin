package hr.ht.rnd.wifiadmin.infra.transport.soap.sync;

import hr.ht.rnd.wifiadmin.common.LogContext;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

@Component
final class SynchronizationScheduler {

    private static final Logger log = LoggerFactory.getLogger(SynchronizationScheduler.class);

    private final PlatformSynchronizer synchronizer;
    private final SynchronizationSchedule schedule;

    SynchronizationScheduler(
            PlatformSynchronizer synchronizer,
            SynchronizationSchedule schedule
    ) {
        this.synchronizer = synchronizer;
        this.schedule = schedule;
    }

    @Scheduled(cron = "#{@platformSyncCronExpression}")
    void synchronize() {
        try (var ignored = LogContext.open()) {
            debug(log).withEvent(Event.SCHEDULED_SYNCHRONIZATION_TRIGGERED).log();

            synchronizer.synchronize();

            info(log).withEvent(Event.NEXT_PLATFORM_SYNCHRONIZATION_SCHEDULED)
                    .withField(Field.DATE, schedule.nextExecution())
                    .log();
        }
    }
}
