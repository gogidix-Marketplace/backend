package com.gogidix.shared.warehousing.warehouse.analytics.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when a utilization report is generated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportGeneratedEvent {

    @Builder.Default
    private String eventId = java.util.UUID.randomUUID().toString();

    private String reportId;
    private String warehouseId;
    private String tenantId;
    private LocalDateTime reportDate;
    private Double overallSpaceUtilization;
    private String status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
