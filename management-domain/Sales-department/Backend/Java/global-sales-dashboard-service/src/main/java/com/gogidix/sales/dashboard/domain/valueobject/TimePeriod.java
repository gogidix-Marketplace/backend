package com.gogidix.sales.dashboard.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Value Object for time periods
 * Represents various time periods for dashboard reporting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimePeriod {

    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType periodType;
    private Integer periodValue;
    private Integer year;
    private String label;
    private String code;

    public enum PeriodType {
        TODAY, YESTERDAY,
        THIS_WEEK, LAST_WEEK, WEEK_TO_DATE,
        THIS_MONTH, LAST_MONTH, MONTH_TO_DATE,
        THIS_QUARTER, LAST_QUARTER, QUARTER_TO_DATE,
        THIS_YEAR, LAST_YEAR, YEAR_TO_DATE,
        LAST_7_DAYS, LAST_30_DAYS, LAST_90_DAYS, LAST_365_DAYS,
        CUSTOM, ROLLING
    }

    /**
     * Creates a time period for a given type
     */
    public static TimePeriod forType(PeriodType periodType) {
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate = today;
        String label;
        String code;

        switch (periodType) {
            case TODAY:
                startDate = today;
                label = "Today";
                code = "TODAY";
                break;
            case YESTERDAY:
                startDate = today.minusDays(1);
                endDate = today.minusDays(1);
                label = "Yesterday";
                code = "YESTERDAY";
                break;
            case THIS_WEEK:
                startDate = today.minusDays(today.getDayOfWeek().getValue() - 1);
                label = "This Week";
                code = "THIS_WEEK";
                break;
            case LAST_WEEK:
                startDate = today.minusWeeks(1).minusDays(today.getDayOfWeek().getValue() - 1);
                endDate = startDate.plusDays(6);
                label = "Last Week";
                code = "LAST_WEEK";
                break;
            case WEEK_TO_DATE:
                startDate = today.minusDays(today.getDayOfWeek().getValue() - 1);
                label = "Week to Date";
                code = "WTD";
                break;
            case THIS_MONTH:
                startDate = LocalDate.of(today.getYear(), today.getMonth(), 1);
                label = "This Month";
                code = "THIS_MONTH";
                break;
            case LAST_MONTH:
                startDate = LocalDate.of(today.minusMonths(1).getYear(),
                        today.minusMonths(1).getMonth(), 1);
                endDate = startDate.plusMonths(1).minusDays(1);
                label = "Last Month";
                code = "LAST_MONTH";
                break;
            case MONTH_TO_DATE:
                startDate = LocalDate.of(today.getYear(), today.getMonth(), 1);
                label = "Month to Date";
                code = "MTD";
                break;
            case THIS_QUARTER:
                int currentMonth = today.getMonthValue();
                int quarterStartMonth = ((currentMonth - 1) / 3) * 3 + 1;
                startDate = LocalDate.of(today.getYear(), quarterStartMonth, 1);
                label = "Q" + ((quarterStartMonth - 1) / 3 + 1) + " " + today.getYear();
                code = "Q" + ((quarterStartMonth - 1) / 3 + 1);
                break;
            case LAST_QUARTER:
                LocalDate lastQuarterDate = today.minusMonths(3);
                int lastQuarterMonth = lastQuarterDate.getMonthValue();
                int lastQuarterStartMonth = ((lastQuarterMonth - 1) / 3) * 3 + 1;
                startDate = LocalDate.of(lastQuarterDate.getYear(), lastQuarterStartMonth, 1);
                endDate = startDate.plusMonths(3).minusDays(1);
                label = "Q" + ((lastQuarterStartMonth - 1) / 3 + 1) + " " + lastQuarterDate.getYear();
                code = "LAST_Q";
                break;
            case QUARTER_TO_DATE:
                int qtdMonth = today.getMonthValue();
                int qtdStartMonth = ((qtdMonth - 1) / 3) * 3 + 1;
                startDate = LocalDate.of(today.getYear(), qtdStartMonth, 1);
                label = "Quarter to Date";
                code = "QTD";
                break;
            case THIS_YEAR:
                startDate = LocalDate.of(today.getYear(), 1, 1);
                label = "This Year";
                code = "THIS_YEAR";
                break;
            case LAST_YEAR:
                startDate = LocalDate.of(today.getYear() - 1, 1, 1);
                endDate = LocalDate.of(today.getYear() - 1, 12, 31);
                label = "Last Year";
                code = "LAST_YEAR";
                break;
            case YEAR_TO_DATE:
                startDate = LocalDate.of(today.getYear(), 1, 1);
                label = "Year to Date";
                code = "YTD";
                break;
            case LAST_7_DAYS:
                startDate = today.minusDays(7);
                label = "Last 7 Days";
                code = "L7D";
                break;
            case LAST_30_DAYS:
                startDate = today.minusDays(30);
                label = "Last 30 Days";
                code = "L30D";
                break;
            case LAST_90_DAYS:
                startDate = today.minusDays(90);
                label = "Last 90 Days";
                code = "L90D";
                break;
            case LAST_365_DAYS:
                startDate = today.minusDays(365);
                label = "Last 365 Days";
                code = "L365D";
                break;
            default:
                startDate = today;
                label = "Custom";
                code = "CUSTOM";
        }

        return TimePeriod.builder()
                .startDate(startDate)
                .endDate(endDate)
                .periodType(periodType)
                .label(label)
                .code(code)
                .build();
    }

    /**
     * Creates a custom time period
     */
    public static TimePeriod custom(LocalDate startDate, LocalDate endDate) {
        return TimePeriod.builder()
                .startDate(startDate)
                .endDate(endDate)
                .periodType(PeriodType.CUSTOM)
                .label(formatDateRange(startDate, endDate))
                .code("CUSTOM")
                .build();
    }

    /**
     * Creates a rolling time period
     */
    public static TimePeriod rolling(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);

        return TimePeriod.builder()
                .startDate(startDate)
                .endDate(endDate)
                .periodType(PeriodType.ROLLING)
                .periodValue(days)
                .label("Rolling " + days + " Days")
                .code("R" + days + "D")
                .build();
    }

    /**
     * Gets the previous period for comparison
     */
    public TimePeriod getPreviousPeriod() {
        long daysBetween = ChronoUnit.DAYS.between(this.startDate, this.endDate) + 1;

        return TimePeriod.builder()
                .startDate(this.startDate.minusDays(daysBetween))
                .endDate(this.startDate.minusDays(1))
                .periodType(this.periodType)
                .label("Previous " + this.label)
                .code("PREV_" + this.code)
                .build();
    }

    /**
     * Gets the same period from previous year
     */
    public TimePeriod getSamePeriodLastYear() {
        return TimePeriod.builder()
                .startDate(this.startDate.minusYears(1))
                .endDate(this.endDate.minusYears(1))
                .periodType(this.periodType)
                .label(this.label + " " + (this.year != null ? this.year - 1 : this.startDate.getYear() - 1))
                .code("PY_" + this.code)
                .build();
    }

    /**
     * Checks if date is within period
     */
    public boolean contains(LocalDate date) {
        return !date.isBefore(this.startDate) && !date.isAfter(this.endDate);
    }

    /**
     * Gets the number of days in period
     */
    public long getDays() {
        return ChronoUnit.DAYS.between(this.startDate, this.endDate) + 1;
    }

    /**
     * Gets the number of weeks in period
     */
    public long getWeeks() {
        return getDays() / 7;
    }

    /**
     * Gets the number of months in period
     */
    public long getMonths() {
        return ChronoUnit.MONTHS.between(
                this.startDate.withDayOfMonth(1),
                this.endDate.withDayOfMonth(1).plusMonths(1)
        );
    }

    /**
     * Gets quarter
     */
    public Integer getQuarter() {
        if (this.startDate == null) {
            return null;
        }
        int month = this.startDate.getMonthValue();
        return (month - 1) / 3 + 1;
    }

    /**
     * Checks if period is current
     */
    public boolean isCurrent() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(this.startDate) && !today.isAfter(this.endDate);
    }

    /**
     * Formats date range as string
     */
    private static String formatDateRange(LocalDate start, LocalDate end) {
        if (start.equals(end)) {
            return start.toString();
        }
        if (start.getYear() == end.getYear()) {
            if (start.getMonth() == end.getMonth()) {
                return start.getMonth().name().substring(0, 3) + " " +
                        start.getDayOfMonth() + "-" + end.getDayOfMonth() + ", " + start.getYear();
            }
            return start.getMonth().name().substring(0, 3) + " " + start.getDayOfMonth() +
                    " - " + end.getMonth().name().substring(0, 3) + " " + end.getDayOfMonth() +
                    ", " + start.getYear();
        }
        return startDateToString(start) + " - " + startDateToString(end);
    }

    private static String startDateToString(LocalDate date) {
        return date.getMonth().name().substring(0, 3) + " " + date.getDayOfMonth() + ", " + date.getYear();
    }

    /**
     * Gets standard reporting periods
     */
    public static List<TimePeriod> getStandardPeriods() {
        return List.of(
                forType(PeriodType.TODAY),
                forType(PeriodType.WEEK_TO_DATE),
                forType(PeriodType.MONTH_TO_DATE),
                forType(PeriodType.QUARTER_TO_DATE),
                forType(PeriodType.YEAR_TO_DATE),
                forType(PeriodType.LAST_7_DAYS),
                forType(PeriodType.LAST_30_DAYS),
                forType(PeriodType.LAST_90_DAYS)
        );
    }
}
