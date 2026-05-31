package com.gogidix.shared.courier.driver.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain event published when driver location is updated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverLocationUpdatedEvent {

    private String eventId;
    private String tenantId;
    private String driverId;
    private List<Double> newLocation; // [longitude, latitude]
    private LocalDateTime occurredAt;
}
