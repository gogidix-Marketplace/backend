package com.gogidix.finance.tax.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Tax Rate Queries (Input Port)
 * Defines the query operations for tax rate data
 */
public interface TaxRateQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTaxRateQuery {
        private String tenantId;

        private String taxRateId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTaxRatesByJurisdictionQuery {
        private String tenantId;

        private String jurisdiction;

        private Boolean includeExpired;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTaxRatesByTypeQuery {
        private String tenantId;

        private String taxType;

        private Boolean includeInactive;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetEffectiveTaxRateQuery {
        private String tenantId;

        private String jurisdiction;

        private String taxType;

        private String taxCode;

        private LocalDate date;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTaxRatesByDateRangeQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private String jurisdiction;

        private String taxType;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchTaxRatesQuery {
        private String tenantId;

        private String searchTerm;

        private String jurisdiction;

        private String taxType;

        private String status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTaxRateHistoryQuery {
        private String tenantId;

        private String taxCode;

        private String jurisdiction;

        private String taxType;

        private Integer page;

        private Integer size;
    }
}
