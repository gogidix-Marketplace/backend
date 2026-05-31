package com.gogidix.universal.tracking.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic paged response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paged response wrapper")
public class PagedResponseDto<T> {

    @Schema(description = "List of items")
    private List<T> items;

    @Schema(description = "Current page number (0-indexed)", example = "0")
    private Integer page;

    @Schema(description = "Page size", example = "20")
    private Integer size;

    @Schema(description = "Total number of elements", example = "100")
    private Long totalElements;

    @Schema(description = "Total number of pages", example = "5")
    private Integer totalPages;

    @Schema(description = "Whether this is the first page", example = "true")
    private Boolean isFirst;

    @Schema(description = "Whether this is the last page", example = "false")
    private Boolean isLast;

    /**
     * Create a paged response from Spring Data Page
     */
    public static <T> PagedResponseDto<T> of(org.springframework.data.domain.Page<T> page) {
        return PagedResponseDto.<T>builder()
            .items(page.getContent())
            .page(page.getNumber())
            .size(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .isFirst(page.isFirst())
            .isLast(page.isLast())
            .build();
    }
}
