package com.gogidix.finance.tax.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Tax Calculation Queries (Input Port)
 * Defines the query operations for tax calculation data
 */
public interface TaxCalculationQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCalculationQuery {
        private String tenantId;

        private String calculationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCalculationsByTransactionQuery {
        private String tenantId;

        private String transactionId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCalculationsByDateRangeQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private String status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCalculationsByPeriodQuery {
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
    class GetTaxSummaryQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private String jurisdiction;

        private String taxType;

        private String currency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTaxLiabilityReportQuery {
        private String tenantId;

        private YearMonth period;

        private String jurisdiction;

        private String taxType;

        private Boolean includePending;

        private Boolean includeVerified;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetReconcilableCalculationsQuery {
        private String tenantId;

        private YearMonth period;

        private String jurisdiction;

        private String taxType;

        private Integer page;

        private Integer size;
    }
}
