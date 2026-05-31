package com.gogidix.courier.routingservice.application.dto;

import java.util.List;

/**
 * Generic DTO for paginated responses.
 */
public record PagedResponseDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        boolean empty
) {
    public PagedResponseDto {
        totalPages = (int) Math.ceil((double) totalElements / size);
        first = page == 0;
        last = page >= totalPages - 1;
        empty = content == null || content.isEmpty();
    }

    /**
     * Create a paged response.
     *
     * @param content         the content
     * @param page            the page number
     * @param size            the page size
     * @param totalElements   the total number of elements
     * @return the paged response
     */
    public static <T> PagedResponseDto<T> of(List<T> content, int page, int size, long totalElements) {
        return new PagedResponseDto<>(
                content,
                page,
                size,
                totalElements,
                (int) Math.ceil((double) totalElements / size),
                page == 0,
                page >= (int) Math.ceil((double) totalElements / size) - 1,
                content == null || content.isEmpty()
        );
    }

    /**
     * Create an empty paged response.
     *
     * @param page the page number
     * @param size the page size
     * @return the empty paged response
     */
    public static <T> PagedResponseDto<T> empty(int page, int size) {
        return new PagedResponseDto<>(
                List.of(),
                page,
                size,
                0,
                0,
                true,
                true,
                true
        );
    }
}
