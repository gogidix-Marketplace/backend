package com.gogidix.shared.warehousing.shipping.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Tracking Event Entity - Multi-tenant with MongoDB
 *
 * Represents a tracking event for a shipment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tracking_events")
@CompoundIndex(def = "{'tenantId': 1, 'shipmentId': 1, 'timestamp': -1}", name = "idx_tenant_shipment_time")
@Schema(description = "Tracking event entity for shipment tracking")
public class TrackingEvent {

    @Id
    @Schema(description = "Unique tracking event identifier")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Indexed
    @Schema(description = "Shipment identifier")
    private String shipmentId;

    @Indexed
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
    private TrackingEventType eventType;

    @Schema(description = "Carrier-specific event code")
    private String carrierEventCode;

    @Schema(description = "Additional event details")
    private String remarks;

    public enum TrackingEventType {
        PICKUP, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED,
        EXCEPTION, DELAYED, RETURNED, CANCELLED
    }
}
