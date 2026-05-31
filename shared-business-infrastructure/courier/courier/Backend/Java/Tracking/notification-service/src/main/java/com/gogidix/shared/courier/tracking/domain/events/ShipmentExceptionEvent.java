package com.gogidix.shared.courier.tracking.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain event published when exception occurs
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentExceptionEvent {

    private String eventId;
    private String tenantId;
    private String shipmentId;
    private String trackingNumber;
    private String exceptionType;
    private String exceptionCode;
    private String description;
    private String location;
    private Double latitude;
    private Double longitude;
    private LocalDateTime occurredAt;
    private String reportedBy;
    private String assignedTo;
    private LocalDateTime expectedResolution;
    private Boolean resolved;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
}
