package hr.ht.rnd.wifiadmin.infra.transport.soap.sync;

import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

/**
 * Tracks the progress of platform synchronization.
 * <p>
 * <strong>Implementation Note:</strong>
 * This implementation assumes a single synchronization
 * run at a time. Stale configurations are removed only after
 * all synchronized configurations have been successfully persisted.
 */
@Component
public final class SynchronizationTracker {

    private static final Logger log = LoggerFactory.getLogger(SynchronizationTracker.class);

    private final WifiConfigurationRepository repository;

    private @Nullable SynchronizationRun run;

    SynchronizationTracker(WifiConfigurationRepository repository) {
        this.repository = repository;
    }

    /**
     * Starts tracking platform synchronization.
     *
     * @param date the synchronization date
     * @param expected the expected number of synchronized configurations
     *
     * @throws NullPointerException if {@code date} is {@code null}
     * @throws IllegalArgumentException if {@code expected} is not positive
     * @throws IllegalStateException if a synchronization is already in progress
     */
    synchronized void start(LocalDate date, int expected) {
        if (run != null) {
            throw new IllegalStateException("Synchronization already in progress");
        }
        debug(log).withEvent(Event.PLATFORM_SYNCHRONIZATION_TRACKING_STARTED)
                .withField(Field.DATE, date)
                .withField(Field.EXPECTED_CONFIGURATION_COUNT, expected)
                .log();

        run = new SynchronizationRun(date, expected);
    }

    /**
     * Records the successful completion
     * of a synchronized configuration.
     * <p>
     * If {@code date} is {@code null}, or the date
     * matches the current run date the method is no-op.
     *
     * @param date the synchronization date, or {@code null} if unknown
     *
     * @throws PersistenceException if stale configurations cannot be removed
     */
    public synchronized void complete(LocalDate date) {
        if (run == null || !run.date.equals(date)) {
            return;
        }
        debug(log).withEvent(Event.PLATFORM_SYNCHRONIZATION_PROGRESS_UPDATED)
                .withField(Field.CONFIGURATION_COUNT, run.completed)
                .withField(Field.EXPECTED_CONFIGURATION_COUNT, run.expected)
                .log();

        if (run.complete()) {
            debug(log).withMessage("Removing stale configurations")
                    .withField(Field.DATE, date)
                    .log();

            repository.deleteOlderThan(date);

            info(log).withEvent(Event.PLATFORM_SYNCHRONIZATION_COMPLETED)
                    .withField(Field.DATE, date)
                    .withField(Field.CONFIGURATION_COUNT, run.completed)
                    .log();

            run = null;
        }
    }

    /**
     * Aborts the active platform synchronization.
     */
    public synchronized void abort() {
        if (run != null) {
            warn(log).withEvent(Event.PLATFORM_SYNCHRONIZATION_ABORTED)
                    .withField(Field.DATE, run.date)
                    .withField(Field.CONFIGURATION_COUNT, run.completed)
                    .withField(Field.EXPECTED_CONFIGURATION_COUNT, run.expected)
                    .log();

            run = null;
        }
    }

    private static final class SynchronizationRun {

        private final LocalDate date;
        private final int expected;

        private int completed;

        SynchronizationRun(LocalDate date, int expected) {
            Objects.requireNonNull(date, "date must not be null");
            if (expected <= 0) {
                throw new IllegalArgumentException("expected must be positive");
            }
            this.date = date;
            this.expected = expected;
        }

        boolean complete() {
            completed++;
            return completed == expected;
        }
    }
}
