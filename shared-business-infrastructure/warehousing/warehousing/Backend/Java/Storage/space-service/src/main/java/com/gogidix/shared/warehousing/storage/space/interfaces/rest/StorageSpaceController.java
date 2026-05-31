package com.gogidix.shared.warehousing.storage.space.interfaces.rest;

import com.gogidix.shared.warehousing.storage.space.application.service.StorageSpaceService;
import com.gogidix.shared.warehousing.storage.space.domain.entity.StorageSpace;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Storage Space Management
 */
@RestController
@RequestMapping("/api/v1/storage/spaces")
@RequiredArgsConstructor
public class StorageSpaceController {

    private final StorageSpaceService spaceService;

    @PostMapping
    public ResponseEntity<StorageSpace> createSpace(@Valid @RequestBody StorageSpace space) {
        StorageSpace created = spaceService.createSpace(space);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<StorageSpace>> getAvailableSpaces(
            @RequestParam String tenantId,
            @RequestParam String warehouseId) {
        List<StorageSpace> spaces = spaceService.getAvailableSpaces(tenantId, warehouseId);
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<StorageSpace>> getSpacesByType(
            @RequestParam String tenantId,
            @RequestParam String warehouseId,
            @RequestParam String spaceType) {
        List<StorageSpace> spaces = spaceService.getSpacesByType(tenantId, warehouseId, spaceType);
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/by-temperature")
    public ResponseEntity<List<StorageSpace>> getSpacesByTemperatureZone(
            @RequestParam String tenantId,
            @RequestParam String warehouseId,
            @RequestParam String temperatureZone) {
        List<StorageSpace> spaces = spaceService.getSpacesByTemperatureZone(tenantId, warehouseId, temperatureZone);
        return ResponseEntity.ok(spaces);
    }

    @PostMapping("/{spaceId}/occupy")
    public ResponseEntity<StorageSpace> occupySpace(
            @PathVariable String spaceId,
            @RequestParam String contentId,
            @RequestParam String contentType) {
        StorageSpace space = spaceService.occupySpace(spaceId, contentId, contentType);
        return ResponseEntity.ok(space);
    }

    @PostMapping("/{spaceId}/vacate")
    public ResponseEntity<StorageSpace> vacateSpace(@PathVariable String spaceId) {
        StorageSpace space = spaceService.vacateSpace(spaceId);
        return ResponseEntity.ok(space);
    }

    @PatchMapping("/{spaceId}/status")
    public ResponseEntity<StorageSpace> updateStatus(
            @PathVariable String spaceId,
            @RequestParam String status) {
        StorageSpace space = spaceService.updateStatus(spaceId, StorageSpace.SpaceStatus.valueOf(status));
        return ResponseEntity.ok(space);
    }
}
