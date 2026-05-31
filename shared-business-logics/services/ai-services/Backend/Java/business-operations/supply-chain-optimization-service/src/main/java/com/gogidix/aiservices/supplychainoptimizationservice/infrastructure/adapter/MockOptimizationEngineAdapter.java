package com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.adapter;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate.OptimizationRequest;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.*;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.port.out.OptimizationEnginePort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class MockOptimizationEngineAdapter implements OptimizationEnginePort {

    @Override
    public OptimizationResult generateOptimization(OptimizationRequest request) {
        List<OptimizationMetric> metrics = generateMockMetrics(request.getType());
        List<OptimizationRecommendation> recommendations = generateMockRecommendations(request.getType());

        return OptimizationResult.builder()
                .requestId(request.getRequestId())
                .type(request.getType())
                .metrics(metrics)
                .recommendations(recommendations)
                .confidenceScore(0.85)
                .summary("Optimization completed successfully for " + request.getType())
                .build();
    }

    @Override
    public List<OptimizationResult> generateBatchOptimizations(List<OptimizationRequest> requests) {
        return requests.stream()
                .map(this::generateOptimization)
                .toList();
    }

    @Override
    public Map<String, Object> analyzeSupplyChainData(String tenantId, OptimizationType type) {
        return Map.of(
                "tenantId", tenantId,
                "type", type.toString(),
                "dataPoints", 1000,
                "lastAnalyzed", Instant.now().toString()
        );
    }

    @Override
    public boolean validateParameters(OptimizationType type, Map<String, Object> parameters) {
        return true;
    }

    @Override
    public double estimateProcessingTime(OptimizationType type, Map<String, Object> parameters) {
        return switch (type) {
            case DEMAND_FORECASTING -> 30.0;
            case INVENTORY_LEVELS -> 15.0;
            case ROUTE_OPTIMIZATION -> 45.0;
            case SUPPLIER_SELECTION -> 20.0;
            case PRODUCTION_SCHEDULING -> 35.0;
            default -> 25.0;
        };
    }

    private List<OptimizationMetric> generateMockMetrics(OptimizationType type) {
        List<OptimizationMetric> metrics = new ArrayList<>();

        switch (type) {
            case INVENTORY_LEVELS -> {
                metrics.add(OptimizationMetric.builder()
                        .metricName("Inventory Carrying Cost")
                        .currentValue(100000)
                        .optimizedValue(85000)
                        .unit("USD")
                        .direction(OptimizationMetric.ImprovementDirection.LOWER_IS_BETTER)
                        .build());
                metrics.add(OptimizationMetric.builder()
                        .metricName("Stockout Rate")
                        .currentValue(0.05)
                        .optimizedValue(0.02)
                        .unit("rate")
                        .direction(OptimizationMetric.ImprovementDirection.LOWER_IS_BETTER)
                        .build());
                break;
            }
            case DEMAND_FORECASTING -> {
                metrics.add(OptimizationMetric.builder()
                        .metricName("Forecast Accuracy")
                        .currentValue(0.75)
                        .optimizedValue(0.90)
                        .unit("score")
                        .direction(OptimizationMetric.ImprovementDirection.HIGHER_IS_BETTER)
                        .build());
                break;
            }
            case ROUTE_OPTIMIZATION -> {
                metrics.add(OptimizationMetric.builder()
                        .metricName("Delivery Distance")
                        .currentValue(5000)
                        .optimizedValue(4200)
                        .unit("km")
                        .direction(OptimizationMetric.ImprovementDirection.LOWER_IS_BETTER)
                        .build());
                metrics.add(OptimizationMetric.builder()
                        .metricName("Fuel Consumption")
                        .currentValue(1000)
                        .optimizedValue(850)
                        .unit("liters")
                        .direction(OptimizationMetric.ImprovementDirection.LOWER_IS_BETTER)
                        .build());
                break;
            }
            default -> {
                metrics.add(OptimizationMetric.builder()
                        .metricName("Efficiency Gain")
                        .currentValue(1.0)
                        .optimizedValue(1.15)
                        .unit("ratio")
                        .direction(OptimizationMetric.ImprovementDirection.HIGHER_IS_BETTER)
                        .build());
            }
        }

        return metrics;
    }

    private List<OptimizationRecommendation> generateMockRecommendations(OptimizationType type) {
        List<OptimizationRecommendation> recommendations = new ArrayList<>();

        switch (type) {
            case INVENTORY_LEVELS -> {
                recommendations.add(OptimizationRecommendation.builder()
                        .title("Implement Just-in-Time Inventory")
                        .description("Reduce carrying costs by implementing JIT inventory management")
                        .priority(OptimizationRecommendation.RecommendationPriority.HIGH)
                        .estimatedSavings(15000)
                        .estimatedTimeToImplement(30)
                        .build());
                break;
            }
            case DEMAND_FORECASTING -> {
                recommendations.add(OptimizationRecommendation.builder()
                        .title("Use Machine Learning Forecasting")
                        .description("Improve forecast accuracy with ML models")
                        .priority(OptimizationRecommendation.RecommendationPriority.MEDIUM)
                        .estimatedSavings(25000)
                        .estimatedTimeToImplement(60)
                        .build());
                break;
            }
            case ROUTE_OPTIMIZATION -> {
                recommendations.add(OptimizationRecommendation.builder()
                        .title("Optimize Delivery Routes")
                        .description("Use AI to optimize delivery routes and reduce fuel consumption")
                        .priority(OptimizationRecommendation.RecommendationPriority.HIGH)
                        .estimatedSavings(12000)
                        .estimatedTimeToImplement(14)
                        .build());
                break;
            }
            default -> {
                recommendations.add(OptimizationRecommendation.builder()
                        .title("General Optimization")
                        .description("Review and optimize supply chain processes")
                        .priority(OptimizationRecommendation.RecommendationPriority.MEDIUM)
                        .estimatedSavings(10000)
                        .estimatedTimeToImplement(30)
                        .build());
            }
        }

        return recommendations;
    }
}
