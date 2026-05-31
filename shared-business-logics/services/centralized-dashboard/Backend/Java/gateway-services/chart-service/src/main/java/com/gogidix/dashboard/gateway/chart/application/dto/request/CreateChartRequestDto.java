package com.gogidix.dashboard.gateway.chart.application.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for creating a new chart configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateChartRequestDto {

    @NotBlank(message = "Chart ID is required")
    private String chartId;

    @NotBlank(message = "Chart name is required")
    private String chartName;

    @NotBlank(message = "Chart type is required")
    private String chartType;

    private String description;

    private String dataSource;

    private String query;

    private Map<String, Object> config;

    private Integer refreshIntervalSeconds;

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;
}
