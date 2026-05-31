package com.gogidix.finance.forecasting.domain.port.in;

import com.gogidix.finance.forecasting.domain.model.Forecast;
import com.gogidix.finance.forecasting.domain.model.ForecastMetric;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Forecast Queries (Input Port)
 * Defines the query operations for forecast data
 */
public interface ForecastQuery {

    /**
     * Query to get a forecast by ID
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetByIdQuery {
        private String tenantId;

        private String forecastId;
    }

    /**
     * Query to get forecasts by type
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetByTypeQuery {
        private String tenantId;

        private Forecast.ForecastType forecastType;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    /**
     * Query to get forecasts by date range
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetByDateRangeQuery {
        private String tenantId;

        private Instant startDate;

        private Instant endDate;

        private List<Forecast.ForecastStatus> statuses;

        private List<Forecast.ForecastType> types;

        private Integer page;

        private Integer size;
    }

    /**
     * Query to get forecasts by status
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetByStatusQuery {
        private String tenantId;

        private Forecast.ForecastStatus status;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    /**
     * Query to get forecasts by horizon
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetByHorizonQuery {
        private String tenantId;

        private Forecast.ForecastHorizon forecastHorizon;

        private Integer page;

        private Integer size;
    }

    /**
     * Query to get forecasts by department
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetByDepartmentQuery {
        private String tenantId;

        private String department;

        private Integer page;

        private Integer size;

        private Forecast.ForecastStatus status;
    }

    /**
     * Query to get forecasts by scenario
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetByScenarioQuery {
        private String tenantId;

        private String scenario;

        private Integer page;

        private Integer size;
    }

    /**
     * Query to search forecasts
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchForecastsQuery {
        private String tenantId;

        private String searchTerm;

        private Forecast.ForecastType forecastType;

        private Forecast.ForecastStatus status;

        private Forecast.ForecastHorizon forecastHorizon;

        private String department;

        private String category;

        private Instant startDate;

        private Instant endDate;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    /**
     * Query to get forecast summary
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastSummaryQuery {
        private String tenantId;

        private Instant startDate;

        private Instant endDate;

        private String department;

        private Forecast.ForecastType forecastType;

        private String scenario;
    }

    /**
     * Query to get forecasts requiring approval
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPendingApprovalQuery {
        private String tenantId;

        private String department;

        private Forecast.ForecastType forecastType;

        private Integer page;

        private Integer size;
    }

    /**
     * Query to get archived forecasts
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetArchivedQuery {
        private String tenantId;

        private Instant startDate;

        private Instant endDate;

        private Integer page;

        private Integer size;
    }

    /**
     * Query to get forecast comparison
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastComparisonQuery {
        private String tenantId;

        private List<String> forecastIds;

        private boolean includeMetrics;
    }

    /**
     * Query to get variance analysis
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetVarianceAnalysisQuery {
        private String tenantId;

        private String forecastId;

        private String comparisonForecastId;

        private boolean includeMetrics;
    }

    /**
     * Query to get forecasts by creator
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetByCreatorQuery {
        private String tenantId;

        private String createdBy;

        private Forecast.ForecastStatus status;

        private Integer page;

        private Integer size;
    }

    /**
     * Query to get latest forecasts
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetLatestForecastsQuery {
        private String tenantId;

        private Forecast.ForecastType forecastType;

        private Integer limit;

        private Forecast.ForecastStatus status;
    }

    /**
     * Query to get forecast metrics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastMetricsQuery {
        private String tenantId;

        private String forecastId;

        private String metricCategory;

        private ForecastMetric.MetricType metricType;
    }

    /**
     * Query to get forecast history
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastHistoryQuery {
        private String tenantId;

        private String baseForecastId;

        private Integer limit;
    }
}
