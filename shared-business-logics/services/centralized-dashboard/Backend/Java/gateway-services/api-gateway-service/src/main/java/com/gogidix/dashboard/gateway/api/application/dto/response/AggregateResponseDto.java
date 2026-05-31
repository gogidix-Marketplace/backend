package com.gogidix.dashboard.gateway.api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Generic aggregate response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregateResponseDto {

    private String endpoint;
    private String serviceName;
    private Object data;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;
    private Long durationMs;
    private String correlationId;
    private String tenantId;

    @Builder.Default
    private Boolean cached = false;

    @Builder.Default
    private Boolean success = true;

    private String error;
    private Integer statusCode;
}
