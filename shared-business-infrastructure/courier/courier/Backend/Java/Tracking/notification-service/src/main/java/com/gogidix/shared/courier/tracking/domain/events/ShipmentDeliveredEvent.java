package com.gogidix.shared.courier.tracking.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when a shipment is delivered
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDeliveredEvent {

    private String eventId;
    private String tenantId;
    private String shipmentId;
    private String trackingNumber;
    private String recipientName;
    private String recipientAddress;
    private LocalDateTime deliveredAt;
    private String deliveredBy;
    private String signature;
    private String notes;
    private LocalDateTime createdAt;
}
