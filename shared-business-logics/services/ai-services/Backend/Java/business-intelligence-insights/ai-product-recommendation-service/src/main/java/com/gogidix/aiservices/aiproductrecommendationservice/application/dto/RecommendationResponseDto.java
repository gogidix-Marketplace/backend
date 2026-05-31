package com.gogidix.aiservices.aiproductrecommendationservice.application.dto;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.Product;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductScore;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationResult;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationResultId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Response DTO for product recommendation results.
 */
public record RecommendationResponseDto(
    @NotNull(message = "recommendationId is required") RecommendationResultId recommendationId,
    @NotNull(message = "customerId is required") String customerId,
    @NotNull(message = "tenantId is required") String tenantId,
    List<@NotNull(message = "rankedProducts is required") ProductScore> rankedProducts,
    @NotNull(message = "executedAt is required") Instant executedAt,
    @NotNull(message = "algorithmVersion is required") String algorithmVersion
) {

    public static RecommendationResponseDto success(String customerId, String tenantId, List<ProductScore> rankedProducts) {
        return new RecommendationResponseDto(
                RecommendationResultId.randomUUID(),
                customerId,
                tenantId,
                rankedProducts,
                Instant.now(),
                "v1.0"
        );
    }

    public static RecommendationResponseDto error(String customerId, String tenantId, String errorCode, String errorMessage) {
        return new RecommendationResponseDto(
                RecommendationResultId.randomUUID(),
                customerId,
                tenantId,
                List.of(),
                Instant.now(),
                "v1.0"
        );
    }

    public boolean isSuccess() {
        return !rankedProducts.isEmpty();
    }

    public String getErrorCode() {
        return rankedProducts.isEmpty() ? null : "RECOMMENDATION_ENGINE_ERROR";
    }

    public String getErrorMessage() {
        return rankedProducts.isEmpty() ? null : "No products found matching criteria";
    }
}
