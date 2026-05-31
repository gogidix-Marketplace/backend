package com.gogidix.aiservices.aifrauddetectionservice.shared.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Utility class for date and time operations.
 * <p>
 * Provides methods for working with UTC timestamps, formatting dates,
 * and parsing date strings in various formats.
 */
@Slf4j
public final class DateUtil {

    private static final DateUtil INSTANCE = new DateUtil();

    public static DateUtil get() {
        return INSTANCE;
    }

    /**
     * ISO 8601 date-time formatter with milliseconds and UTC timezone.
     */
    public static final DateTimeFormatter ISO8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /**
     * Simple date formatter (yyyy-MM-dd).
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Date-time formatter without timezone (yyyy-MM-dd HH:mm:ss).
     */
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * UTC timezone ID.
     */
    public static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    /**
     * Gets the current timestamp in UTC.
     *
     * @return the current Instant in UTC
     */
    public Instant nowUtc() {
        return Instant.now();
    }

    /**
     * Gets the current LocalDateTime in UTC.
     *
     * @return the current LocalDateTime in UTC
     */
    public LocalDateTime nowUtcLocal() {
        return LocalDateTime.now(UTC_ZONE);
    }

    /**
     * Gets the current time as a Date in UTC.
     *
     * @return the current Date
     */
    public Date nowUtcAsDate() {
        return Date.from(Instant.now());
    }

    /**
     * Formats an Instant to ISO 8601 string in UTC.
     *
     * @param instant the instant to format
     * @return the formatted ISO 8601 string
     */
    public String formatIso8601(Instant instant) {
        if (instant == null) {
            return null;
        }
        return ISO8601_FORMATTER.format(ZonedDateTime.ofInstant(instant, UTC_ZONE));
    }

    /**
     * Formats a LocalDateTime to ISO 8601 string in UTC.
     *
     * @param localDateTime the LocalDateTime to format
     * @return the formatted ISO 8601 string
     */
    public String formatIso8601(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return ISO8601_FORMATTER.format(localDateTime.atZone(UTC_ZONE));
    }

    /**
     * Formats an Instant to a simple date string (yyyy-MM-dd).
     *
     * @param instant the instant to format
     * @return the formatted date string
     */
    public String formatDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        return DATE_FORMATTER.format(ZonedDateTime.ofInstant(instant, UTC_ZONE));
    }

    /**
     * Formats an Instant to a date-time string (yyyy-MM-dd HH:mm:ss).
     *
     * @param instant the instant to format
     * @return the formatted date-time string
     */
    public String formatDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return DATETIME_FORMATTER.format(ZonedDateTime.ofInstant(instant, UTC_ZONE));
    }

    /**
     * Parses an ISO 8601 string to an Instant.
     *
     * @param isoString the ISO 8601 string to parse
     * @return the parsed Instant, or null if parsing fails
     */
    public Instant parseIso8601(String isoString) {
        if (isoString == null || isoString.isBlank()) {
            return null;
        }
        try {
            return Instant.parse(isoString);
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse ISO 8601 date: {}", isoString);
            return null;
        }
    }

    /**
     * Parses a date string (yyyy-MM-dd) to an Instant at start of day in UTC.
     *
     * @param dateString the date string to parse
     * @return the parsed Instant, or null if parsing fails
     */
    public Instant parseDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
            return date.atStartOfDay(UTC_ZONE).toInstant();
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse date string: {}", dateString);
            return null;
        }
    }

    /**
     * Parses a date-time string (yyyy-MM-dd HH:mm:ss) to an Instant in UTC.
     *
     * @param dateTimeString the date-time string to parse
     * @return the parsed Instant, or null if parsing fails
     */
    public Instant parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isBlank()) {
            return null;
        }
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
            return dateTime.atZone(UTC_ZONE).toInstant();
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse date-time string: {}", dateTimeString);
            return null;
        }
    }

    /**
     * Converts a Date to an Instant.
     *
     * @param date the Date to convert
     * @return the converted Instant, or null if date is null
     */
    public Instant toInstant(Date date) {
        return date != null ? date.toInstant() : null;
    }

    /**
     * Converts an Instant to a Date.
     *
     * @param instant the Instant to convert
     * @return the converted Date, or null if instant is null
     */
    public Date fromDate(Instant instant) {
        return instant != null ? Date.from(instant) : null;
    }

    /**
     * Adds the specified number of seconds to an Instant.
     *
     * @param instant  the base instant
     * @param seconds  the number of seconds to add
     * @return the new Instant, or null if instant is null
     */
    public Instant addSeconds(Instant instant, long seconds) {
        return instant != null ? instant.plusSeconds(seconds) : null;
    }

    /**
     * Adds the specified number of minutes to an Instant.
     *
     * @param instant  the base instant
     * @param minutes  the number of minutes to add
     * @return the new Instant, or null if instant is null
     */
    public Instant addMinutes(Instant instant, long minutes) {
        return instant != null ? instant.plusSeconds(minutes * 60) : null;
    }

    /**
     * Adds the specified number of hours to an Instant.
     *
     * @param instant  the base instant
     * @param hours    the number of hours to add
     * @return the new Instant, or null if instant is null
     */
    public Instant addHours(Instant instant, long hours) {
        return instant != null ? instant.plusSeconds(hours * 3600) : null;
    }

    /**
     * Adds the specified number of days to an Instant.
     *
     * @param instant  the base instant
     * @param days     the number of days to add
     * @return the new Instant, or null if instant is null
     */
    public Instant addDays(Instant instant, long days) {
        return instant != null ? instant.plusSeconds(days * 86400) : null;
    }

    /**
     * Checks if an Instant is between two other Instants (inclusive).
     *
     * @param instant   the instant to check
     * @param start     the start of the range
     * @param end       the end of the range
     * @return true if instant is between start and end, false otherwise
     */
    public boolean isBetween(Instant instant, Instant start, Instant end) {
        if (instant == null || start == null || end == null) {
            return false;
        }
        return !instant.isBefore(start) && !instant.isAfter(end);
    }

    /**
     * Calculates the duration between two instants in milliseconds.
     *
     * @param start the start instant
     * @param end   the end instant
     * @return the duration in milliseconds, or 0 if either instant is null
     */
    public long durationMillis(Instant start, Instant end) {
        if (start == null || end == null) {
            return 0;
        }
        return java.time.Duration.between(start, end).toMillis();
    }

    /**
     * Calculates the duration between two instants in seconds.
     *
     * @param start the start instant
     * @param end   the end instant
     * @return the duration in seconds, or 0 if either instant is null
     */
    public long durationSeconds(Instant start, Instant end) {
        if (start == null || end == null) {
            return 0;
        }
        return java.time.Duration.between(start, end).getSeconds();
    }

    /**
     * Checks if an Instant is in the past.
     *
     * @param instant the instant to check
     * @return true if the instant is in the past, false otherwise
     */
    public boolean isPast(Instant instant) {
        return instant != null && instant.isBefore(Instant.now());
    }

    /**
     * Checks if an Instant is in the future.
     *
     * @param instant the instant to check
     * @return true if the instant is in the future, false otherwise
     */
    public boolean isFuture(Instant instant) {
        return instant != null && instant.isAfter(Instant.now());
    }

    /**
     * Gets the epoch milliseconds from an Instant.
     *
     * @param instant the instant
     * @return the epoch milliseconds, or 0 if instant is null
     */
    public long toEpochMillis(Instant instant) {
        return instant != null ? instant.toEpochMilli() : 0;
    }

    /**
     * Creates an Instant from epoch milliseconds.
     *
     * @param epochMillis the epoch milliseconds
     * @return the Instant
     */
    public Instant fromEpochMillis(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis);
    }
}
