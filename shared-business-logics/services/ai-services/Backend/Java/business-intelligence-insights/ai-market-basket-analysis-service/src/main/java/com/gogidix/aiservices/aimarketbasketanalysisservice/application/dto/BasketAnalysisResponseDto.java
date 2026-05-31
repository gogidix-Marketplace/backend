package com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;

/**
 * DTO for segment analysis response.
 */
@Schema(description = "Response DTO for segment analysis results")
public record BasketAnalysisResponseDto(

        @Schema(description = "Basket identifier", example = "seg_1234567890abcdef")
        String segmentId,

        @Schema(description = "Analysis timestamp", example = "2024-01-20T14:45:00Z")
        Instant analyzedAt,

        @Schema(description = "Total customers analyzed", example = "1523")
        int totalCustomers,

        @Schema(description = "Customer demographics breakdown")
        Map<String, Object> demographics,

        @Schema(description = "Behavioral metrics")
        Map<String, Object> behaviors,

        @Schema(description = "Transaction metrics")
        Map<String, Object> transactions,

        @Schema(description = "Predictive insights")
        Map<String, Object> predictions,

        @Schema(description = "Recommendations for segment optimization")
        Map<String, Object> recommendations,

        @Schema(description = "Confidence score of the analysis (0-1)", example = "0.87")
        double confidenceScore

) {
    /**
     * Get the customer count (alias for totalCustomers for test compatibility).
     * @return the total number of customers analyzed
     */
    public int customerCount() {
        return totalCustomers;
    }
}
