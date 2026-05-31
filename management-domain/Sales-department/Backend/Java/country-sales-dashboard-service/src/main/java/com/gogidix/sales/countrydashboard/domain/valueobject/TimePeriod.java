package com.gogidix.sales.countrydashboard.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;

/**
 * Time Period Value Object
 * Represents a period for sales comparison and analysis
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimePeriod {

    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType type;

    public enum PeriodType {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY, CUSTOM
    }

    public static TimePeriod of(PeriodType type, int periodsBack) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (type) {
            case DAILY -> endDate.minusDays(periodsBack);
            case WEEKLY -> endDate.minusWeeks(periodsBack);
            case MONTHLY -> endDate.minusMonths(periodsBack);
            case QUARTERLY -> endDate.minusMonths(periodsBack * 3L);
            case YEARLY -> endDate.minusYears(periodsBack);
            case CUSTOM -> throw new IllegalArgumentException("Use of() with specific dates for CUSTOM periods");
        };
        return new TimePeriod(startDate, endDate, type);
    }

    public static TimePeriod currentMonth() {
        LocalDate now = LocalDate.now();
        return new TimePeriod(
                now.withDayOfMonth(1),
                now,
                PeriodType.MONTHLY
        );
    }

    public static TimePeriod currentQuarter() {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int quarterStartMonth = ((currentMonth - 1) / 3) * 3 + 1;
        return new TimePeriod(
                now.withMonth(quarterStartMonth).withDayOfMonth(1),
                now,
                PeriodType.QUARTERLY
        );
    }

    public static TimePeriod currentYear() {
        LocalDate now = LocalDate.now();
        return new TimePeriod(
                now.withDayOfYear(1),
                now,
                PeriodType.YEARLY
        );
    }

    public static TimePeriod lastMonth() {
        LocalDate now = LocalDate.now();
        YearMonth lastYearMonth = YearMonth.from(now).minusMonths(1);
        return new TimePeriod(
                lastYearMonth.atDay(1),
                lastYearMonth.atEndOfMonth(),
                PeriodType.MONTHLY
        );
    }

    public static TimePeriod lastQuarter() {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int lastQuarterStartMonth = ((currentMonth - 1) / 3) * 3 - 2;
        int yearAdjust = lastQuarterStartMonth < 1 ? -1 : 0;
        if (lastQuarterStartMonth < 1) lastQuarterStartMonth += 12;

        LocalDate start = now.minusYears(yearAdjust).withMonth(lastQuarterStartMonth).withDayOfMonth(1);
        LocalDate end = start.plusMonths(2).withDayOfMonth(start.plusMonths(2).lengthOfMonth());

        return new TimePeriod(start, end, PeriodType.QUARTERLY);
    }

    public static TimePeriod lastYear() {
        LocalDate now = LocalDate.now();
        return new TimePeriod(
                now.minusYears(1).withDayOfYear(1),
                now.minusYears(1).withMonth(12).withDayOfMonth(31),
                PeriodType.YEARLY
        );
    }

    public static TimePeriod custom(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        return new TimePeriod(startDate, endDate, PeriodType.CUSTOM);
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public long getDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public long getMonths() {
        return java.time.temporal.ChronoUnit.MONTHS.between(startDate, endDate) + 1;
    }

    public TimePeriod previousSamePeriod() {
        return switch (type) {
            case DAILY -> new TimePeriod(startDate.minusYears(1), endDate.minusYears(1), type);
            case WEEKLY -> new TimePeriod(startDate.minusWeeks(1), endDate.minusWeeks(1), type);
            case MONTHLY -> new TimePeriod(startDate.minusMonths(1), endDate.minusMonths(1), type);
            case QUARTERLY -> new TimePeriod(startDate.minusMonths(3), endDate.minusMonths(3), type);
            case YEARLY -> new TimePeriod(startDate.minusYears(1), endDate.minusYears(1), type);
            case CUSTOM -> new TimePeriod(startDate.minusYears(1), endDate.minusYears(1), type);
        };
    }
}
