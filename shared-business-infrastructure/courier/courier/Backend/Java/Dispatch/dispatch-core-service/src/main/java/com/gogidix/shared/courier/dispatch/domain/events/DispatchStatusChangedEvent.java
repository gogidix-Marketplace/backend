package com.gogidix.shared.courier.dispatch.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when dispatch order status changes
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchStatusChangedEvent {

    private String eventId;

    private String tenantId;

    private String dispatchId;

    private String orderId;

    private String oldStatus;

    private String newStatus;

    private LocalDateTime occurredAt;
}
