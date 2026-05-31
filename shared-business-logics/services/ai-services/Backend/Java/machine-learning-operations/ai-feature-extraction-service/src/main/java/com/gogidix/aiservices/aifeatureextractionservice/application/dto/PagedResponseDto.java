package com.gogidix.aiservices.aifeatureextractionservice.application.dto;

import java.util.List;

/**
 * Generic paged response DTO.
 */
public record PagedResponseDto<T>(
        List<T> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
