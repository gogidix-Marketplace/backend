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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Aisle Configuration Entity
 *
 * Configuration for aisles within a zone
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "aisle_configs")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'zoneId': 1, 'aisleId': 1}", name = "idx_aisle_config_tenant")
public class AisleConfig {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String zoneId;

    @Indexed
    private String aisleId;

    private String aisleName;

    private AisleType aisleType;

    private Integer bayCount;

    private Integer levelCount;

    private Integer positionCount;

    private String orientation;

    private Double length;

    private LengthUnit lengthUnit;

    private Double width;

    private WidthUnit widthUnit;

    private Boolean hasLighting;

    private Boolean hasClimateControl;

    private AisleStatus status;

    private List<String> equipmentIds;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum AisleType {
        SELECTIVE_RACK,
        DRIVE_IN,
        PUSH_BACK,
        PALLET_FLOW,
        CANTILEVER,
        MEZZANINE,
        SHELVING,
        BIN_STORAGE,
        FLOOR_STORAGE,
        AUTOMATED
    }

    public enum LengthUnit {
        FEET,
        METERS,
        YARDS
    }

    public enum WidthUnit {
        INCHES,
        CENTIMETERS,
        FEET,
        METERS
    }

    public enum AisleStatus {
        ACTIVE,
        INACTIVE,
        MAINTENANCE,
        RESERVED,
        CLOSED
    }
}
