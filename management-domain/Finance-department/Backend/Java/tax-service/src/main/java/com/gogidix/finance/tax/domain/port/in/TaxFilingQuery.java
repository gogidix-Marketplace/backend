package com.gogidix.finance.tax.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Tax Filing Queries (Input Port)
 * Defines the query operations for tax filing data
 */
public interface TaxFilingQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetFilingQuery {
        private String tenantId;

        private String filingId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetFilingsByPeriodQuery {
        private String tenantId;

        private YearMonth period;

        private String jurisdiction;

        private String taxType;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetFilingsByStatusQuery {
        private String tenantId;

        private String status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPendingFilingsQuery {
        private String tenantId;

        private LocalDate dueBefore;

        private String jurisdiction;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetOverdueFilingsQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetFilingsByDateRangeQuery {
        private String tenantId;

        private YearMonth startPeriod;

        private YearMonth endPeriod;

        private String jurisdiction;

        private String taxType;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetFilingHistoryQuery {
        private String tenantId;

        private String jurisdiction;

        private String taxType;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetFilingSummaryQuery {
        private String tenantId;

        private YearMonth period;

        private String jurisdiction;

        private String taxType;
    }
}
