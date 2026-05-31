package com.gogidix.shared.utilities.domain.service;

import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Domain service for date and time utility operations
 * Provides comprehensive date/time processing capabilities
 */
@Slf4j
public class DateTimeUtilityService {

    /**
     * Format date/time to ISO string
     */
    public UtilityResult<String> formatToIso(String operationId, LocalDateTime dateTime) {
        try {
            String result = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            log.debug("Formatted date {} to ISO string: {}", dateTime, result);
            return UtilityResult.success(operationId, UtilityType.DATETIME_FORMATTING, result);
        } catch (Exception e) {
            log.error("Failed to format date {} to ISO string", dateTime, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_FORMATTING, e.getMessage());
        }
    }

    /**
     * Parse date/time from ISO string
     */
    public UtilityResult<LocalDateTime> parseFromIso(String operationId, String isoString) {
        try {
            LocalDateTime result = LocalDateTime.parse(isoString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            log.debug("Parsed ISO string {} to date: {}", isoString, result);
            return UtilityResult.success(operationId, UtilityType.DATETIME_PARSING, result);
        } catch (DateTimeParseException e) {
            log.error("Failed to parse ISO string {} to date", isoString, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_PARSING, e.getMessage());
        }
    }

    /**
     * Format date/time with custom pattern
     */
    public UtilityResult<String> formatWithPattern(String operationId, LocalDateTime dateTime, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String result = dateTime.format(formatter);
            log.debug("Formatted date {} with pattern {} to: {}", dateTime, pattern, result);
            return UtilityResult.success(operationId, UtilityType.DATETIME_FORMATTING, result);
        } catch (Exception e) {
            log.error("Failed to format date {} with pattern {}", dateTime, pattern, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_FORMATTING, e.getMessage());
        }
    }

    /**
     * Parse date/time with custom pattern
     */
    public UtilityResult<LocalDateTime> parseWithPattern(String operationId, String dateString, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime result = LocalDateTime.parse(dateString, formatter);
            log.debug("Parsed string {} with pattern {} to: {}", dateString, pattern, result);
            return UtilityResult.success(operationId, UtilityType.DATETIME_PARSING, result);
        } catch (Exception e) {
            log.error("Failed to parse string {} with pattern {}", dateString, pattern, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_PARSING, e.getMessage());
        }
    }

    /**
     * Convert LocalDateTime to Date
     */
    public UtilityResult<Date> toDate(String operationId, LocalDateTime localDateTime) {
        try {
            Date result = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            return UtilityResult.success(operationId, UtilityType.DATETIME_CONVERSION, result);
        } catch (Exception e) {
            log.error("Failed to convert LocalDateTime {} to Date", localDateTime, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_CONVERSION, e.getMessage());
        }
    }

    /**
     * Convert Date to LocalDateTime
     */
    public UtilityResult<LocalDateTime> fromDate(String operationId, Date date) {
        try {
            LocalDateTime result = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return UtilityResult.success(operationId, UtilityType.DATETIME_CONVERSION, result);
        } catch (Exception e) {
            log.error("Failed to convert Date {} to LocalDateTime", date, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_CONVERSION, e.getMessage());
        }
    }

    /**
     * Convert between time zones
     */
    public UtilityResult<ZonedDateTime> convertTimeZone(String operationId, ZonedDateTime dateTime, ZoneId targetZone) {
        try {
            ZonedDateTime result = dateTime.withZoneSameInstant(targetZone);
            log.debug("Converted {} to timezone {}: {}", dateTime, targetZone, result);
            return UtilityResult.success(operationId, UtilityType.TIMEZONE_CONVERSION, result);
        } catch (Exception e) {
            log.error("Failed to convert {} to timezone {}", dateTime, targetZone, e);
            return UtilityResult.failure(operationId, UtilityType.TIMEZONE_CONVERSION, e.getMessage());
        }
    }

    /**
     * Calculate difference between two dates
     */
    public UtilityResult<Map<String, Long>> calculateDifference(String operationId, LocalDateTime start, LocalDateTime end) {
        try {
            Map<String, Long> result = new HashMap<>();
            result.put("seconds", ChronoUnit.SECONDS.between(start, end));
            result.put("minutes", ChronoUnit.MINUTES.between(start, end));
            result.put("hours", ChronoUnit.HOURS.between(start, end));
            result.put("days", ChronoUnit.DAYS.between(start, end));
            result.put("weeks", ChronoUnit.WEEKS.between(start, end));
            result.put("months", ChronoUnit.MONTHS.between(start, end));
            result.put("years", ChronoUnit.YEARS.between(start, end));
            
            log.debug("Calculated difference between {} and {}: {} days", start, end, result.get("days"));
            return UtilityResult.success(operationId, UtilityType.DATETIME_CALCULATION, result);
        } catch (Exception e) {
            log.error("Failed to calculate difference between {} and {}", start, end, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_CALCULATION, e.getMessage());
        }
    }

    /**
     * Add time to date
     */
    public UtilityResult<LocalDateTime> addTime(String operationId, LocalDateTime dateTime, long amount, ChronoUnit unit) {
        try {
            LocalDateTime result = dateTime.plus(amount, unit);
            log.debug("Added {} {} to {}: {}", amount, unit, dateTime, result);
            return UtilityResult.success(operationId, UtilityType.DATETIME_CALCULATION, result);
        } catch (Exception e) {
            log.error("Failed to add {} {} to {}", amount, unit, dateTime, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_CALCULATION, e.getMessage());
        }
    }

    /**
     * Subtract time from date
     */
    public UtilityResult<LocalDateTime> subtractTime(String operationId, LocalDateTime dateTime, long amount, ChronoUnit unit) {
        try {
            LocalDateTime result = dateTime.minus(amount, unit);
            log.debug("Subtracted {} {} from {}: {}", amount, unit, dateTime, result);
            return UtilityResult.success(operationId, UtilityType.DATETIME_CALCULATION, result);
        } catch (Exception e) {
            log.error("Failed to subtract {} {} from {}", amount, unit, dateTime, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_CALCULATION, e.getMessage());
        }
    }

    /**
     * Check if date is expired (before now)
     */
    public UtilityResult<Boolean> isExpired(String operationId, LocalDateTime dateTime) {
        try {
            boolean result = dateTime.isBefore(LocalDateTime.now());
            return UtilityResult.success(operationId, UtilityType.DATETIME_VALIDATION, result);
        } catch (Exception e) {
            log.error("Failed to check if date {} is expired", dateTime, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_VALIDATION, e.getMessage());
        }
    }

    /**
     * Validate date range
     */
    public UtilityResult<Boolean> isValidDateRange(String operationId, LocalDateTime start, LocalDateTime end) {
        try {
            boolean result = start.isBefore(end);
            List<String> warnings = new ArrayList<>();
            if (!result) {
                warnings.add("Start date is not before end date");
            }
            
            if (warnings.isEmpty()) {
                return UtilityResult.success(operationId, UtilityType.DATETIME_VALIDATION, result);
            } else {
                return UtilityResult.withWarnings(operationId, UtilityType.DATETIME_VALIDATION, result, warnings);
            }
        } catch (Exception e) {
            log.error("Failed to validate date range {} to {}", start, end, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_VALIDATION, e.getMessage());
        }
    }

    /**
     * Get start and end of day
     */
    public UtilityResult<Map<String, LocalDateTime>> getDayBoundaries(String operationId, LocalDate date) {
        try {
            Map<String, LocalDateTime> result = new HashMap<>();
            result.put("startOfDay", date.atStartOfDay());
            result.put("endOfDay", date.atTime(LocalTime.MAX));
            return UtilityResult.success(operationId, UtilityType.DATETIME_CALCULATION, result);
        } catch (Exception e) {
            log.error("Failed to get day boundaries for {}", date, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_CALCULATION, e.getMessage());
        }
    }

    /**
     * Get business days between dates (excluding weekends)
     */
    public UtilityResult<Long> getBusinessDaysBetween(String operationId, LocalDate start, LocalDate end) {
        try {
            long businessDays = 0;
            LocalDate current = start;
            
            while (!current.isAfter(end)) {
                DayOfWeek dayOfWeek = current.getDayOfWeek();
                if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                    businessDays++;
                }
                current = current.plusDays(1);
            }
            
            log.debug("Calculated {} business days between {} and {}", businessDays, start, end);
            return UtilityResult.success(operationId, UtilityType.DATETIME_CALCULATION, businessDays);
        } catch (Exception e) {
            log.error("Failed to calculate business days between {} and {}", start, end, e);
            return UtilityResult.failure(operationId, UtilityType.DATETIME_CALCULATION, e.getMessage());
        }
    }
}