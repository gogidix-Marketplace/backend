package com.gogidix.shared.warehousing.zone.interfaces.rest;

import com.gogidix.shared.warehousing.zone.application.service.ZoneService;
import com.gogidix.shared.warehousing.zone.domain.entity.Zone;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/zones")
@RequiredArgsConstructor
@Tag(name = "Zone Management", description = "Zone management APIs for warehouse zones")
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping("/{zoneId}/warehouses")
    @Operation(summary = "Get warehouses in a zone")
    public ResponseEntity<List<Zone>> getWarehousesForZone(
            @Parameter(description = "Zone ID") @PathVariable String zoneId) {
        log.info("REST request to get warehouses for zone {}", zoneId);
        List<Zone> zones = zoneService.getWarehousesForZone(zoneId);
        return ResponseEntity.ok(zones);
    }

    @GetMapping("/resolve")
    @Operation(summary = "Resolve address to zone by coordinates")
    public ResponseEntity<Zone> resolveZone(
            @Parameter(description = "Latitude") @RequestParam double lat,
            @Parameter(description = "Longitude") @RequestParam double lng) {
        log.info("REST request to resolve zone for lat={}, lng={}", lat, lng);
        Zone zone = zoneService.resolveZone(lat, lng);
        return ResponseEntity.ok(zone);
    }

    @GetMapping("/nearby")
    @Operation(summary = "Find nearby zones")
    public ResponseEntity<List<Zone>> findNearbyZones(
            @Parameter(description = "Latitude") @RequestParam double lat,
            @Parameter(description = "Longitude") @RequestParam double lng,
            @Parameter(description = "Radius in km") @RequestParam(defaultValue = "50") double radius) {
        log.info("REST request to find nearby zones for lat={}, lng={}, radius={}km", lat, lng, radius);
        List<Zone> zones = zoneService.findNearbyZones(lat, lng, radius);
        return ResponseEntity.ok(zones);
    }

    @GetMapping
    @Operation(summary = "Get all zones")
    public ResponseEntity<List<Zone>> getAllZones() {
        log.info("REST request to get all zones");
        return ResponseEntity.ok(zoneService.getAllZones());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get zone by ID")
    public ResponseEntity<Zone> getZoneById(@PathVariable String id) {
        log.info("REST request to get zone {}", id);
        return ResponseEntity.ok(zoneService.getZoneById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new zone")
    public ResponseEntity<Zone> createZone(@RequestBody Zone zone) {
        log.info("REST request to create zone: {}", zone.getZoneName());
        Zone created = zoneService.createZone(zone);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
