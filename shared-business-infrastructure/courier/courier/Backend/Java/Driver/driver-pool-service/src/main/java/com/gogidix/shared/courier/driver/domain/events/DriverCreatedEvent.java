package com.gogidix.shared.courier.driver.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when a new driver is created
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverCreatedEvent {

    private String eventId;
    private String tenantId;
    private String driverId;
    private String fullName;
    private String email;
    private String vehicleType;
    private LocalDateTime occurredAt;
}
