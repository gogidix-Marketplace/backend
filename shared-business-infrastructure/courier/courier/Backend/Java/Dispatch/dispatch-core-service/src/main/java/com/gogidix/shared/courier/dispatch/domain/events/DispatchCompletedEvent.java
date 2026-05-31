package com.gogidix.shared.courier.dispatch.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when a dispatch order is completed/delivered
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchCompletedEvent {

    private String eventId;

    private String tenantId;

    private String dispatchId;

    private String orderId;

    private String customerId;

    private String driverId;

    private String vehicleId;

    private LocalDateTime completedAt;

    private Double actualDistanceMeters;

    private Long actualDurationMinutes;

    private LocalDateTime occurredAt;
}
