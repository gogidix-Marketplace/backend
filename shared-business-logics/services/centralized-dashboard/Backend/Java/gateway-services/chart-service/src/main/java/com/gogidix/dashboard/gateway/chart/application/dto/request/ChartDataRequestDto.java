package com.gogidix.dashboard.gateway.chart.application.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for requesting chart data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartDataRequestDto {

    @NotBlank(message = "Chart ID is required")
    private String chartId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String aggregation;

    private Long intervalMinutes;

    private Integer limit;
}
