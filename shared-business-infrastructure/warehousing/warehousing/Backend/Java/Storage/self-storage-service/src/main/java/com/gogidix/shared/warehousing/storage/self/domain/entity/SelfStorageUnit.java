package com.gogidix.shared.warehousing.storage.self.domain.entity;

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
 * Self Storage Unit entity
 * Represents self-storage rental units
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "self_storage_units")
public class SelfStorageUnit {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String facilityId;

    @Indexed
    private String unitNumber;

    private String unitName;

    private UnitType unitType;

    private UnitStatus status;

    private Dimensions dimensions;

    private Double area;

    private String areaUnit;

    private Features features;

    private Double basePrice;

    private String currency;

    private PricingInfo pricingInfo;

    private AccessInfo accessInfo;

    private List<String> amenities;

    private String temperatureZone;

    private Boolean climateControlled;

    private Boolean interior;

    private Boolean groundFloor;

    private Integer floorNumber;

    private String currentRentalId;

    private String currentTenantId;

    private String currentTenantName;

    private LocalDateTime rentalStartDate;

    private LocalDateTime rentalEndDate;

    private Boolean depositRequired;

    private Double depositAmount;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime updatedAt;

    private LocalDateTime lastInspectionDate;

    private String notes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dimensions {
        private Double length;
        private Double width;
        private Double height;
        private String unit;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Features {
        private Boolean powerOutlet;
        private Boolean lighting;
        private Boolean shelvingIncluded;
        private Boolean humidityControl;
        private Boolean alarmSystem;
        private Boolean cctvMonitoring;
        private Boolean driveUpAccess;
        private Boolean elevatorAccess;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PricingInfo {
        private Double weeklyPrice;
        private Double monthlyPrice;
        private Double quarterlyPrice;
        private Double annualPrice;
        private Double securityDeposit;
        private Double adminFee;
        private String currency;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessInfo {
        private String accessHours;
        private Boolean twentyFourAccess;
        private String gateCode;
        private String keyCardNumber;
        private List<String> accessMethods;
    }

    public enum UnitType {
        LOCKER,
        SMALL,
        MEDIUM,
        LARGE,
        EXTRA_LARGE,
        WAREHOUSE,
        PARKING,
        BOAT,
        RV
    }

    public enum UnitStatus {
        AVAILABLE,
        OCCUPIED,
        RESERVED,
        MAINTENANCE,
        CLEANING,
        UNAVAILABLE
    }

    public boolean isAvailable() {
        return status == UnitStatus.AVAILABLE;
    }

    public boolean isOccupied() {
        return status == UnitStatus.OCCUPIED;
    }

    public void rentOut(String rentalId, String tenantId, String tenantName, LocalDateTime startDate) {
        this.status = UnitStatus.OCCUPIED;
        this.currentRentalId = rentalId;
        this.currentTenantId = tenantId;
        this.currentTenantName = tenantName;
        this.rentalStartDate = startDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void vacate() {
        this.status = UnitStatus.AVAILABLE;
        this.currentRentalId = null;
        this.currentTenantId = null;
        this.currentTenantName = null;
        this.rentalEndDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void reserve(String tenantId) {
        this.status = UnitStatus.RESERVED;
        this.currentTenantId = tenantId;
        this.updatedAt = LocalDateTime.now();
    }
}
