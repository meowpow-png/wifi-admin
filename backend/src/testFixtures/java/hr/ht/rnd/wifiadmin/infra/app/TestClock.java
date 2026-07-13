package hr.ht.rnd.wifiadmin.infra.app;

import java.time.*;

/**
 * Mutable clock fixture for controlling time in tests.
 */
public final class TestClock extends Clock {

    private final ZoneId zoneId;
    private Instant instant;

    private TestClock(Instant instant, ZoneId zoneId) {
        this.instant = instant;
        this.zoneId = zoneId;
    }

    public static TestClock create(Instant instant) {
        return new TestClock(instant, ZoneOffset.UTC);
    }

    public static TestClock create() {
        return create(Instant.parse("2025-01-01T00:00:00Z"));
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new TestClock(instant, zone);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    public TestClock advance(Duration duration) {
        instant = instant.plus(duration);
        return this;
    }

    public TestClock rewind(Duration duration) {
        instant = instant.minus(duration);
        return this;
    }

    public TestClock setTime(LocalTime time) {
        var zonedDateTime = instant.atZone(zoneId)
                .withHour(time.getHour())
                .withMinute(time.getMinute())
                .withSecond(time.getSecond())
                .withNano(time.getNano());

        instant = zonedDateTime.toInstant();
        return this;
    }

    public LocalDate localDate() {
        return LocalDate.now(this);
    }
}
