package com.gogidix.universal.tracking.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for tracking metric response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tracking metric response")
public class TrackingMetricResponseDto {

    @Schema(description = "Metric ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Metric name", example = "daily_page_views")
    private String metricName;

    @Schema(description = "Metric type", example = "COUNT")
    private String metricType;

    @Schema(description = "Metric value", example = "1500.0")
    private Double metricValue;

    @Schema(description = "Metric date", example = "2025-01-15")
    private LocalDate metricDate;

    @Schema(description = "Metric hour (0-23)", example = "10")
    private Integer metricHour;

    @Schema(description = "Dimensions")
    private String dimensions;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Event type", example = "PAGE_VIEW")
    private String eventType;

    @Schema(description = "Source", example = "WEB")
    private String source;

    @Schema(description = "Count", example = "1500")
    private Long count;

    @Schema(description = "Last updated timestamp")
    private LocalDateTime lastUpdatedAt;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
