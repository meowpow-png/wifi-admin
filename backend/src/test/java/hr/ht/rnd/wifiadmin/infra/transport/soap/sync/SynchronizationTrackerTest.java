package hr.ht.rnd.wifiadmin.infra.transport.soap.sync;

import hr.ht.rnd.wifiadmin.application.exception.PersistenceException;
import hr.ht.rnd.wifiadmin.application.outbound.WifiConfigurationRepository;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SynchronizationTrackerTest {

    private static final LocalDate SYNCHRONIZATION_DATE =
            LocalDate.of(2026, 7, 9);

    @Nested
    @DisplayName("start")
    class StartMethodTests {

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Throws NullPointerException when date is null")
        void should_ThrowNullPointerException_when_DateIsNull() {
            assertThatThrownBy(() -> tracker().start(null, 1))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when expected count is zero")
        void should_ThrowIllegalArgumentException_when_ExpectedCountIsZero() {
            assertThatThrownBy(() -> tracker().start(SYNCHRONIZATION_DATE, 0))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Throws IllegalStateException when synchronization is already in progress")
        void should_ThrowIllegalStateException_when_SynchronizationIsAlreadyInProgress() {
            var tracker = tracker();

            tracker.start(SYNCHRONIZATION_DATE, 1);

            assertThatThrownBy(() -> tracker.start(SYNCHRONIZATION_DATE, 1))
                    .isInstanceOf(IllegalStateException.class);
        }

        private static SynchronizationTracker tracker() {
            var repository = new RecordingWifiConfigurationRepository();
            return new SynchronizationTracker(repository);
        }
    }

    @Nested
    @DisplayName("complete")
    class CompleteMethodTests {

        @Test
        @DisplayName("Does nothing when synchronization has not started")
        void should_DoNothing_when_SynchronizationHasNotStarted() {
            var repository = new RecordingWifiConfigurationRepository();
            var tracker = new SynchronizationTracker(repository);

            tracker.complete(SYNCHRONIZATION_DATE);

            assertThat(repository.deletedDates()).isEmpty();
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        @DisplayName("Does nothing when date is null")
        void should_DoNothing_when_DateIsNull() {
            var repository = new RecordingWifiConfigurationRepository();
            var tracker = new SynchronizationTracker(repository);

            tracker.start(SYNCHRONIZATION_DATE, 1);
            tracker.complete(null);

            assertThat(repository.deletedDates()).isEmpty();
        }

        @Test
        @DisplayName("Does nothing when date does not match the active synchronization")
        void should_DoNothing_when_DateDoesNotMatchActiveSynchronization() {
            var repository = new RecordingWifiConfigurationRepository();
            var tracker = new SynchronizationTracker(repository);

            tracker.start(SYNCHRONIZATION_DATE, 1);
            tracker.complete(SYNCHRONIZATION_DATE.minusDays(1));

            assertThat(repository.deletedDates()).isEmpty();
        }

        @Test
        @DisplayName("Keeps tracking when fewer than expected configurations are completed")
        void should_KeepTracking_when_FewerThanExpectedConfigurationsAreCompleted() {
            var repository = new RecordingWifiConfigurationRepository();
            var tracker = new SynchronizationTracker(repository);

            tracker.start(SYNCHRONIZATION_DATE, 2);
            tracker.complete(SYNCHRONIZATION_DATE);

            assertThat(repository.deletedDates()).isEmpty();
        }

        @Test
        @DisplayName("Deletes stale configurations when expected configurations are completed")
        void should_DeleteStaleConfigurations_when_ExpectedConfigurationsAreCompleted() {
            var repository = new RecordingWifiConfigurationRepository();
            var tracker = new SynchronizationTracker(repository);

            tracker.start(SYNCHRONIZATION_DATE, 2);

            tracker.complete(SYNCHRONIZATION_DATE);
            tracker.complete(SYNCHRONIZATION_DATE);

            assertThat(repository.deletedDates())
                    .containsExactly(SYNCHRONIZATION_DATE);
        }

        @Test
        @DisplayName("Stops tracking when expected configurations are completed")
        void should_StopTracking_when_ExpectedConfigurationsAreCompleted() {
            var repository = new RecordingWifiConfigurationRepository();
            var tracker = new SynchronizationTracker(repository);

            tracker.start(SYNCHRONIZATION_DATE, 1);

            tracker.complete(SYNCHRONIZATION_DATE);
            tracker.complete(SYNCHRONIZATION_DATE);

            assertThat(repository.deletedDates())
                    .containsExactly(SYNCHRONIZATION_DATE);
        }

        @Test
        @DisplayName("Throws PersistenceException when stale configurations cannot be deleted")
        void should_ThrowPersistenceException_when_StaleConfigurationsCannotBeDeleted() {
            var repository = new RecordingWifiConfigurationRepository();
            var tracker = new SynchronizationTracker(repository);

            repository.failOnDelete();
            tracker.start(SYNCHRONIZATION_DATE, 1);

            assertThatThrownBy(() -> tracker.complete(SYNCHRONIZATION_DATE))
                    .isInstanceOf(PersistenceException.class);
        }
    }

    @Nested
    @DisplayName("abort")
    class AbortMethodTests {

        @Test
        @DisplayName("Does nothing when synchronization has not started")
        void should_DoNothing_when_SynchronizationHasNotStarted() {
            var repository = new RecordingWifiConfigurationRepository();
            var tracker = new SynchronizationTracker(repository);

            tracker.abort();

            assertThat(repository.deletedDates()).isEmpty();
        }

        @Test
        @DisplayName("Stops tracking active synchronization")
        void should_StopTrackingActiveSynchronization_when_SynchronizationIsInProgress() {
            var repository = new RecordingWifiConfigurationRepository();
            var tracker = new SynchronizationTracker(repository);

            tracker.start(SYNCHRONIZATION_DATE, 1);

            tracker.abort();
            tracker.complete(SYNCHRONIZATION_DATE);

            assertThat(repository.deletedDates()).isEmpty();
        }

        @Test
        @DisplayName("Allows synchronization to start again after abort")
        void should_AllowSynchronizationToStartAgain_when_SynchronizationWasAborted() {
            var repository = new RecordingWifiConfigurationRepository();
            var tracker = new SynchronizationTracker(repository);
            var date = SYNCHRONIZATION_DATE.minusDays(1);

            tracker.start(date, 1);
            tracker.abort();

            tracker.start(SYNCHRONIZATION_DATE, 1);
            tracker.complete(SYNCHRONIZATION_DATE);

            assertThat(repository.deletedDates())
                    .containsExactly(SYNCHRONIZATION_DATE);
        }
    }

    @NullMarked
    private static final class RecordingWifiConfigurationRepository implements WifiConfigurationRepository {

        private final List<LocalDate> deletedDates = new ArrayList<>();

        private boolean failOnDelete;

        @Override
        public Optional<WifiConfiguration> findByCpeId(String cpeId) {
            return Optional.empty();
        }

        @Override
        public void save(WifiConfiguration configuration, @Nullable LocalDate lastSynchronized) {}

        @Override
        public void deleteOlderThan(LocalDate lastSynchronized) {
            if (failOnDelete) {
                throw new PersistenceException(new RuntimeException());
            }
            deletedDates.add(lastSynchronized);
        }

        void failOnDelete() {
            failOnDelete = true;
        }

        List<LocalDate> deletedDates() {
            return deletedDates;
        }
    }
}
