package com.gogidix.monitoring.monitoringdataservice.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for collecting multiple metrics in batch.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectBatchMetricsRequestDto {

    @NotEmpty(message = "At least one metric is required")
    @Valid
    private List<CollectMetricRequestDto> metrics;
}
