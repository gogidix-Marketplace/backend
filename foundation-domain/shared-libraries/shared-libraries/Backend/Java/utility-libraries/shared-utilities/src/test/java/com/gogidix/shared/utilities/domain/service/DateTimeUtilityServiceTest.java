package com.gogidix.shared.utilities.domain.service;

import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.valueobject.ProcessingStatus;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

class DateTimeUtilityServiceTest {

    private final DateTimeUtilityService service = new DateTimeUtilityService();

    @Test
    void formatToIso() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        UtilityResult<String> result = service.formatToIso("op-1", now);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("2024-01-15T10:30:00", result.getResult());
    }

    @Test
    void parseFromIso() {
        UtilityResult<LocalDateTime> result = service.parseFromIso("op-1", "2024-01-15T10:30:00");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30, 0), result.getResult());
    }

    @Test
    void parseFromIsoInvalid() {
        UtilityResult<LocalDateTime> result = service.parseFromIso("op-1", "invalid-date");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
        assertNull(result.getResult());
    }

    @Test
    void formatWithPattern() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        UtilityResult<String> result = service.formatWithPattern("op-1", now, "dd/MM/yyyy HH:mm");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("15/01/2024 10:30", result.getResult());
    }

    @Test
    void formatWithInvalidPattern() {
        LocalDateTime now = LocalDateTime.now();
        UtilityResult<String> result = service.formatWithPattern("op-1", now, "INVALID{{");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void parseWithPattern() {
        UtilityResult<LocalDateTime> result = service.parseWithPattern("op-1", "15/01/2024 10:30", "dd/MM/yyyy HH:mm");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30, 0), result.getResult());
    }

    @Test
    void parseWithInvalidPattern() {
        UtilityResult<LocalDateTime> result = service.parseWithPattern("op-1", "invalid", "INVALID{{");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void toDate() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        UtilityResult<Date> result = service.toDate("op-1", now);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getResult());
    }

    @Test
    void fromDate() {
        Date date = new Date();
        UtilityResult<LocalDateTime> result = service.fromDate("op-1", date);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getResult());
    }

    @Test
    void convertTimeZone() {
        ZonedDateTime utc = ZonedDateTime.of(2024, 1, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
        UtilityResult<ZonedDateTime> result = service.convertTimeZone("op-1", utc, ZoneId.of("Africa/Lagos"));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(ZoneId.of("Africa/Lagos"), result.getResult().getZone());
    }

    @Test
    void calculateDifference() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 3, 12, 0);
        UtilityResult<Map<String, Long>> result = service.calculateDifference("op-1", start, end);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(2, result.getResult().get("days"));
        assertTrue(result.getResult().containsKey("hours"));
        assertTrue(result.getResult().containsKey("minutes"));
    }

    @Test
    void addTime() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        UtilityResult<LocalDateTime> result = service.addTime("op-1", start, 5, ChronoUnit.DAYS);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 1, 6, 0, 0), result.getResult());
    }

    @Test
    void subtractTime() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 6, 0, 0);
        UtilityResult<LocalDateTime> result = service.subtractTime("op-1", start, 5, ChronoUnit.DAYS);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), result.getResult());
    }

    @Test
    void isExpiredTrue() {
        UtilityResult<Boolean> result = service.isExpired("op-1", LocalDateTime.now().minusDays(1));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult());
    }

    @Test
    void isExpiredFalse() {
        UtilityResult<Boolean> result = service.isExpired("op-1", LocalDateTime.now().plusDays(1));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertFalse(result.getResult());
    }

    @Test
    void isValidDateRangeTrue() {
        UtilityResult<Boolean> result = service.isValidDateRange("op-1",
            LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 2, 0, 0));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult());
    }

    @Test
    void isValidDateRangeFalse() {
        UtilityResult<Boolean> result = service.isValidDateRange("op-1",
            LocalDateTime.of(2024, 1, 2, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0));
        assertEquals(ProcessingStatus.SUCCESS_WITH_WARNINGS, result.getStatus());
        assertFalse(result.getResult());
    }

    @Test
    void getDayBoundaries() {
        UtilityResult<Map<String, LocalDateTime>> result = service.getDayBoundaries("op-1", LocalDate.of(2024, 1, 15));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 1, 15, 0, 0), result.getResult().get("startOfDay"));
        assertNotNull(result.getResult().get("endOfDay"));
    }

    @Test
    void getBusinessDaysBetween() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 7);
        UtilityResult<Long> result = service.getBusinessDaysBetween("op-1", start, end);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult() >= 5);
    }
}
