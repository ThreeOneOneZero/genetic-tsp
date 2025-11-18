package com.yourproject.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final ZoneId UTC = ZoneId.of("UTC");
    private DateUtil() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static String now() {
        return Instant.now().toString();
    }

    public static String toIso(Instant instant) {
        return instant.toString();
    }

    public static Instant parseIso(String dateString) {
        return Instant.parse(dateString);
    }

    public static LocalDateTime nowLocal() {
        return LocalDateTime.now(UTC);
    }

    public static String formatUTC(LocalDateTime dateTime) {
        ZonedDateTime zdt = dateTime.atZone(UTC);
        return zdt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
