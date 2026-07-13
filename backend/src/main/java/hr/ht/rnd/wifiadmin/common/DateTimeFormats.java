package hr.ht.rnd.wifiadmin.common;

import java.time.format.DateTimeFormatter;

public final class DateTimeFormats {

    public static final DateTimeFormatter LONG =
            DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy 'at' HH:mm:ss z");

    private DateTimeFormats() {}
}
