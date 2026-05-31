package com.gogidix.finance.currency.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

/**
 * Response DTO - Paged Response
 * Generic pagination response wrapper
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    public static <T> PagedResponseDto<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return PagedResponseDto.<T>builder()
            .content(content)
            .page(page)
            .size(size)
            .totalElements(totalElements)
            .totalPages(totalPages)
            .first(page == 0)
            .last(page >= totalPages - 1)
            .empty(content.isEmpty())
            .build();
    }

    public static <T> PagedResponseDto<T> emptyResult() {
        return PagedResponseDto.<T>builder()
            .content(List.of())
            .page(0)
            .size(0)
            .totalElements(0)
            .totalPages(0)
            .first(true)
            .last(true)
            .empty(true)
            .build();
    }
}
