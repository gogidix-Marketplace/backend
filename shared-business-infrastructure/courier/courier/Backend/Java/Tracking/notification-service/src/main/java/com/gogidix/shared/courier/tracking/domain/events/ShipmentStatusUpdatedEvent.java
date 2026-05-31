package com.gogidix.shared.courier.tracking.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when shipment status changes
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentStatusUpdatedEvent {

    private String eventId;
    private String tenantId;
    private String shipmentId;
    private String trackingNumber;
    private String previousStatus;
    private String newStatus;
    private String location;
    private String description;
    private LocalDateTime statusChangedAt;
    private String changedBy;
    private LocalDateTime createdAt;
}
