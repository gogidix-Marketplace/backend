package com.gogidix.dashboard.gateway.chart.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for chart configuration response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartConfigurationResponseDto {

    private Long id;
    private String chartId;
    private String chartName;
    private String chartType;
    private String tenantId;
    private String description;
    private String dataSource;
    private String query;
    private Map<String, Object> config;
    private Integer refreshIntervalSeconds;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
