package com.gogidix.aiservices.aichurnpredictionservice.application.dto;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for searching segments with filters.
 */
@Schema(description = "Request DTO for searching churn predictions")
public record PredictionSearchRequestDto(

        @Schema(description = "Filter by segment type", example = "BEHAVIORAL")
        PredictionType segmentType,

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
    public PredictionSearchRequestDto {
        page = page == null ? 0 : page;
        size = size == null ? 20 : size;
        sortBy = sortBy == null || sortBy.isBlank() ? "createdAt" : sortBy;
        sortDirection = sortDirection == null || sortDirection.isBlank() ? "DESC" : sortDirection;
    }

    public static PredictionSearchRequestDto create() {
        return new PredictionSearchRequestDto(null, null, 0, 20, "createdAt", "DESC");
    }
}
