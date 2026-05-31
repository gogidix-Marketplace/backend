package com.gogidix.shared.warehousing.warehouseconfig.application.dto;

import com.gogidix.shared.warehousing.warehouseconfig.domain.entity.ZoneConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for Zone Config
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneConfigDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String zoneId;
    private String zoneName;
    private ZoneConfig.ZoneType zoneType;
    private BigDecimal area;
    private ZoneConfig.AreaUnit areaUnit;
    private Integer capacityPallets;
    private Integer capacityBins;
    private BigDecimal maxWeight;
    private ZoneConfig.WeightUnit weightUnit;
    private ZoneConfig.TemperatureRange temperatureRange;
    private Boolean climateControlled;
    private Boolean secured;
    private ZoneConfig.SecurityLevel securityLevel;
    private List<String> allowedItemTypes;
    private List<String> forbiddenItemTypes;
    private String locationDescription;
    private ZoneConfig.ZoneStatus status;
    private Integer priority;
    private Map<String, Object> attributes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
