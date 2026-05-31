package com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.adapter;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate.OptimizationRequest;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MockOptimizationEngineAdapter Infrastructure Tests")
class MockOptimizationEngineAdapterTest {

    private MockOptimizationEngineAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MockOptimizationEngineAdapter();
    }

    @Nested
    @DisplayName("Generate Optimization Tests")
    class GenerateOptimizationTests {

        @Test
        @DisplayName("Should generate inventory optimization")
        void shouldGenerateInventoryOptimization() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result).isNotNull();
            assertThat(result.getRequestId()).isEqualTo(request.getRequestId());
            assertThat(result.getType()).isEqualTo(OptimizationType.INVENTORY_LEVELS);
            assertThat(result.getConfidenceScore()).isEqualTo(0.85);
        }

        @Test
        @DisplayName("Should generate demand forecasting optimization")
        void shouldGenerateDemandForecasting() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.DEMAND_FORECASTING)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result).isNotNull();
            assertThat(result.getType()).isEqualTo(OptimizationType.DEMAND_FORECASTING);
            assertThat(result.getMetrics()).isNotEmpty();
        }

        @Test
        @DisplayName("Should generate route optimization")
        void shouldGenerateRouteOptimization() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.ROUTE_OPTIMIZATION)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result).isNotNull();
            assertThat(result.getType()).isEqualTo(OptimizationType.ROUTE_OPTIMIZATION);
            assertThat(result.getRecommendations()).hasSize(1);
        }

        @Test
        @DisplayName("Should generate supplier selection optimization")
        void shouldGenerateSupplierSelection() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.SUPPLIER_SELECTION)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result).isNotNull();
            assertThat(result.getType()).isEqualTo(OptimizationType.SUPPLIER_SELECTION);
        }

        @Test
        @DisplayName("Should generate production scheduling optimization")
        void shouldGenerateProductionScheduling() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.PRODUCTION_SCHEDULING)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result).isNotNull();
            assertThat(result.getType()).isEqualTo(OptimizationType.PRODUCTION_SCHEDULING);
        }
    }

    @Nested
    @DisplayName("Batch Optimization Tests")
    class BatchOptimizationTests {

        @Test
        @DisplayName("Should generate batch optimizations")
        void shouldGenerateBatchOptimizations() {
            List<OptimizationRequest> requests = Arrays.asList(
                    OptimizationRequest.builder()
                            .tenantId("tenant-123")
                            .type(OptimizationType.INVENTORY_LEVELS)
                            .build(),
                    OptimizationRequest.builder()
                            .tenantId("tenant-123")
                            .type(OptimizationType.ROUTE_OPTIMIZATION)
                            .build()
            );

            List<OptimizationResult> results = adapter.generateBatchOptimizations(requests);

            assertThat(results).hasSize(2);
            assertThat(results.get(0).getType()).isEqualTo(OptimizationType.INVENTORY_LEVELS);
            assertThat(results.get(1).getType()).isEqualTo(OptimizationType.ROUTE_OPTIMIZATION);
        }

        @Test
        @DisplayName("Should handle empty batch")
        void shouldHandleEmptyBatch() {
            List<OptimizationResult> results = adapter.generateBatchOptimizations(List.of());

            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("Data Analysis Tests")
    class DataAnalysisTests {

        @Test
        @DisplayName("Should analyze supply chain data")
        void shouldAnalyzeSupplyChainData() {
            Map<String, Object> result = adapter.analyzeSupplyChainData(
                    "tenant-123",
                    OptimizationType.INVENTORY_LEVELS
            );

            assertThat(result).isNotNull();
            assertThat(result.get("tenantId")).isEqualTo("tenant-123");
            assertThat(result.get("type")).isEqualTo(OptimizationType.INVENTORY_LEVELS.toString());
            assertThat(result.get("dataPoints")).isEqualTo(1000);
        }
    }

    @Nested
    @DisplayName("Parameter Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate inventory parameters")
        void shouldValidateInventoryParameters() {
            Map<String, Object> params = Map.of(
                    "warehouseId", "WH-001",
                    "productId", "SKU-12345"
            );

            boolean isValid = adapter.validateParameters(
                    OptimizationType.INVENTORY_LEVELS,
                    params
            );

            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("Should validate route parameters")
        void shouldValidateRouteParameters() {
            Map<String, Object> params = Map.of(
                    "origin", "Warehouse-001",
                    "destinations", List.of("Customer-001", "Customer-002")
            );

            boolean isValid = adapter.validateParameters(
                    OptimizationType.ROUTE_OPTIMIZATION,
                    params
            );

            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("Should validate demand forecasting parameters")
        void shouldValidateDemandForecastingParameters() {
            Map<String, Object> params = Map.of(
                    "productId", "SKU-12345",
                    "forecastHorizon", 30
            );

            boolean isValid = adapter.validateParameters(
                    OptimizationType.DEMAND_FORECASTING,
                    params
            );

            assertThat(isValid).isTrue();
        }
    }

    @Nested
    @DisplayName("Processing Time Estimation Tests")
    class ProcessingTimeTests {

        @Test
        @DisplayName("Should estimate demand forecasting time")
        void shouldEstimateDemandForecastingTime() {
            double time = adapter.estimateProcessingTime(
                    OptimizationType.DEMAND_FORECASTING,
                    Map.of("productId", "SKU-12345")
            );

            assertThat(time).isEqualTo(30.0);
        }

        @Test
        @DisplayName("Should estimate inventory optimization time")
        void shouldEstimateInventoryOptimizationTime() {
            double time = adapter.estimateProcessingTime(
                    OptimizationType.INVENTORY_LEVELS,
                    Map.of("warehouseId", "WH-001")
            );

            assertThat(time).isEqualTo(15.0);
        }

        @Test
        @DisplayName("Should estimate route optimization time")
        void shouldEstimateRouteOptimizationTime() {
            double time = adapter.estimateProcessingTime(
                    OptimizationType.ROUTE_OPTIMIZATION,
                    Map.of("origin", "Warehouse-001")
            );

            assertThat(time).isEqualTo(45.0);
        }

        @Test
        @DisplayName("Should estimate supplier selection time")
        void shouldEstimateSupplierSelectionTime() {
            double time = adapter.estimateProcessingTime(
                    OptimizationType.SUPPLIER_SELECTION,
                    Map.of("category", "Raw Materials")
            );

            assertThat(time).isEqualTo(20.0);
        }

        @Test
        @DisplayName("Should estimate production scheduling time")
        void shouldEstimateProductionSchedulingTime() {
            double time = adapter.estimateProcessingTime(
                    OptimizationType.PRODUCTION_SCHEDULING,
                    Map.of("productionLine", "Line-1")
            );

            assertThat(time).isEqualTo(35.0);
        }

        @Test
        @DisplayName("Should estimate default time for unknown type")
        void shouldEstimateDefaultTime() {
            double time = adapter.estimateProcessingTime(
                    OptimizationType.INVENTORY_LEVELS,
                    Map.of()
            );

            assertThat(time).isGreaterThanOrEqualTo(0.0);
        }
    }

    @Nested
    @DisplayName("Metrics Generation Tests")
    class MetricsGenerationTests {

        @Test
        @DisplayName("Should generate inventory metrics")
        void shouldGenerateInventoryMetrics() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result.getMetrics()).isNotEmpty();
            assertThat(result.getMetrics()).anyMatch(m ->
                    m.getMetricName().equals("Inventory Carrying Cost") ||
                    m.getMetricName().equals("Stockout Rate")
            );
        }

        @Test
        @DisplayName("Should generate demand forecasting metrics")
        void shouldGenerateDemandForecastingMetrics() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.DEMAND_FORECASTING)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result.getMetrics()).isNotEmpty();
            assertThat(result.getMetrics()).anyMatch(m ->
                    m.getMetricName().equals("Forecast Accuracy")
            );
        }

        @Test
        @DisplayName("Should generate route optimization metrics")
        void shouldGenerateRouteOptimizationMetrics() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.ROUTE_OPTIMIZATION)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result.getMetrics()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Recommendations Generation Tests")
    class RecommendationsGenerationTests {

        @Test
        @DisplayName("Should generate inventory recommendations")
        void shouldGenerateInventoryRecommendations() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result.getRecommendations()).isNotEmpty();
            assertThat(result.getRecommendations().get(0).getTitle()).contains("Inventory");
        }

        @Test
        @DisplayName("Should generate demand forecasting recommendations")
        void shouldGenerateDemandForecastingRecommendations() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.DEMAND_FORECASTING)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result.getRecommendations()).isNotEmpty();
            assertThat(result.getRecommendations().get(0).getTitle()).contains("Forecasting");
        }

        @Test
        @DisplayName("Should generate route optimization recommendations")
        void shouldGenerateRouteOptimizationRecommendations() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.ROUTE_OPTIMIZATION)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result.getRecommendations()).isNotEmpty();
            assertThat(result.getRecommendations().get(0).getTitle()).contains("Route");
        }
    }

    @Nested
    @DisplayName("Summary Generation Tests")
    class SummaryGenerationTests {

        @Test
        @DisplayName("Should generate summary with type info")
        void shouldGenerateSummaryWithType() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result.getSummary()).contains(OptimizationType.INVENTORY_LEVELS.toString());
        }
    }

    @Nested
    @DisplayName("Confidence Score Tests")
    class ConfidenceScoreTests {

        @Test
        @DisplayName("Should set high confidence score")
        void shouldSetHighConfidenceScore() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationResult result = adapter.generateOptimization(request);

            assertThat(result.getConfidenceScore()).isEqualTo(0.85);
            assertThat(result.hasHighConfidence()).isTrue();
        }
    }
}
