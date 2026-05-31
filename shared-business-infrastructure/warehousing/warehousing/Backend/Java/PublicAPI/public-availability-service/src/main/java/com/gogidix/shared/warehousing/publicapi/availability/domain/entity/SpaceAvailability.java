package com.gogidix.shared.warehousing.publicapi.availability.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Space Availability entity
 * Represents public availability information for warehouse spaces
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "space_availabilities")
public class SpaceAvailability {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String warehouseName;

    private String warehouseCode;

    private String address;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    private Double latitude;

    private Double longitude;

    @Indexed
    private LocalDateTime availableFrom;

    @Indexed
    private LocalDateTime availableUntil;

    private AvailabilityStatus status;

    private SpaceTypeInfo spaceType;

    private Double availableArea;

    private String areaUnit;

    private Double availableVolume;

    private String volumeUnit;

    private Double maxWeightCapacity;

    private String weightUnit;

    private Double pricePerUnit;

    private String currency;

    private String pricingUnit;

    private List<String> features;

    private String temperatureZone;

    private Boolean climateControlled;

    private Boolean hazardousAllowed;

    private String contactEmail;

    private String contactPhone;

    private String websiteUrl;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime updatedAt;

    private LocalDateTime lastVerifiedAt;

    private String notes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpaceTypeInfo {
        private String spaceType;
        private String category;
        private String description;
        private List<String> allowedProductTypes;
    }

    public enum AvailabilityStatus {
        AVAILABLE,
        RESERVED,
        OCCUPIED,
        UNAVAILABLE,
        COMING_SOON,
        MAINTENANCE
    }

    public boolean isAvailable() {
        return status == AvailabilityStatus.AVAILABLE
                && availableFrom != null
                && !availableFrom.isAfter(LocalDateTime.now())
                && (availableUntil == null || availableUntil.isAfter(LocalDateTime.now()));
    }

    public boolean isAvailableAt(LocalDateTime date) {
        return status == AvailabilityStatus.AVAILABLE
                && availableFrom != null
                && !availableFrom.isAfter(date)
                && (availableUntil == null || availableUntil.isAfter(date));
    }
}
