package com.gogidix.shared.warehousing.warehouseconfig.application.dto;

import com.gogidix.shared.warehousing.warehouseconfig.domain.entity.AisleConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for Aisle Config
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AisleConfigDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String zoneId;
    private String aisleId;
    private String aisleName;
    private AisleConfig.AisleType aisleType;
    private Integer bayCount;
    private Integer levelCount;
    private Integer positionCount;
    private String orientation;
    private Double length;
    private AisleConfig.LengthUnit lengthUnit;
    private Double width;
    private AisleConfig.WidthUnit widthUnit;
    private Boolean hasLighting;
    private Boolean hasClimateControl;
    private AisleConfig.AisleStatus status;
    private List<String> equipmentIds;
    private Map<String, Object> attributes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
