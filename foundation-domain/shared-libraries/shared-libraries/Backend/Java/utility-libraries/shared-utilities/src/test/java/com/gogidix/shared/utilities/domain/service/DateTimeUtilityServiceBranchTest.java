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

class DateTimeUtilityServiceBranchTest {

    private final DateTimeUtilityService service = new DateTimeUtilityService();

    @Test
    void formatToIso_success() {
        LocalDateTime dt = LocalDateTime.of(2024, 6, 15, 14, 30, 0);
        UtilityResult<String> result = service.formatToIso("op-1", dt);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("2024-06-15T14:30:00", result.getResult());
    }

    @Test
    void parseFromIso_success() {
        UtilityResult<LocalDateTime> result = service.parseFromIso("op-1", "2024-06-15T14:30:00");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 6, 15, 14, 30, 0), result.getResult());
    }

    @Test
    void parseFromIso_invalid() {
        UtilityResult<LocalDateTime> result = service.parseFromIso("op-1", "not-a-date");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void formatWithPattern_success() {
        LocalDateTime dt = LocalDateTime.of(2024, 6, 15, 14, 30, 0);
        UtilityResult<String> result = service.formatWithPattern("op-1", dt, "yyyy-MM-dd");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("2024-06-15", result.getResult());
    }

    @Test
    void formatWithPattern_invalidPattern() {
        LocalDateTime dt = LocalDateTime.now();
        UtilityResult<String> result = service.formatWithPattern("op-1", dt, "INVALID{{");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void parseWithPattern_success() {
        UtilityResult<LocalDateTime> result = service.parseWithPattern("op-1", "15/06/2024 14:30", "dd/MM/yyyy HH:mm");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 6, 15, 14, 30, 0), result.getResult());
    }

    @Test
    void parseWithPattern_invalidPattern() {
        UtilityResult<LocalDateTime> result = service.parseWithPattern("op-1", "invalid", "INVALID{{");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void toDate_success() {
        LocalDateTime dt = LocalDateTime.of(2024, 6, 15, 14, 30, 0);
        UtilityResult<Date> result = service.toDate("op-1", dt);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getResult());
    }

    @Test
    void fromDate_success() {
        Date date = new Date();
        UtilityResult<LocalDateTime> result = service.fromDate("op-1", date);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getResult());
    }

    @Test
    void convertTimeZone_success() {
        ZonedDateTime utc = ZonedDateTime.of(2024, 6, 15, 14, 0, 0, 0, ZoneId.of("UTC"));
        UtilityResult<ZonedDateTime> result = service.convertTimeZone("op-1", utc, ZoneId.of("Africa/Lagos"));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(ZoneId.of("Africa/Lagos"), result.getResult().getZone());
    }

    @Test
    void calculateDifference_success() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 3, 12, 0);
        UtilityResult<Map<String, Long>> result = service.calculateDifference("op-1", start, end);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(2, result.getResult().get("days"));
        assertTrue(result.getResult().containsKey("hours"));
        assertTrue(result.getResult().containsKey("minutes"));
        assertTrue(result.getResult().containsKey("weeks"));
        assertTrue(result.getResult().containsKey("months"));
        assertTrue(result.getResult().containsKey("years"));
    }

    @Test
    void addTime_success() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        UtilityResult<LocalDateTime> result = service.addTime("op-1", start, 5, ChronoUnit.DAYS);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 1, 6, 0, 0), result.getResult());
    }

    @Test
    void subtractTime_success() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 6, 0, 0);
        UtilityResult<LocalDateTime> result = service.subtractTime("op-1", start, 5, ChronoUnit.DAYS);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), result.getResult());
    }

    @Test
    void isExpired_true() {
        UtilityResult<Boolean> result = service.isExpired("op-1", LocalDateTime.now().minusDays(1));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult());
    }

    @Test
    void isExpired_false() {
        UtilityResult<Boolean> result = service.isExpired("op-1", LocalDateTime.now().plusDays(1));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertFalse(result.getResult());
    }

    @Test
    void isValidDateRange_true() {
        UtilityResult<Boolean> result = service.isValidDateRange("op-1",
            LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 2, 0, 0));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult());
    }

    @Test
    void isValidDateRange_false_withWarnings() {
        UtilityResult<Boolean> result = service.isValidDateRange("op-1",
            LocalDateTime.of(2024, 1, 2, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0));
        assertEquals(ProcessingStatus.SUCCESS_WITH_WARNINGS, result.getStatus());
        assertFalse(result.getResult());
    }

    @Test
    void getDayBoundaries_success() {
        UtilityResult<Map<String, LocalDateTime>> result = service.getDayBoundaries("op-1", LocalDate.of(2024, 6, 15));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(LocalDateTime.of(2024, 6, 15, 0, 0), result.getResult().get("startOfDay"));
        assertNotNull(result.getResult().get("endOfDay"));
    }

    @Test
    void getBusinessDaysBetween_mondayToFriday() {
        LocalDate start = LocalDate.of(2024, 1, 8); // Monday
        LocalDate end = LocalDate.of(2024, 1, 12); // Friday
        UtilityResult<Long> result = service.getBusinessDaysBetween("op-1", start, end);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(5, result.getResult());
    }

    @Test
    void getBusinessDaysBetween_withWeekend() {
        LocalDate start = LocalDate.of(2024, 1, 6); // Saturday
        LocalDate end = LocalDate.of(2024, 1, 8); // Monday
        UtilityResult<Long> result = service.getBusinessDaysBetween("op-1", start, end);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(1, result.getResult());
    }

    @Test
    void addTime_hours() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        UtilityResult<LocalDateTime> result = service.addTime("op-1", start, 3, ChronoUnit.HOURS);
        assertEquals(LocalDateTime.of(2024, 1, 1, 13, 0), result.getResult());
    }

    @Test
    void subtractTime_minutes() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 30);
        UtilityResult<LocalDateTime> result = service.subtractTime("op-1", start, 15, ChronoUnit.MINUTES);
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 15), result.getResult());
    }
}
