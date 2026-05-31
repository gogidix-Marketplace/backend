package com.gogidix.aiservices.aisalesforecastingservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * DTO for analyzing a segment.
 */
@Schema(description = "Request DTO for analyzing a sales forecast")
public record AnalyzeForecastRequestDto(

        @Schema(description = "Additional analysis options", example = "{\"includeTrends\": true, \"includePredictions\": false}")
        Map<String, Object> analysisOptions

) {
    public AnalyzeForecastRequestDto {
        analysisOptions = analysisOptions == null ? Map.of() : analysisOptions;
    }

    public static AnalyzeForecastRequestDto create() {
        return new AnalyzeForecastRequestDto(Map.of());
    }
}
