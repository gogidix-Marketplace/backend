package com.gogidix.shared.warehousing.warehouseconfig.application.service;

import com.gogidix.shared.warehousing.warehouseconfig.application.dto.*;
import com.gogidix.shared.warehousing.warehouseconfig.domain.entity.*;
import com.gogidix.shared.warehousing.warehouseconfig.domain.exception.ConfigNotFoundException;
import com.gogidix.shared.warehousing.warehouseconfig.domain.repository.AisleConfigRepository;
import com.gogidix.shared.warehousing.warehouseconfig.domain.repository.WarehouseConfigRepository;
import com.gogidix.shared.warehousing.warehouseconfig.domain.repository.ZoneConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Warehouse Config Service
 *
 * Handles warehouse configuration management
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseConfigService {

    private final WarehouseConfigRepository warehouseConfigRepository;
    private final ZoneConfigRepository zoneConfigRepository;
    private final AisleConfigRepository aisleConfigRepository;

    /**
     * Create warehouse config
     */
    public WarehouseConfigDTO createWarehouseConfig(CreateWarehouseConfigCommand command) {
        log.info("Creating warehouse config for: {}", command.getWarehouseName());

        String tenantId = "current-tenant";

        WarehouseConfig config = WarehouseConfig.builder()
            .tenantId(tenantId)
            .warehouseId(command.getWarehouseId())
            .warehouseName(command.getWarehouseName())
            .warehouseCode(command.getWarehouseCode())
            .warehouseType(command.getWarehouseType())
            .address(command.getAddress())
            .city(command.getCity())
            .state(command.getState())
            .country(command.getCountry())
            .postalCode(command.getPostalCode())
            .totalArea(command.getTotalArea())
            .areaUnit(command.getAreaUnit())
            .totalZones(0)
            .capacityPallets(command.getCapacityPallets())
            .capacityBins(command.getCapacityBins())
            .operatingHours(command.getOperatingHours())
            .status(WarehouseConfig.WarehouseStatus.ACTIVE)
            .timeZone(command.getTimeZone())
            .currency(command.getCurrency())
            .supportedItemTypes(command.getSupportedItemTypes())
            .supportedOperations(command.getSupportedOperations())
            .attributes(command.getAttributes())
            .active(true)
            .build();

        WarehouseConfig saved = warehouseConfigRepository.save(config);
        log.info("Warehouse config created with ID: {}", saved.getId());

        return toDTO(saved);
    }

    /**
     * Get warehouse config
     */
    @Transactional(readOnly = true)
    public WarehouseConfigDTO getWarehouseConfig(String warehouseId) {
        String tenantId = "current-tenant";
        WarehouseConfig config = warehouseConfigRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId)
            .orElseThrow(() -> new ConfigNotFoundException("Warehouse config not found: " + warehouseId));
        return toDTO(config);
    }

    /**
     * Update warehouse config
     */
    public WarehouseConfigDTO updateWarehouseConfig(String warehouseId, UpdateWarehouseConfigCommand command) {
        log.info("Updating warehouse config: {}", warehouseId);

        String tenantId = "current-tenant";
        WarehouseConfig config = warehouseConfigRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId)
            .orElseThrow(() -> new ConfigNotFoundException("Warehouse config not found: " + warehouseId));

        // Update fields
        if (command.getWarehouseName() != null) config.setWarehouseName(command.getWarehouseName());
        if (command.getStatus() != null) config.setStatus(command.getStatus());
        if (command.getOperatingHours() != null) config.setOperatingHours(command.getOperatingHours());
        if (command.getTotalArea() != null) config.setTotalArea(command.getTotalArea());

        WarehouseConfig saved = warehouseConfigRepository.save(config);
        return toDTO(saved);
    }

    /**
     * Create zone config
     */
    public ZoneConfigDTO createZoneConfig(CreateZoneConfigCommand command) {
        log.info("Creating zone config for warehouse: {}", command.getWarehouseId());

        String tenantId = "current-tenant";

        ZoneConfig config = ZoneConfig.builder()
            .tenantId(tenantId)
            .warehouseId(command.getWarehouseId())
            .zoneId(command.getZoneId())
            .zoneName(command.getZoneName())
            .zoneType(command.getZoneType())
            .area(command.getArea())
            .areaUnit(command.getAreaUnit())
            .capacityPallets(command.getCapacityPallets())
            .capacityBins(command.getCapacityBins())
            .maxWeight(command.getMaxWeight())
            .weightUnit(command.getWeightUnit())
            .temperatureRange(command.getTemperatureRange())
            .climateControlled(command.getClimatedControlled())
            .secured(command.getSecured())
            .securityLevel(command.getSecurityLevel())
            .allowedItemTypes(command.getAllowedItemTypes())
            .forbiddenItemTypes(command.getForbiddenItemTypes())
            .locationDescription(command.getLocationDescription())
            .status(ZoneConfig.ZoneStatus.ACTIVE)
            .priority(command.getPriority() != null ? command.getPriority() : 0)
            .attributes(command.getAttributes())
            .build();

        ZoneConfig saved = zoneConfigRepository.save(config);

        // Update warehouse zone count
        warehouseConfigRepository.findByTenantIdAndWarehouseId(tenantId, command.getWarehouseId())
            .ifPresent(warehouse -> {
                warehouse.setTotalZones(warehouse.getTotalZones() + 1);
                warehouseConfigRepository.save(warehouse);
            });

        log.info("Zone config created with ID: {}", saved.getId());
        return toDTO(saved);
    }

    /**
     * Get zones for warehouse
     */
    @Transactional(readOnly = true)
    public List<ZoneConfigDTO> getZones(String warehouseId) {
        String tenantId = "current-tenant";
        return zoneConfigRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Create aisle config
     */
    public AisleConfigDTO createAisleConfig(CreateAisleConfigCommand command) {
        log.info("Creating aisle config for zone: {}", command.getZoneId());

        String tenantId = "current-tenant";

        AisleConfig config = AisleConfig.builder()
            .tenantId(tenantId)
            .warehouseId(command.getWarehouseId())
            .zoneId(command.getZoneId())
            .aisleId(command.getAisleId())
            .aisleName(command.getAisleName())
            .aisleType(command.getAisleType())
            .bayCount(command.getBayCount())
            .levelCount(command.getLevelCount())
            .positionCount(command.getPositionCount())
            .orientation(command.getOrientation())
            .length(command.getLength())
            .lengthUnit(command.getLengthUnit())
            .width(command.getWidth())
            .widthUnit(command.getWidthUnit())
            .hasLighting(command.getHasLighting())
            .hasClimateControl(command.getHasClimateControl())
            .status(AisleConfig.AisleStatus.ACTIVE)
            .equipmentIds(command.getEquipmentIds())
            .attributes(command.getAttributes())
            .build();

        AisleConfig saved = aisleConfigRepository.save(config);
        log.info("Aisle config created with ID: {}", saved.getId());

        return toDTO(saved);
    }

    /**
     * Get aisles for zone
     */
    @Transactional(readOnly = true)
    public List<AisleConfigDTO> getAisles(String warehouseId, String zoneId) {
        String tenantId = "current-tenant";
        return aisleConfigRepository.findByTenantIdAndWarehouseIdAndZoneId(tenantId, warehouseId, zoneId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Delete warehouse config
     */
    public void deleteWarehouseConfig(String warehouseId) {
        log.info("Deleting warehouse config: {}", warehouseId);

        String tenantId = "current-tenant";
        WarehouseConfig config = warehouseConfigRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId)
            .orElseThrow(() -> new ConfigNotFoundException("Warehouse config not found: " + warehouseId));

        warehouseConfigRepository.delete(config);
        log.info("Warehouse config deleted: {}", warehouseId);
    }

    private WarehouseConfigDTO toDTO(WarehouseConfig config) {
        return WarehouseConfigDTO.builder()
            .id(config.getId())
            .tenantId(config.getTenantId())
            .warehouseId(config.getWarehouseId())
            .warehouseName(config.getWarehouseName())
            .warehouseCode(config.getWarehouseCode())
            .warehouseType(config.getWarehouseType())
            .address(config.getAddress())
            .city(config.getCity())
            .state(config.getState())
            .country(config.getCountry())
            .postalCode(config.getPostalCode())
            .totalArea(config.getTotalArea())
            .areaUnit(config.getAreaUnit())
            .totalZones(config.getTotalZones())
            .capacityPallets(config.getCapacityPallets())
            .capacityBins(config.getCapacityBins())
            .operatingHours(config.getOperatingHours())
            .status(config.getStatus())
            .timeZone(config.getTimeZone())
            .currency(config.getCurrency())
            .supportedItemTypes(config.getSupportedItemTypes())
            .supportedOperations(config.getSupportedOperations())
            .attributes(config.getAttributes())
            .active(config.getActive())
            .createdAt(config.getCreatedAt())
            .updatedAt(config.getUpdatedAt())
            .build();
    }

    private ZoneConfigDTO toDTO(ZoneConfig config) {
        return ZoneConfigDTO.builder()
            .id(config.getId())
            .tenantId(config.getTenantId())
            .warehouseId(config.getWarehouseId())
            .zoneId(config.getZoneId())
            .zoneName(config.getZoneName())
            .zoneType(config.getZoneType())
            .area(config.getArea())
            .areaUnit(config.getAreaUnit())
            .capacityPallets(config.getCapacityPallets())
            .capacityBins(config.getCapacityBins())
            .maxWeight(config.getMaxWeight())
            .weightUnit(config.getWeightUnit())
            .temperatureRange(config.getTemperatureRange())
            .climateControlled(config.getClimateControlled())
            .secured(config.getSecured())
            .securityLevel(config.getSecurityLevel())
            .allowedItemTypes(config.getAllowedItemTypes())
            .forbiddenItemTypes(config.getForbiddenItemTypes())
            .locationDescription(config.getLocationDescription())
            .status(config.getStatus())
            .priority(config.getPriority())
            .attributes(config.getAttributes())
            .createdAt(config.getCreatedAt())
            .updatedAt(config.getUpdatedAt())
            .build();
    }

    private AisleConfigDTO toDTO(AisleConfig config) {
        return AisleConfigDTO.builder()
            .id(config.getId())
            .tenantId(config.getTenantId())
            .warehouseId(config.getWarehouseId())
            .zoneId(config.getZoneId())
            .aisleId(config.getAisleId())
            .aisleName(config.getAisleName())
            .aisleType(config.getAisleType())
            .bayCount(config.getBayCount())
            .levelCount(config.getLevelCount())
            .positionCount(config.getPositionCount())
            .orientation(config.getOrientation())
            .length(config.getLength())
            .lengthUnit(config.getLengthUnit())
            .width(config.getWidth())
            .widthUnit(config.getWidthUnit())
            .hasLighting(config.getHasLighting())
            .hasClimateControl(config.getHasClimateControl())
            .status(config.getStatus())
            .equipmentIds(config.getEquipmentIds())
            .attributes(config.getAttributes())
            .createdAt(config.getCreatedAt())
            .updatedAt(config.getUpdatedAt())
            .build();
    }

    /**
     * Command classes
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CreateWarehouseConfigCommand {
        private String warehouseId;
        private String warehouseName;
        private String warehouseCode;
        private WarehouseConfig.WarehouseType warehouseType;
        private String address;
        private String city;
        private String state;
        private String country;
        private String postalCode;
        private java.math.BigDecimal totalArea;
        private WarehouseConfig.AreaUnit areaUnit;
        private Integer capacityPallets;
        private Integer capacityBins;
        private WarehouseConfig.OperatingHours operatingHours;
        private String timeZone;
        private String currency;
        private java.util.List<String> supportedItemTypes;
        private java.util.List<String> supportedOperations;
        private java.util.Map<String, Object> attributes;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UpdateWarehouseConfigCommand {
        private String warehouseName;
        private WarehouseConfig.WarehouseStatus status;
        private WarehouseConfig.OperatingHours operatingHours;
        private java.math.BigDecimal totalArea;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CreateZoneConfigCommand {
        private String warehouseId;
        private String zoneId;
        private String zoneName;
        private ZoneConfig.ZoneType zoneType;
        private java.math.BigDecimal area;
        private ZoneConfig.AreaUnit areaUnit;
        private Integer capacityPallets;
        private Integer capacityBins;
        private java.math.BigDecimal maxWeight;
        private ZoneConfig.WeightUnit weightUnit;
        private ZoneConfig.TemperatureRange temperatureRange;
        private Boolean climatedControlled;
        private Boolean secured;
        private ZoneConfig.SecurityLevel securityLevel;
        private java.util.List<String> allowedItemTypes;
        private java.util.List<String> forbiddenItemTypes;
        private String locationDescription;
        private Integer priority;
        private java.util.Map<String, Object> attributes;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CreateAisleConfigCommand {
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
        private java.util.List<String> equipmentIds;
        private java.util.Map<String, Object> attributes;
    }
}
