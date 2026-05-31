package com.gogidix.finance.currency.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO - Error Response
 * Standard error response object for API errors
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDto(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path,
    String correlationId,
    String tenantId,
    List<FieldError> fieldErrors
) {

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FieldError(
        String field,
        String message,
        Object rejectedValue
    ) {}
}
