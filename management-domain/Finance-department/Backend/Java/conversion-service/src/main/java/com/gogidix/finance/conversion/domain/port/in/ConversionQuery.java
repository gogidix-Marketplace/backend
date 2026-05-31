package com.gogidix.finance.conversion.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Conversion Queries (Input Port)
 * Defines the query operations for conversion data
 */
public interface ConversionQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConversionQuery {
        private String tenantId;

        private String conversionId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConversionsByUserQuery {
        private String tenantId;

        private String requestedBy;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConversionsByDateRangeQuery {
        private String tenantId;

        private Instant startDate;

        private Instant endDate;

        private String fromCurrency;

        private String toCurrency;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConversionsByCurrenciesQuery {
        private String tenantId;

        private String fromCurrency;

        private String toCurrency;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConversionRateQuery {
        private String tenantId;

        private String fromCurrency;

        private String toCurrency;

        private Boolean forceRefresh;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetHistoricalRatesQuery {
        private String tenantId;

        private String fromCurrency;

        private String toCurrency;

        private Instant startDate;

        private Instant endDate;

        private String provider;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConversionStatisticsQuery {
        private String tenantId;

        private Instant startDate;

        private Instant endDate;

        private String fromCurrency;

        private String toCurrency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetSupportedCurrenciesQuery {
        private String tenantId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CalculateConversionQuery {
        private String tenantId;

        private BigDecimal amount;

        private String fromCurrency;

        private String toCurrency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class BatchConversionQuery {
        private String tenantId;

        private List<CurrencyPair> currencyPairs;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CurrencyPair {
            private String fromCurrency;
            private String toCurrency;
        }
    }
}
