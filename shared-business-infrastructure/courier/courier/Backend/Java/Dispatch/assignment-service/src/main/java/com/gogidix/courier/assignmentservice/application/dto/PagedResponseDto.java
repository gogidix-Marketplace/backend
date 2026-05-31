package com.gogidix.courier.assignmentservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Generic paged response DTO.
 */
@Schema(description = "Paged response wrapper")
public record PagedResponseDto<T>(

        @Schema(description = "Page content")
        List<T> content,

        @JsonProperty("page_number")
        @Schema(description = "Current page number (0-based)", example = "0")
        int pageNumber,

        @JsonProperty("page_size")
        @Schema(description = "Number of items per page", example = "20")
        int pageSize,

        @JsonProperty("total_elements")
        @Schema(description = "Total number of elements", example = "100")
        long totalElements,

        @JsonProperty("total_pages")
        @Schema(description = "Total number of pages", example = "5")
        int totalPages,

        @JsonProperty("first")
        @Schema(description = "Is this the first page?", example = "true")
        boolean first,

        @JsonProperty("last")
        @Schema(description = "Is this the last page?", example = "false")
        boolean last
) {
    /**
     * Create an empty paged response.
     */
    public static <T> PagedResponseDto<T> empty(int page, int size) {
        return new PagedResponseDto<>(List.of(), page, size, 0, 0, true, true);
    }

    /**
     * Create a paged response from content.
     */
    public static <T> PagedResponseDto<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PagedResponseDto<>(
                content,
                page,
                size,
                totalElements,
                totalPages,
                page == 0,
                page >= totalPages - 1
        );
    }
}
