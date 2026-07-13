package hr.ht.rnd.wifiadmin.infra.transport.soap.sync;

import hr.ht.rnd.wifiadmin.application.exception.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformException;
import hr.ht.rnd.wifiadmin.application.inbound.WifiConfigurationSynchronization;
import hr.ht.rnd.wifiadmin.infra.transport.soap.PlatformProperties;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

/**
 * Coordinates platform synchronization.
 */
@Component
public final class PlatformSynchronizer {

    private static final Logger log = LoggerFactory.getLogger(PlatformSynchronizer.class);

    private final PlatformProperties properties;
    private final WifiConfigurationSynchronization synchronization;
    private final SynchronizationTracker tracker;
    private final Clock clock;

    PlatformSynchronizer(
            PlatformProperties properties,
            WifiConfigurationSynchronization synchronization,
            SynchronizationTracker tracker,
            Clock clock
    ) {
        this.properties = properties;
        this.synchronization = synchronization;
        this.tracker = tracker;
        this.clock = clock;
    }

    /**
     * Starts platform synchronization.
     *
     * <p><strong>Implementation Note:</strong>
     * Configuration retrieval is dispatched synchronously,
     * while persistence executes asynchronously
     * through application events.
     *
     * @throws CpeNotFoundException if a configured device does not exist
     * @throws PlatformException if communication with the platform fails
     */
    public void synchronize() {
        int idCount = properties.cpeIdCount();
        info(log).withEvent(Event.PLATFORM_SYNCHRONIZATION_STARTED)
                .withField("cpe_count", idCount)
                .log();

        tracker.start(LocalDate.now(clock), idCount);
        try {
            for (var i = 1; i <= idCount; i++) {
                var cpeId = properties.cpeIdFormat().formatted(i);
                debug(log).withEvent(Event.CPE_SYNCHRONIZATION_STARTED)
                        .withField(Field.CPE_ID, cpeId)
                        .log();

                synchronization.synchronize(cpeId);
            }
            info(log).withEvent(Event.PLATFORM_SYNCHRONIZATION_DISPATCH_COMPLETED)
                    .withField("cpe_count", idCount)
                    .log();
        }
        catch (Exception e) {
            tracker.abort();
            throw e;
        }
    }
}
