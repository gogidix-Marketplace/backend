package com.gogidix.aiservices.aiproductrecommendationservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * DTO for analyzing a segment.
 */
@Schema(description = "Request DTO for analyzing a product recommendation")
public record AnalyzeRecommendationRequestDto(

        @Schema(description = "Additional analysis options", example = "{\"includeTrends\": true, \"includePredictions\": false}")
        Map<String, Object> analysisOptions

) {
    public AnalyzeRecommendationRequestDto {
        analysisOptions = analysisOptions == null ? Map.of() : analysisOptions;
    }

    public static AnalyzeRecommendationRequestDto create() {
        return new AnalyzeRecommendationRequestDto(Map.of());
    }
}
