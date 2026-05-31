package com.gogidix.shared.courier.driver.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when driver status changes
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverStatusChangedEvent {

    private String eventId;
    private String tenantId;
    private String driverId;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime occurredAt;
}
