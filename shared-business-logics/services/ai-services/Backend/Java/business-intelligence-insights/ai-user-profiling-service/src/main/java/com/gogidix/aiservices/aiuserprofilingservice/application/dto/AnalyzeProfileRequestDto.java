package com.gogidix.aiservices.aiuserprofilingservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * DTO for analyzing a segment.
 */
@Schema(description = "Request DTO for analyzing a customer segment")
public record AnalyzeProfileRequestDto(

        @Schema(description = "Additional analysis options", example = "{\"includeTrends\": true, \"includePredictions\": false}")
        Map<String, Object> analysisOptions

) {
    public AnalyzeProfileRequestDto {
        analysisOptions = analysisOptions == null ? Map.of() : analysisOptions;
    }

    public static AnalyzeProfileRequestDto create() {
        return new AnalyzeProfileRequestDto(Map.of());
    }
}
