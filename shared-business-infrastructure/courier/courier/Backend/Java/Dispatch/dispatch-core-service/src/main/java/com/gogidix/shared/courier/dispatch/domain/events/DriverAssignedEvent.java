package com.gogidix.shared.courier.dispatch.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when a driver is assigned to a dispatch order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverAssignedEvent {

    private String eventId;

    private String tenantId;

    private String dispatchId;

    private String orderId;

    private String driverId;

    private String vehicleId;

    private LocalDateTime assignedAt;

    private LocalDateTime occurredAt;
}
