package com.gogidix.courier.gpstrackingservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Generic paged response DTO.
 */
@Schema(description = "Paged response wrapper")
public record PagedResponseDto<T>(

        @JsonProperty("data")
        @Schema(description = "Page data")
        List<T> data,

        @JsonProperty("page")
        @Schema(description = "Current page number (0-based)", example = "0")
        int page,

        @JsonProperty("size")
        @Schema(description = "Page size", example = "20")
        int size,

        @JsonProperty("total_elements")
        @Schema(description = "Total number of elements", example = "150")
        long totalElements,

        @JsonProperty("total_pages")
        @Schema(description = "Total number of pages", example = "8")
        int totalPages,

        @JsonProperty("has_next")
        @Schema(description = "Whether there is a next page", example = "true")
        boolean hasNext,

        @JsonProperty("has_previous")
        @Schema(description = "Whether there is a previous page", example = "false")
        boolean hasPrevious
) {
    public static <T> PagedResponseDto<T> of(List<T> data, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PagedResponseDto<>(
                data,
                page,
                size,
                totalElements,
                totalPages,
                page < totalPages - 1,
                page > 0
        );
    }

    public static <T> PagedResponseDto<T> empty() {
        return new PagedResponseDto<>(List.of(), 0, 0, 0, 0, false, false);
    }
}
