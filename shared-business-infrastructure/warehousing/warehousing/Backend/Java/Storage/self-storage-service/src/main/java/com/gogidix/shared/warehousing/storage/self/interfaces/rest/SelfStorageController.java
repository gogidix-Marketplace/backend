package com.gogidix.shared.warehousing.storage.self.interfaces.rest;

import com.gogidix.shared.warehousing.storage.self.application.service.SelfStorageService;
import com.gogidix.shared.warehousing.storage.self.domain.entity.SelfStorageUnit;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Self Storage Units
 */
@RestController
@RequestMapping("/api/v1/storage/self-storage")
@RequiredArgsConstructor
public class SelfStorageController {

    private final SelfStorageService selfStorageService;

    @PostMapping
    public ResponseEntity<SelfStorageUnit> createUnit(@Valid @RequestBody SelfStorageUnit unit) {
        SelfStorageUnit created = selfStorageService.createUnit(unit);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<SelfStorageUnit>> getAvailableUnits(
            @RequestParam String tenantId,
            @RequestParam String facilityId) {
        List<SelfStorageUnit> units = selfStorageService.getAvailableUnits(tenantId, facilityId);
        return ResponseEntity.ok(units);
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<SelfStorageUnit>> getUnitsByType(
            @RequestParam String tenantId,
            @RequestParam String facilityId,
            @RequestParam String unitType) {
        List<SelfStorageUnit> units = selfStorageService.getUnitsByType(tenantId, facilityId, unitType);
        return ResponseEntity.ok(units);
    }

    @GetMapping("/climate-controlled")
    public ResponseEntity<List<SelfStorageUnit>> getClimateControlledUnits(
            @RequestParam String tenantId,
            @RequestParam String facilityId) {
        List<SelfStorageUnit> units = selfStorageService.getClimateControlledUnits(tenantId, facilityId);
        return ResponseEntity.ok(units);
    }

    @PostMapping("/{unitId}/rent")
    public ResponseEntity<SelfStorageUnit> rentUnit(
            @PathVariable String unitId,
            @RequestParam String rentalId,
            @RequestParam String tenantId,
            @RequestParam String tenantName,
            @RequestParam(defaultValue = "@now") LocalDateTime startDate) {
        SelfStorageUnit unit = selfStorageService.rentUnit(unitId, rentalId, tenantId, tenantName, startDate);
        return ResponseEntity.ok(unit);
    }

    @PostMapping("/{unitId}/vacate")
    public ResponseEntity<SelfStorageUnit> vacateUnit(@PathVariable String unitId) {
        SelfStorageUnit unit = selfStorageService.vacateUnit(unitId);
        return ResponseEntity.ok(unit);
    }

    @PostMapping("/{unitId}/reserve")
    public ResponseEntity<SelfStorageUnit> reserveUnit(
            @PathVariable String unitId,
            @RequestParam String tenantId) {
        SelfStorageUnit unit = selfStorageService.reserveUnit(unitId, tenantId);
        return ResponseEntity.ok(unit);
    }
}
