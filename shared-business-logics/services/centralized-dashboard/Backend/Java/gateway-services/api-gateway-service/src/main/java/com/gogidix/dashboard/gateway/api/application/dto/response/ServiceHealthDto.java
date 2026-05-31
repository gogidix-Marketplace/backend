package com.gogidix.dashboard.gateway.api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Service health status DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceHealthDto {

    private String serviceName;
    private String status;
    private String baseUrl;
    private LocalDateTime lastChecked;
    private Long responseTimeMs;
    private Integer retryCount;
    private Map<String, Object> details;

    public enum Status {
        UP, DOWN, DEGRADED, UNKNOWN
    }
}
