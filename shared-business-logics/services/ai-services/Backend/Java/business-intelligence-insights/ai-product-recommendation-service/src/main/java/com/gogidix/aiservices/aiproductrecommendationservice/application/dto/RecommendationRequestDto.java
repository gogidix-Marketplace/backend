package com.gogidix.aiservices.aiproductrecommendationservice.application.dto;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request DTO for product recommendation.
 */
public record RecommendationRequestDto(
    @NotNull(message = "customerId is required") String customerId,
    @NotNull(message = "tenantId is required") String tenantId,
    @NotNull(message = "type is required") RecommendationType type,
    @NotNull(message = "maxResults must be positive") @Min(value = 1, message = "maxResults must be at least 1") Integer maxResults,
    List<String> excludedCategories,
    Boolean includeOutOfStock
) {

    /**
     * Create a recommendation request for cross-sell.
     */
    public static RecommendationRequestDto forCrossSell(String customerId, String tenantId, List<String> purchasedProductCategories) {
        return new RecommendationRequestDto(
                customerId,
                tenantId,
                RecommendationType.COLLABORATIVE_FILTERING,
                10,
                purchasedProductCategories,
                true
        );
    }

    /**
     * Create a recommendation request for purchase history.
     */
    public static RecommendationRequestDto forPurchaseHistory(String customerId, String tenantId) {
        return new RecommendationRequestDto(
                customerId,
                tenantId,
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                10,
                List.of(),
                true
        );
    }

    /**
     * Create a recommendation request for trending products.
     */
    public static RecommendationRequestDto forTrending(String customerId, String tenantId) {
        return new RecommendationRequestDto(
                customerId,
                tenantId,
                RecommendationType.TREND_BASED,
                20,
                List.of(),
                true
        );
    }
}
