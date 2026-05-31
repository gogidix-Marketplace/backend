package com.gogidix.aiservices.aifrauddetectionservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic pagination wrapper for API responses.
 * Contains paginated data along with metadata about the pagination.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * The list of items in the current page.
     */
    private List<T> content;

    /**
     * Current page number (zero-based).
     */
    private int pageNumber;

    /**
     * Number of items per page.
     */
    private int pageSize;

    /**
     * Total number of items across all pages.
     */
    private long totalElements;

    /**
     * Total number of pages available.
     */
    private int totalPages;

    /**
     * Whether there is a next page.
     */
    private boolean hasNext;

    /**
     * Whether there is a previous page.
     */
    private boolean hasPrevious;

    /**
     * Whether this is the first page.
     */
    private boolean isFirst;

    /**
     * Whether this is the last page.
     */
    private boolean isLast;

    /**
     * Creates an empty PageResponse.
     */
    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(
                List.of(),
                0,
                0,
                0,
                0,
                false,
                false,
                true,
                true
        );
    }

    /**
     * Creates a PageResponse from a Spring Data Page.
     */
    public static <T> PageResponse<T> of(org.springframework.data.domain.Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                page.isFirst(),
                page.isLast()
        );
    }
}
