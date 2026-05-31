package com.gogidix.aiservices.intelligenceanalysisservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * DTO for analyzing a segment.
 */
@Schema(description = "Request DTO for analyzing a customer segment")
public record AnalyzeAnalysisRequestDto(

        @Schema(description = "Additional analysis options", example = "{\"includeTrends\": true, \"includePredictions\": false}")
        Map<String, Object> analysisOptions

) {
    public AnalyzeAnalysisRequestDto {
        analysisOptions = analysisOptions == null ? Map.of() : analysisOptions;
    }

    public static AnalyzeAnalysisRequestDto create() {
        return new AnalyzeAnalysisRequestDto(Map.of());
    }
}
