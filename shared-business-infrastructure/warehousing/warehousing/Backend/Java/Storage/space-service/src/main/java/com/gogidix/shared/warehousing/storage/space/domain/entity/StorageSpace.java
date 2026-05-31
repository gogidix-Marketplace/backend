package com.gogidix.shared.warehousing.storage.space.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Storage Space entity
 * Represents a storage space unit within a warehouse
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "storage_spaces")
@CompoundIndex(def = "{'tenantId': 1, 'id': 1}", name = "idx_tenant_id")
@CompoundIndex(def = "{'tenantId': 1, 'spaceCode': 1}", name = "idx_tenant_space_code", unique = true)
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1}", name = "idx_tenant_warehouse")
@CompoundIndex(def = "{'tenantId': 1, 'status': 1}", name = "idx_tenant_status")
public class StorageSpace {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String spaceCode;

    private String spaceName;

    private SpaceType spaceType;

    private SpaceStatus status;

    private Location location;

    private Dimensions dimensions;

    private Capacity capacity;

    private OccupancyInfo occupancyInfo;

    private List<String> allowedProductTypes;

    private List<String> requiredEquipment;

    private String temperatureZone;

    private Boolean hazardousMaterialAllowed;

    private String currentContentId;

    private String currentContentType;

    private LocalDateTime lastInspectionDate;

    private String notes;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private String zone;
        private String aisle;
        private String bay;
        private String level;
        private String position;
        private Double latitude;
        private Double longitude;
    }

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
    public static class Capacity {
        private Double weightCapacity;
        private Double volumeCapacity;
        private Integer itemCapacity;
        private String weightUnit;
        private String volumeUnit;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OccupancyInfo {
        private Double currentWeight;
        private Double currentVolume;
        private Integer currentItemCount;
        private Double utilizationPercentage;
    }

    public enum SpaceType {
        SHELF,
        BIN,
        PALLET_POSITION,
        FLOOR_SPACE,
        MEZZANINE,
        COLD_ROOM,
        FREEZER,
        HAZARDOUS_STORAGE,
        LOCKER,
        RACK
    }

    public enum SpaceStatus {
        AVAILABLE,
        OCCUPIED,
        RESERVED,
        MAINTENANCE,
        BLOCKED,
        CLEANING,
        UNAVAILABLE
    }

    public boolean isAvailable() {
        return status == SpaceStatus.AVAILABLE;
    }

    public boolean isOccupied() {
        return status == SpaceStatus.OCCUPIED;
    }

    public Double calculateUtilization() {
        if (capacity == null || capacity.getVolumeCapacity() == null || occupancyInfo == null) {
            return 0.0;
        }
        return (occupancyInfo.getCurrentVolume() / capacity.getVolumeCapacity()) * 100.0;
    }

    public void occupy(String contentId, String contentType) {
        this.status = SpaceStatus.OCCUPIED;
        this.currentContentId = contentId;
        this.currentContentType = contentType;
        this.updatedAt = LocalDateTime.now();
    }

    public void vacate() {
        this.status = SpaceStatus.AVAILABLE;
        this.currentContentId = null;
        this.currentContentType = null;
        if (occupancyInfo != null) {
            occupancyInfo.setCurrentWeight(0.0);
            occupancyInfo.setCurrentVolume(0.0);
            occupancyInfo.setCurrentItemCount(0);
            occupancyInfo.setUtilizationPercentage(0.0);
        }
        this.updatedAt = LocalDateTime.now();
    }
}
