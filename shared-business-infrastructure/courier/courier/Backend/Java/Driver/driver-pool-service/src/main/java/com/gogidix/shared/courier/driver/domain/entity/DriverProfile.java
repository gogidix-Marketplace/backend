package com.gogidix.shared.courier.driver.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;

/**
 * Driver Profile Entity with Geospatial Support
 * Multi-tenant document for driver pool management
 */
@Document(collection = "driver_profiles")
@CompoundIndex(def = "{'tenantId': 1, 'driverId': 1}", unique = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverProfile {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String driverId;

    private String fullName;
    private String email;
    private String phone;

    private DriverStatus status;

    // GEOSPATIAL INDEX for real-time location tracking
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint currentLocation;

    private String vehicleType;
    private String licensePlate;

    // Performance metrics
    private Double rating;
    private Integer totalDeliveries;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
