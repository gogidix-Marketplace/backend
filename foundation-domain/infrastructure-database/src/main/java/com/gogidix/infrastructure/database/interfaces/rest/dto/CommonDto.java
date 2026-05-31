package com.gogidix.infrastructure.database.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Common DTOs for REST API responses.
 */
public class CommonDto {

    /**
     * API Response wrapper.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        private LocalDateTime timestamp;
        private List<ApiError> errors;

        public static <T> ApiResponse<T> success(T data) {
            return ApiResponse.<T>builder()
                    .success(true)
                    .data(data)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        public static <T> ApiResponse<T> success(String message, T data) {
            return ApiResponse.<T>builder()
                    .success(true)
                    .message(message)
                    .data(data)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        public static <T> ApiResponse<T> error(String message) {
            return ApiResponse.<T>builder()
                    .success(false)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        public static <T> ApiResponse<T> error(String message, List<ApiError> errors) {
            return ApiResponse.<T>builder()
                    .success(false)
                    .message(message)
                    .errors(errors)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * API Error details.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiError {
        private String field;
        private String message;
        private String code;

        public static ApiError of(String field, String message) {
            return ApiError.builder()
                    .field(field)
                    .message(message)
                    .build();
        }
    }

    /**
     * Paged response.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedResponse<T> {
        private List<T> content;
        private int currentPage;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;
        private boolean empty;

        public static <T> PagedResponse<T> of(org.springframework.data.domain.Page<T> page) {
            return PagedResponse.<T>builder()
                    .content(page.getContent())
                    .currentPage(page.getNumber())
                    .pageSize(page.getSize())
                    .totalElements(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .first(page.isFirst())
                    .last(page.isLast())
                    .empty(page.isEmpty())
                    .build();
        }
    }

    /**
     * Statistics response.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatisticsResponse {
        private String tenantId;
        private long totalCount;
        private long activeCount;
        private long inactiveCount;
        private java.util.Map<String, Long> breakdownByStatus;
        private java.util.Map<String, Long> breakdownByType;
    }

    /**
     * Health check response.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthCheckResponse {
        private String status;
        private String component;
        private java.util.Map<String, Boolean> checks;
        private LocalDateTime timestamp;
        private String version;

        public static HealthCheckResponse healthy(String component) {
            return HealthCheckResponse.builder()
                    .status("UP")
                    .component(component)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        public static HealthCheckResponse unhealthy(String component) {
            return HealthCheckResponse.builder()
                    .status("DOWN")
                    .component(component)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }
}
