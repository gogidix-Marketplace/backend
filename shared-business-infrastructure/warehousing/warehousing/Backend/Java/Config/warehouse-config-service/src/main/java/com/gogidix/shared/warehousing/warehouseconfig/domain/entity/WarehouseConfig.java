package com.gogidix.shared.warehousing.warehouseconfig.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Warehouse Configuration Entity
 *
 * Main configuration for a warehouse
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "warehouse_configs")
public class WarehouseConfig {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String warehouseName;

    private String warehouseCode;

    private WarehouseType warehouseType;

    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private BigDecimal totalArea;

    private AreaUnit areaUnit;

    private Integer totalZones;

    private Integer capacityPallets;

    private Integer capacityBins;

    private OperatingHours operatingHours;

    private WarehouseStatus status;

    private String timeZone;

    private String currency;

    private List<String> supportedItemTypes;

    private List<String> supportedOperations;

    private Map<String, Object> attributes;

    private Boolean active;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum WarehouseType {
        DISTRIBUTION_CENTER,
        FULFILLMENT_CENTER,
        COLD_STORAGE,
        CROSS_DOCK,
        WAREHOUSE,
        SORTING_CENTER,
        RETURNS_CENTER
    }

    public enum AreaUnit {
        SQUARE_FEET,
        SQUARE_METERS,
        SQUARE_YARDS
    }

    public enum WarehouseStatus {
        ACTIVE,
        INACTIVE,
        MAINTENANCE,
        UNDER_CONSTRUCTION,
        CLOSED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperatingHours {
        private String monday;
        private String tuesday;
        private String wednesday;
        private String thursday;
        private String friday;
        private String saturday;
        private String sunday;
        private String timezone;
    }
}
