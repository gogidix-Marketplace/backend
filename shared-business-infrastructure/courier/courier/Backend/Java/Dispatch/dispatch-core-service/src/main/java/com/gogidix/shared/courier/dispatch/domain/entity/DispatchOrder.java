package com.gogidix.shared.courier.dispatch.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Dispatch Order entity with geospatial support
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dispatch_orders")
@CompoundIndex(def = "{'tenantId': 1, 'dispatchId': 1}", unique = true)
@CompoundIndex(def = "{'tenantId': 1, 'status': 1}")
@CompoundIndex(def = "{'tenantId': 1, 'priority': -1}")
public class DispatchOrder {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Indexed
    private String dispatchId;

    private String orderId;
    private String customerId;

    // GEOSPATIAL INDEXES for location queries
    @GeoSpatialIndexed(type = org.springframework.data.mongodb.core.index.GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint pickupLocation;

    @GeoSpatialIndexed(type = org.springframework.data.mongodb.core.index.GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint deliveryLocation;

    private String pickupAddress;
    private String deliveryAddress;

    private DispatchStatus status;
    private Integer priority;

    private String assignedDriverId;
    private String assignedVehicleId;

    private LocalDateTime estimatedPickupTime;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualPickupTime;
    private LocalDateTime actualDeliveryTime;

    private Map<String, Object> metadata;

    private Double distanceMeters;
    private Double estimatedDurationMinutes;

    private Double totalAmount;
    private String currency;
}
