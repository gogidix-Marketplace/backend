package com.gogidix.shared.warehousing.warehouseconfig.application.dto;

import com.gogidix.shared.warehousing.warehouseconfig.domain.entity.WarehouseConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for Warehouse Config
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseConfigDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String warehouseName;
    private String warehouseCode;
    private WarehouseConfig.WarehouseType warehouseType;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private BigDecimal totalArea;
    private WarehouseConfig.AreaUnit areaUnit;
    private Integer totalZones;
    private Integer capacityPallets;
    private Integer capacityBins;
    private WarehouseConfig.OperatingHours operatingHours;
    private WarehouseConfig.WarehouseStatus status;
    private String timeZone;
    private String currency;
    private List<String> supportedItemTypes;
    private List<String> supportedOperations;
    private Map<String, Object> attributes;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
