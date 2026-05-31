package com.gogidix.shared.warehousing.shipping.application.dto;

import com.gogidix.shared.warehousing.shipping.domain.entity.TrackingEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Tracking Event Data Transfer Object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tracking Event DTO")
public class TrackingEventDTO {

    @Schema(description = "Unique tracking event identifier")
    private String id;

    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Schema(description = "Shipment identifier")
    private String shipmentId;

    @Schema(description = "Tracking number")
    private String trackingNumber;

    @Schema(description = "Event status code")
    private String statusCode;

    @Schema(description = "Event status description")
    private String statusDescription;

    @Schema(description = "Event location")
    private String location;

    @Schema(description = "Event timestamp")
    private LocalDateTime timestamp;

    @Schema(description = "Event type")
    private TrackingEvent.TrackingEventType eventType;

    @Schema(description = "Carrier-specific event code")
    private String carrierEventCode;

    @Schema(description = "Additional event details")
    private String remarks;
}
