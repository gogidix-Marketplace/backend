package com.gogidix.aiservices.intelligenceanalysisservice.application.dto;

import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for searching segments with filters.
 */
@Schema(description = "Request DTO for searching customer segments")
public record AnalysisSearchRequestDto(

        @Schema(description = "Filter by segment type", example = "BEHAVIORAL")
        AnalysisType segmentType,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active,

        @Schema(description = "Page number (0-based)", example = "0")
        Integer page,

        @Schema(description = "Page size", example = "20")
        Integer size,

        @Schema(description = "Sort field", example = "createdAt")
        String sortBy,

        @Schema(description = "Sort direction", example = "DESC")
        String sortDirection

) {
    public AnalysisSearchRequestDto {
        page = page == null ? 0 : page;
        size = size == null ? 20 : size;
        sortBy = sortBy == null || sortBy.isBlank() ? "createdAt" : sortBy;
        sortDirection = sortDirection == null || sortDirection.isBlank() ? "DESC" : sortDirection;
    }

    public static AnalysisSearchRequestDto create() {
        return new AnalysisSearchRequestDto(null, null, 0, 20, "createdAt", "DESC");
    }
}
