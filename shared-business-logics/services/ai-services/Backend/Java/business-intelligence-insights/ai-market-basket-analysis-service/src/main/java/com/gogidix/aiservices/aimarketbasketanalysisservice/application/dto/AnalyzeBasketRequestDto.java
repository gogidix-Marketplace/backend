package com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * DTO for analyzing a segment.
 */
@Schema(description = "Request DTO for analyzing a market basket")
public record AnalyzeBasketRequestDto(

        @Schema(description = "Additional analysis options", example = "{\"includeTrends\": true, \"includePredictions\": false}")
        Map<String, Object> analysisOptions

) {
    public AnalyzeBasketRequestDto {
        analysisOptions = analysisOptions == null ? Map.of() : analysisOptions;
    }

    public static AnalyzeBasketRequestDto create() {
        return new AnalyzeBasketRequestDto(Map.of());
    }
}
