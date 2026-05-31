package com.gogidix.shared.courier.dispatch.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when a dispatch order is assigned to a driver
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchAssignedEvent {

    private String eventId;

    private String tenantId;

    private String dispatchId;

    private String orderId;

    private String driverId;

    private String vehicleId;

    private LocalDateTime assignedAt;

    private LocalDateTime occurredAt;
}
