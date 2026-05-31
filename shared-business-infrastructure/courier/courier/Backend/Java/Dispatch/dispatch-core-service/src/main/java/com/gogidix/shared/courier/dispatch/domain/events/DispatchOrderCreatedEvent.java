package com.gogidix.shared.courier.dispatch.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when a new dispatch order is created
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchOrderCreatedEvent {

    private String eventId;

    private String tenantId;

    private String dispatchId;

    private String orderId;

    private String customerId;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double deliveryLatitude;

    private Double deliveryLongitude;

    private Integer priority;

    private String status;

    private LocalDateTime occurredAt;
}
