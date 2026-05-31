package com.gogidix.platform.metering.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for creating usage record.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUsageRecordRequestDto {

    @NotBlank(message = "Metric name is required")
    private String metricName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    private String unit;

    @NotNull(message = "Event time is required")
    private LocalDateTime eventTime;

    private Map<String, Object> dimensions;

    private String serviceName;

    private String resourceId;

    private String userId;

    private String correlationId;

    private Map<String, Object> metadata;
}
