package com.gogidix.monitoring.performance.infrastructure.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Event class for alert publication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertEvent {

    private String alertId;
    private String tenantId;
    private String serviceId;
    private String message;
    private Instant timestamp;
}
