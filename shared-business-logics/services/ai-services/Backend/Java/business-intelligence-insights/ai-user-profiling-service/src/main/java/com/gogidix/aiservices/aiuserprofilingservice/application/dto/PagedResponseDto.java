package com.gogidix.aiservices.aiuserprofilingservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Generic paged response DTO.
 *
 * @param <T> The type of data in the page
 */
@Schema(description = "Generic paged response wrapper")
public record PagedResponseDto<T>(

        @Schema(description = "List of items in the current page")
        List<T> content,

        @Schema(description = "Current page number (0-based)", example = "0")
        int page,

        @Schema(description = "Page size", example = "20")
        int size,

        @Schema(description = "Total number of elements", example = "152")
        long totalElements,

        @Schema(description = "Total number of pages", example = "8")
        int totalPages,

        @Schema(description = "Whether this is the first page", example = "true")
        boolean first,

        @Schema(description = "Whether this is the last page", example = "false")
        boolean last

) {
    /**
     * Creates an empty paged response.
     */
    public static <T> PagedResponseDto<T> empty() {
        return new PagedResponseDto<>(List.of(), 0, 0, 0, 0, true, true);
    }

    /**
     * Creates a paged response from a list and pagination info.
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
