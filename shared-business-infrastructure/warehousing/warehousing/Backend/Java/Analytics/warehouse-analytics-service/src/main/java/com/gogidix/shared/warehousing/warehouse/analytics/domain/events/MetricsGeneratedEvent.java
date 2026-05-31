package com.gogidix.shared.warehousing.warehouse.analytics.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when warehouse metrics are generated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricsGeneratedEvent {

    @Builder.Default
    private String eventId = java.util.UUID.randomUUID().toString();

    private String metricsId;
    private String warehouseId;
    private String tenantId;
    private Double utilizationPercentage;
    private Integer ordersProcessed;
    private LocalDateTime metricDate;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
