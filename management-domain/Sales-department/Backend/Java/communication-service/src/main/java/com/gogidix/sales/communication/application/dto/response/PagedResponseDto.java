package com.gogidix.sales.communication.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Paged Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponseDto<T> {

    private List<T> content;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;

    public static <T> PagedResponseDto<T> of(List<T> content, int currentPage, int pageSize, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        return PagedResponseDto.<T>builder()
                .content(content)
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .first(currentPage == 0)
                .last(currentPage >= totalPages - 1)
                .empty(content == null || content.isEmpty())
                .build();
    }

    public static <T> PagedResponseDto<T> empty() {
        return PagedResponseDto.<T>builder()
                .content(List.of())
                .currentPage(0)
                .pageSize(0)
                .totalElements(0)
                .totalPages(0)
                .first(true)
                .last(true)
                .empty(true)
                .build();
    }
}
