package com.gogidix.shared.courier.dispatch.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when a dispatch order is cancelled
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchCancelledEvent {

    private String eventId;

    private String tenantId;

    private String dispatchId;

    private String orderId;

    private String customerId;

    private String cancelledBy;

    private String cancellationReason;

    private LocalDateTime cancelledAt;

    private LocalDateTime occurredAt;
}
