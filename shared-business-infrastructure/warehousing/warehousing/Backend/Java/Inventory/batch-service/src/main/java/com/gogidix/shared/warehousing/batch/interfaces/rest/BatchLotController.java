package com.gogidix.shared.warehousing.batch.interfaces.rest;

import com.gogidix.shared.warehousing.batch.application.command.CreateBatchLotCommand;
import com.gogidix.shared.warehousing.batch.application.command.UpdateBatchLotCommand;
import com.gogidix.shared.warehousing.batch.application.dto.BatchLotDTO;
import com.gogidix.shared.warehousing.batch.application.service.BatchLotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Batch Lot REST Controller
 *
 * Provides multi-tenant batch lot management APIs
 */
@RestController
@RequestMapping("/batch-lots")
@RequiredArgsConstructor
@Tag(name = "Batch Lots", description = "APIs for managing batch lots")
public class BatchLotController {

    private final BatchLotService batchLotService;

    @PostMapping
    @Operation(summary = "Create a batch lot", description = "Create a new batch lot with lot number")
    public ResponseEntity<BatchLotDTO> createBatchLot(
            @Valid @RequestBody CreateBatchLotCommand command) {
        BatchLotDTO lot = batchLotService.createBatchLot(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(lot);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get batch lot by ID", description = "Retrieve a batch lot by its ID")
    public ResponseEntity<BatchLotDTO> getBatchLot(
            @Parameter(description = "Batch lot ID") @PathVariable String id) {
        BatchLotDTO lot = batchLotService.getBatchLot(id);
        return ResponseEntity.ok(lot);
    }

    @GetMapping("/number/{lotNumber}")
    @Operation(summary = "Get batch lot by number", description = "Retrieve a batch lot by its lot number")
    public ResponseEntity<BatchLotDTO> getBatchLotByNumber(
            @Parameter(description = "Lot number") @PathVariable String lotNumber) {
        BatchLotDTO lot = batchLotService.getBatchLotByNumber(lotNumber);
        return ResponseEntity.ok(lot);
    }

    @GetMapping
    @Operation(summary = "Get all batch lots", description = "Retrieve all batch lots for the current tenant")
    public ResponseEntity<List<BatchLotDTO>> getAllBatchLots() {
        List<BatchLotDTO> lots = batchLotService.getAllBatchLots();
        return ResponseEntity.ok(lots);
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get batch lots by SKU", description = "Retrieve all batch lots for a specific SKU")
    public ResponseEntity<List<BatchLotDTO>> getBatchLotsBySku(
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        List<BatchLotDTO> lots = batchLotService.getBatchLotsBySku(sku);
        return ResponseEntity.ok(lots);
    }

    @GetMapping("/sku/{sku}/fefo")
    @Operation(summary = "Get batch lots by SKU ordered by expiry (FEFO)", description = "Retrieve batch lots ordered by expiration date (First Expired First Out)")
    public ResponseEntity<List<BatchLotDTO>> getBatchLotsBySkuOrderedByExpiry(
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        List<BatchLotDTO> lots = batchLotService.getBatchLotsBySkuOrderedByExpiry(sku);
        return ResponseEntity.ok(lots);
    }

    @GetMapping("/expiring-before/{date}")
    @Operation(summary = "Get batch lots expiring before date", description = "Retrieve all batch lots expiring before a specific date")
    public ResponseEntity<List<BatchLotDTO>> getBatchLotsExpiringBefore(
            @Parameter(description = "Expiration date (ISO format)") @PathVariable String date) {
        LocalDate expiryDate = LocalDate.parse(date);
        List<BatchLotDTO> lots = batchLotService.getBatchLotsExpiringBefore(expiryDate);
        return ResponseEntity.ok(lots);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update batch lot", description = "Update a batch lot by ID")
    public ResponseEntity<BatchLotDTO> updateBatchLot(
            @Parameter(description = "Batch lot ID") @PathVariable String id,
            @Valid @RequestBody UpdateBatchLotCommand command) {
        BatchLotDTO lot = batchLotService.updateBatchLot(id, command);
        return ResponseEntity.ok(lot);
    }

    @PatchMapping("/{id}/allocate")
    @Operation(summary = "Allocate quantity from batch lot", description = "Allocate quantity from a batch lot")
    public ResponseEntity<BatchLotDTO> allocateQuantity(
            @Parameter(description = "Batch lot ID") @PathVariable String id,
            @Parameter(description = "Quantity to allocate") @RequestParam Integer quantity) {
        BatchLotDTO lot = batchLotService.allocateQuantity(id, quantity);
        return ResponseEntity.ok(lot);
    }

    @PatchMapping("/{id}/deallocate")
    @Operation(summary = "Deallocate quantity from batch lot", description = "Deallocate quantity from a batch lot")
    public ResponseEntity<BatchLotDTO> deallocateQuantity(
            @Parameter(description = "Batch lot ID") @PathVariable String id,
            @Parameter(description = "Quantity to deallocate") @RequestParam Integer quantity) {
        BatchLotDTO lot = batchLotService.deallocateQuantity(id, quantity);
        return ResponseEntity.ok(lot);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete batch lot", description = "Delete a batch lot by ID")
    public ResponseEntity<Void> deleteBatchLot(
            @Parameter(description = "Batch lot ID") @PathVariable String id) {
        batchLotService.deleteBatchLot(id);
        return ResponseEntity.noContent().build();
    }
}
