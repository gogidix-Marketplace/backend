package com.gogidix.aiservices.aisalesforecastingservice.application.dto;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for searching segments with filters.
 */
@Schema(description = "Request DTO for searching sales forecasts")
public record ForecastSearchRequestDto(

        @Schema(description = "Filter by segment type", example = "BEHAVIORAL")
        ForecastType segmentType,

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
    public ForecastSearchRequestDto {
        page = page == null ? 0 : page;
        size = size == null ? 20 : size;
        sortBy = sortBy == null || sortBy.isBlank() ? "createdAt" : sortBy;
        sortDirection = sortDirection == null || sortDirection.isBlank() ? "DESC" : sortDirection;
    }

    public static ForecastSearchRequestDto create() {
        return new ForecastSearchRequestDto(null, null, 0, 20, "createdAt", "DESC");
    }
}
