package com.gogidix.dashboard.gateway.chart.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for chart summary response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartSummaryResponseDto {

    private Long totalCharts;
    private Long activeCharts;
    private Long totalDataPoints;
    private Map<String, Long> chartsByType;
    private String timestamp;
}
