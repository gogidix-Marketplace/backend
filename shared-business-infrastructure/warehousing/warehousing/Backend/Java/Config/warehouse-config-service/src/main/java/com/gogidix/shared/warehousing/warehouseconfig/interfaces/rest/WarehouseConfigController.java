package com.gogidix.shared.warehousing.warehouseconfig.interfaces.rest;

import com.gogidix.shared.warehousing.warehouseconfig.application.dto.*;
import com.gogidix.shared.warehousing.warehouseconfig.application.service.WarehouseConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Warehouse Config REST Controller
 */
@RestController
@RequestMapping("/warehouse-config")
@RequiredArgsConstructor
@Tag(name = "Warehouse Configuration", description = "Warehouse configuration management APIs")
public class WarehouseConfigController {

    private final WarehouseConfigService configService;

    @PostMapping("/warehouses")
    @Operation(summary = "Create warehouse config", description = "Create a new warehouse configuration")
    public ResponseEntity<WarehouseConfigDTO> createWarehouseConfig(
            @Valid @RequestBody WarehouseConfigService.CreateWarehouseConfigCommand command) {
        WarehouseConfigDTO config = configService.createWarehouseConfig(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(config);
    }

    @GetMapping("/warehouses/{warehouseId}")
    @Operation(summary = "Get warehouse config", description = "Get warehouse configuration by ID")
    public ResponseEntity<WarehouseConfigDTO> getWarehouseConfig(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        WarehouseConfigDTO config = configService.getWarehouseConfig(warehouseId);
        return ResponseEntity.ok(config);
    }

    @PutMapping("/warehouses/{warehouseId}")
    @Operation(summary = "Update warehouse config", description = "Update warehouse configuration")
    public ResponseEntity<WarehouseConfigDTO> updateWarehouseConfig(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId,
            @Valid @RequestBody WarehouseConfigService.UpdateWarehouseConfigCommand command) {
        WarehouseConfigDTO config = configService.updateWarehouseConfig(warehouseId, command);
        return ResponseEntity.ok(config);
    }

    @DeleteMapping("/warehouses/{warehouseId}")
    @Operation(summary = "Delete warehouse config", description = "Delete warehouse configuration")
    public ResponseEntity<Void> deleteWarehouseConfig(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        configService.deleteWarehouseConfig(warehouseId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/zones")
    @Operation(summary = "Create zone config", description = "Create a new zone configuration")
    public ResponseEntity<ZoneConfigDTO> createZoneConfig(
            @Valid @RequestBody WarehouseConfigService.CreateZoneConfigCommand command) {
        ZoneConfigDTO config = configService.createZoneConfig(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(config);
    }

    @GetMapping("/warehouses/{warehouseId}/zones")
    @Operation(summary = "Get zones", description = "Get all zones for a warehouse")
    public ResponseEntity<List<ZoneConfigDTO>> getZones(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        List<ZoneConfigDTO> zones = configService.getZones(warehouseId);
        return ResponseEntity.ok(zones);
    }

    @PostMapping("/aisles")
    @Operation(summary = "Create aisle config", description = "Create a new aisle configuration")
    public ResponseEntity<AisleConfigDTO> createAisleConfig(
            @Valid @RequestBody WarehouseConfigService.CreateAisleConfigCommand command) {
        AisleConfigDTO config = configService.createAisleConfig(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(config);
    }

    @GetMapping("/warehouses/{warehouseId}/zones/{zoneId}/aisles")
    @Operation(summary = "Get aisles", description = "Get all aisles for a zone")
    public ResponseEntity<List<AisleConfigDTO>> getAisles(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId,
            @Parameter(description = "Zone ID") @PathVariable String zoneId) {
        List<AisleConfigDTO> aisles = configService.getAisles(warehouseId, zoneId);
        return ResponseEntity.ok(aisles);
    }
}
