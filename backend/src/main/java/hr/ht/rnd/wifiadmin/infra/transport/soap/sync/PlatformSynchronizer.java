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
import java.util.Objects;

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
        Objects.requireNonNull(properties, "properties must not be null");
        Objects.requireNonNull(synchronization, "synchronization must not be null");
        Objects.requireNonNull(tracker, "tracker must not be null");
        Objects.requireNonNull(clock, "clock must not be null");

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
        log.info("Starting platform synchronization of {} CPE devices", idCount);

        tracker.start(LocalDate.now(clock), idCount);
        try {
            for (var i = 1; i <= idCount; i++) {
                var cpeId = properties.cpeIdFormat().formatted(i);
                log.debug("Synchronizing device '{}'", cpeId);

                synchronization.synchronize(cpeId);
            }
            log.info("Platform synchronization dispatching completed");
        }
        catch (Exception e) {
            tracker.abort();
            throw e;
        }
    }
}
