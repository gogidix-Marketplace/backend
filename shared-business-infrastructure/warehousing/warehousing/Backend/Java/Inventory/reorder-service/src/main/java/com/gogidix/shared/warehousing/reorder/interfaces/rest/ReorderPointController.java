package com.gogidix.shared.warehousing.reorder.interfaces.rest;

import com.gogidix.shared.warehousing.reorder.application.dto.ReorderPointDTO;
import com.gogidix.shared.warehousing.reorder.application.service.ReorderPointService;
import com.gogidix.shared.warehousing.reorder.domain.entity.ReorderPoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Reorder Point REST Controller
 */
@RestController
@RequestMapping("/reorder-points")
@RequiredArgsConstructor
@Tag(name = "Reorder Points", description = "APIs for managing reorder points")
public class ReorderPointController {

    private final ReorderPointService reorderPointService;

    @PostMapping
    @Operation(summary = "Create reorder point", description = "Create a new reorder point")
    public ResponseEntity<ReorderPointDTO> createReorderPoint(@RequestBody ReorderPoint reorderPoint) {
        ReorderPointDTO created = reorderPointService.createReorderPoint(reorderPoint);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reorder point by ID", description = "Retrieve a reorder point by ID")
    public ResponseEntity<ReorderPointDTO> getReorderPoint(
            @Parameter(description = "Reorder point ID") @PathVariable String id) {
        ReorderPointDTO reorderPoint = reorderPointService.getReorderPoint(id);
        return ResponseEntity.ok(reorderPoint);
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get reorder point by SKU", description = "Retrieve a reorder point by SKU")
    public ResponseEntity<ReorderPointDTO> getReorderPointBySku(
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        ReorderPointDTO reorderPoint = reorderPointService.getReorderPointBySku(sku);
        return ResponseEntity.ok(reorderPoint);
    }

    @GetMapping
    @Operation(summary = "Get all reorder points", description = "Retrieve all reorder points for current tenant")
    public ResponseEntity<List<ReorderPointDTO>> getAllReorderPoints() {
        List<ReorderPointDTO> reorderPoints = reorderPointService.getAllReorderPoints();
        return ResponseEntity.ok(reorderPoints);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reorder points by status", description = "Retrieve reorder points by status")
    public ResponseEntity<List<ReorderPointDTO>> getReorderPointsByStatus(
            @Parameter(description = "Reorder status") @PathVariable ReorderPoint.ReorderStatus status) {
        List<ReorderPointDTO> reorderPoints = reorderPointService.getReorderPointsByStatus(status);
        return ResponseEntity.ok(reorderPoints);
    }

    @PatchMapping("/{id}/check-stock")
    @Operation(summary = "Check stock level", description = "Check and update stock level for reorder point")
    public ResponseEntity<ReorderPointDTO> checkStockLevel(
            @Parameter(description = "Reorder point ID") @PathVariable String id,
            @Parameter(description = "Current stock level") @RequestParam Integer currentStock) {
        ReorderPointDTO reorderPoint = reorderPointService.checkStockLevel(id, currentStock);
        return ResponseEntity.ok(reorderPoint);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update reorder point", description = "Update a reorder point")
    public ResponseEntity<ReorderPointDTO> updateReorderPoint(
            @Parameter(description = "Reorder point ID") @PathVariable String id,
            @RequestBody ReorderPoint updates) {
        ReorderPointDTO reorderPoint = reorderPointService.updateReorderPoint(id, updates);
        return ResponseEntity.ok(reorderPoint);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reorder point", description = "Delete a reorder point by ID")
    public ResponseEntity<Void> deleteReorderPoint(
            @Parameter(description = "Reorder point ID") @PathVariable String id) {
        reorderPointService.deleteReorderPoint(id);
        return ResponseEntity.noContent().build();
    }
}
