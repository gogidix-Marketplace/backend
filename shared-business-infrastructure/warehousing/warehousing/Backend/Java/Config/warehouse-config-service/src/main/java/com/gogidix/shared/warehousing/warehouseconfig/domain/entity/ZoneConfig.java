package com.gogidix.shared.warehousing.warehouseconfig.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Zone Configuration Entity
 *
 * Configuration for zones within a warehouse
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "zone_configs")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'zoneId': 1}", name = "idx_zone_config_tenant")
public class ZoneConfig {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String zoneId;

    private String zoneName;

    private ZoneType zoneType;

    private BigDecimal area;

    private AreaUnit areaUnit;

    private Integer capacityPallets;

    private Integer capacityBins;

    private BigDecimal maxWeight;

    private WeightUnit weightUnit;

    private TemperatureRange temperatureRange;

    private Boolean climateControlled;

    private Boolean secured;

    private SecurityLevel securityLevel;

    private List<String> allowedItemTypes;

    private List<String> forbiddenItemTypes;

    private String locationDescription;

    private ZoneStatus status;

    private Integer priority;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum ZoneType {
        RECEIVING,
        PUTAWAY,
        STORAGE,
        PICKING,
        PACKING,
        SHIPPING,
        STAGING,
        RETURNS,
        DAMAGED_GOODS,
        QUALITY_CONTROL,
        CROSS_DOCK,
        BULK_STORAGE,
        CLIMATE_CONTROLLED,
        FREEZER,
        COOLER,
        HAZMAT,
        HIGH_VALUE,
        VAULT
    }

    public enum AreaUnit {
        SQUARE_FEET,
        SQUARE_METERS,
        SQUARE_YARDS
    }

    public enum WeightUnit {
        KILOGRAMS,
        POUNDS,
        TONS
    }

    public enum SecurityLevel {
        LOW,
        MEDIUM,
        HIGH,
        RESTRICTED,
        MAXIMUM
    }

    public enum ZoneStatus {
        ACTIVE,
        INACTIVE,
        MAINTENANCE,
        FULL,
        CLOSED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemperatureRange {
        private Double minCelsius;
        private Double maxCelsius;
        private Double minFahrenheit;
        private Double maxFahrenheit;
    }
}
