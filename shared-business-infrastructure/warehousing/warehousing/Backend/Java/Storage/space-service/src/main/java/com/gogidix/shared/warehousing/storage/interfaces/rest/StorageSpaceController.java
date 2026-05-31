package com.gogidix.shared.warehousing.storage.interfaces.rest;

import com.gogidix.shared.warehousing.storage.application.dto.*;
import com.gogidix.shared.warehousing.storage.application.service.StorageSpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Storage Space Management
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/storage/spaces")
@RequiredArgsConstructor
public class StorageSpaceController {

    private final StorageSpaceService service;

    /**
     * Create a new storage space
     */
    @PostMapping
    public ResponseEntity<StorageSpaceResponse> createSpace(@Valid @RequestBody CreateSpaceRequest request) {
        log.info("REST request to create storage space: {}", request.getSpaceCode());
        StorageSpaceResponse response = service.createSpace(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * List all storage spaces for tenant
     */
    @GetMapping
    public ResponseEntity<List<StorageSpaceResponse>> getSpaces(
            @RequestParam String tenantId,
            @RequestParam(required = false) String spaceType) {

        log.info("REST request to get spaces for tenant: {}", tenantId);

        if (spaceType != null && !spaceType.isEmpty()) {
            return ResponseEntity.ok(service.getAvailableSpaces(tenantId, spaceType));
        }

        return ResponseEntity.ok(service.getSpaces(tenantId));
    }

    /**
     * Get storage space by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<StorageSpaceResponse> getSpace(@PathVariable String id) {
        log.info("REST request to get space: {}", id);
        return ResponseEntity.ok(service.getSpaceById(id));
    }

    /**
     * Update storage space
     */
    @PutMapping("/{id}")
    public ResponseEntity<StorageSpaceResponse> updateSpace(
            @PathVariable String id,
            @Valid @RequestBody CreateSpaceRequest request) {

        log.info("REST request to update space: {}", id);
        return ResponseEntity.ok(service.updateSpace(id, request));
    }

    /**
     * Allocate space to customer
     */
    @PostMapping("/{id}/allocate")
    public ResponseEntity<StorageSpaceResponse> allocateSpace(
            @PathVariable String id,
            @Valid @RequestBody AllocateSpaceRequest request) {

        log.info("REST request to allocate space: {}", id);
        return ResponseEntity.ok(service.allocateSpace(id, request));
    }

    /**
     * Optimize space utilization
     */
    @PutMapping("/{id}/optimize")
    public ResponseEntity<Void> optimizeSpace(@PathVariable String id) {
        log.info("REST request to optimize space: {}", id);
        service.optimizeSpace(id);
        return ResponseEntity.ok().build();
    }
}
