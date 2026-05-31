package com.gogidix.dashboard.gateway.chart.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for chart data response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartDataResponseDto {

    private String chartId;
    private String chartName;
    private String chartType;
    private List<DataPoint> data;
    private Map<String, Object> metadata;
    private LocalDateTime generatedAt;
    private String tenantId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataPoint {
        private String label;
        private LocalDateTime timestamp;
        private Double value;
        private Map<String, Object> metadata;
    }
}
