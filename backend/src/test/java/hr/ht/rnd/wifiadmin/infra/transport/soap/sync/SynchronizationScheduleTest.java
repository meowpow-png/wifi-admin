package hr.ht.rnd.wifiadmin.infra.transport.soap.sync;

import hr.ht.rnd.wifiadmin.infra.app.TestClock;
import hr.ht.rnd.wifiadmin.infra.transport.soap.TestPlatformProperties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SynchronizationScheduleTest {

    private static final LocalTime SYNC_SCHEDULE = LocalTime.of(14, 30);

    @Nested
    @DisplayName("nextExecution")
    class NextExecutionMethodTests {

        private final TestClock clock = TestClock.create();

        @Test
        @DisplayName("Returns today's scheduled time when it is still in the future")
        void should_ReturnTodayScheduledTime_when_ScheduleIsStillInFuture() {
            var schedule = scheduleAt(clock);

            clock.setTime(SYNC_SCHEDULE)
                    .rewind(Duration.ofMinutes(1));

            var expectedExecution = nextExecution(Duration.ZERO);
            var nextExecution = schedule.nextExecution();

            assertThat(nextExecution).isEqualTo(expectedExecution);
        }

        @Test
        @DisplayName("Returns tomorrow's scheduled time when today's time has passed")
        void should_ReturnTomorrowScheduledTime_when_TodayScheduleHasPassed() {
            var schedule = scheduleAt(clock);

            clock.setTime(SYNC_SCHEDULE)
                    .advance(Duration.ofMinutes(1));

            var expectedNextExecution = nextExecution(Duration.ofDays(1));
            var nextExecution = schedule.nextExecution();

            assertThat(nextExecution).isEqualTo(expectedNextExecution);
        }

        @Test
        @DisplayName("Returns tomorrow's scheduled time when today's time is now")
        void should_ReturnTomorrowScheduledTime_when_TodayScheduleIsNow() {
            var schedule = scheduleAt(clock);

            clock.setTime(SYNC_SCHEDULE);

            var expectedNextExecution = nextExecution(Duration.ofDays(1));
            var nextExecution = schedule.nextExecution();

            assertThat(nextExecution).isEqualTo(expectedNextExecution);
        }

        private ZonedDateTime nextExecution(Duration in) {
            return clock.instant()
                    .atZone(clock.getZone())
                    .with(SYNC_SCHEDULE)
                    .plus(in);
        }
    }

    private static SynchronizationSchedule scheduleAt(TestClock clock) {
        var properties = TestPlatformProperties.builder()
                .withSyncSchedule(SYNC_SCHEDULE)
                .build();

        return new SynchronizationSchedule(properties, clock);
    }
}
