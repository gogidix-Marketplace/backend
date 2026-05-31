package com.gogidix.courier.availabilityservice.application.dto;

import java.util.List;

/**
 * Generic paged response DTO.
 */
public record PagedResponseDto<T>(
        List<T> data,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
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
