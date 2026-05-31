package com.gogidix.shared.warehousing.serialization.interfaces.rest;

import com.gogidix.shared.warehousing.serialization.application.command.CreateBatchCommand;
import com.gogidix.shared.warehousing.serialization.application.dto.BatchDTO;
import com.gogidix.shared.warehousing.serialization.application.service.BatchService;
import com.gogidix.shared.warehousing.serialization.domain.entity.Batch.QCStatus;
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
 * Batch REST Controller
 *
 * Provides multi-tenant batch management APIs
 * All endpoints require X-Tenant-ID header for tenant isolation
 */
@RestController
@RequestMapping("/batches")
@RequiredArgsConstructor
@Tag(name = "Batches", description = "APIs for managing inventory batches")
public class BatchController {

    private final BatchService batchService;

    /**
     * Create batch (tenant-scoped)
     * POST /api/v1/serialization/batches
     */
    @PostMapping
    @Operation(summary = "Create a batch", description = "Create a new batch with lot/batch number")
    public ResponseEntity<BatchDTO> createBatch(
            @Valid @RequestBody CreateBatchCommand command) {
        BatchDTO batch = batchService.createBatch(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(batch);
    }

    /**
     * Get batch by ID (tenant-scoped)
     * GET /api/v1/serialization/batches/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get batch by ID", description = "Retrieve a batch by its ID")
    public ResponseEntity<BatchDTO> getBatch(
            @Parameter(description = "Batch ID") @PathVariable String id) {
        BatchDTO batch = batchService.getBatch(id);
        return ResponseEntity.ok(batch);
    }

    /**
     * Get batch by batch number (tenant-scoped)
     * GET /api/v1/serialization/batches/number/{batchNumber}
     */
    @GetMapping("/number/{batchNumber}")
    @Operation(summary = "Get batch by number", description = "Retrieve a batch by its batch number")
    public ResponseEntity<BatchDTO> getBatchByNumber(
            @Parameter(description = "Batch number") @PathVariable String batchNumber) {
        BatchDTO batch = batchService.getBatchByNumber(batchNumber);
        return ResponseEntity.ok(batch);
    }

    /**
     * Get all batches for current tenant
     * GET /api/v1/serialization/batches
     */
    @GetMapping
    @Operation(summary = "Get all batches", description = "Retrieve all batches for the current tenant")
    public ResponseEntity<List<BatchDTO>> getAllBatches() {
        List<BatchDTO> batches = batchService.getAllBatches();
        return ResponseEntity.ok(batches);
    }

    /**
     * Get batches by SKU (tenant-scoped)
     * GET /api/v1/serialization/batches/sku/{sku}
     */
    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get batches by SKU", description = "Retrieve all batches for a specific SKU")
    public ResponseEntity<List<BatchDTO>> getBatchesBySku(
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        List<BatchDTO> batches = batchService.getBatchesBySku(sku);
        return ResponseEntity.ok(batches);
    }

    /**
     * Update batch available quantity (tenant-scoped)
     * PATCH /api/v1/serialization/batches/{id}/quantity
     */
    @PatchMapping("/{id}/quantity")
    @Operation(summary = "Update batch quantity", description = "Adjust the available quantity of a batch")
    public ResponseEntity<BatchDTO> updateAvailableQuantity(
            @Parameter(description = "Batch ID") @PathVariable String id,
            @Parameter(description = "Quantity adjustment (can be positive or negative)") @RequestParam Integer adjustment) {
        BatchDTO batch = batchService.updateAvailableQuantity(id, adjustment);
        return ResponseEntity.ok(batch);
    }

    /**
     * Update batch QC status (tenant-scoped)
     * PUT /api/v1/serialization/batches/{id}/qc
     */
    @PutMapping("/{id}/qc")
    @Operation(summary = "Update batch QC status", description = "Update the quality control status of a batch")
    public ResponseEntity<BatchDTO> updateQCStatus(
            @Parameter(description = "Batch ID") @PathVariable String id,
            @Parameter(description = "QC status") @RequestParam QCStatus qcStatus,
            @Parameter(description = "QC notes") @RequestParam(required = false) String notes) {
        BatchDTO batch = batchService.updateQCStatus(id, qcStatus, notes);
        return ResponseEntity.ok(batch);
    }

    /**
     * Delete batch (tenant-scoped)
     * DELETE /api/v1/serialization/batches/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete batch", description = "Delete a batch by ID")
    public ResponseEntity<Void> deleteBatch(
            @Parameter(description = "Batch ID") @PathVariable String id) {
        batchService.deleteBatch(id);
        return ResponseEntity.noContent().build();
    }
}
