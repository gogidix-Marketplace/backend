package com.gogidix.sales.forecast.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

/**
 * Forecast Queries (Input Port)
 * Defines the input queries for forecast read operations
 */
public interface ForecastQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastByIdQuery {
        private String forecastId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastsByPeriodQuery {
        private String period;
        private Integer year;
        private Integer monthOrQuarter;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastsByStatusQuery {
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastsByDateRangeQuery {
        private YearMonth startDate;
        private YearMonth endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastsByRegionQuery {
        private String region;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastSummaryQuery {
        private YearMonth startDate;
        private YearMonth endDate;
        private String region;
        private String territory;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastVersionsQuery {
        private String parentForecastId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetLineItemsQuery {
        private String forecastId;
    }
}
