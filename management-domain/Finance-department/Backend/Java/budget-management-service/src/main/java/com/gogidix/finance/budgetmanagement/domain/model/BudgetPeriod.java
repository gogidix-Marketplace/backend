package com.gogidix.finance.budgetmanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Budget Period Enum
 * Defines the period type for budget cycles
 */
public enum BudgetPeriod {
    MONTHLY,
    QUARTERLY,
    ANNUAL,
    CUSTOM;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeriodRange {
        private LocalDate startDate;
        private LocalDate endDate;
        private int year;
        private int period;

        public static PeriodRange forMonthly(int year, int month) {
            YearMonth yearMonth = YearMonth.of(year, month);
            return PeriodRange.builder()
                    .startDate(yearMonth.atDay(1))
                    .endDate(yearMonth.atEndOfMonth())
                    .year(year)
                    .period(month)
                    .build();
        }

        public static PeriodRange forQuarterly(int year, int quarter) {
            int startMonth = (quarter - 1) * 3 + 1;
            YearMonth startYearMonth = YearMonth.of(year, startMonth);
            YearMonth endYearMonth = YearMonth.of(year, startMonth + 2);

            return PeriodRange.builder()
                    .startDate(startYearMonth.atDay(1))
                    .endDate(endYearMonth.atEndOfMonth())
                    .year(year)
                    .period(quarter)
                    .build();
        }

        public static PeriodRange forAnnual(int year) {
            return PeriodRange.builder()
                    .startDate(LocalDate.of(year, 1, 1))
                    .endDate(LocalDate.of(year, 12, 31))
                    .year(year)
                    .period(0)
                    .build();
        }
    }
}
