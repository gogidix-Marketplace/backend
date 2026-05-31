package com.gogidix.shared.warehousing.cyclecounting.interfaces.rest;

import com.gogidix.shared.warehousing.cyclecounting.application.dto.*;
import com.gogidix.shared.warehousing.cyclecounting.application.service.CycleCountingService;
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
 * Cycle Counting REST Controller
 */
@RestController
@RequestMapping("/cycle-counting")
@RequiredArgsConstructor
@Tag(name = "Cycle Counting", description = "Inventory cycle counting and reconciliation APIs")
public class CycleCountingController {

    private final CycleCountingService cycleCountingService;

    @PostMapping("/counts")
    @Operation(summary = "Create cycle count", description = "Create a new cycle count")
    public ResponseEntity<CycleCountDTO> createCycleCount(
            @Valid @RequestBody CreateCycleCountCommand command) {
        CycleCountDTO count = cycleCountingService.createCycleCount(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(count);
    }

    @PostMapping("/counts/{cycleCountId}/start")
    @Operation(summary = "Start count session", description = "Start a new counting session")
    public ResponseEntity<CountSessionDTO> startSession(
            @Parameter(description = "Cycle count ID") @PathVariable String cycleCountId,
            @Parameter(description = "Counter ID") @RequestParam String counterId) {
        CountSessionDTO session = cycleCountingService.startSession(cycleCountId, counterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @PostMapping("/counts/record")
    @Operation(summary = "Record count", description = "Record inventory count")
    public ResponseEntity<CountDiscrepancyDTO> recordCount(
            @Valid @RequestBody RecordCountCommand command) {
        CountDiscrepancyDTO discrepancy = cycleCountingService.recordCount(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(discrepancy);
    }

    @PostMapping("/counts/{cycleCountId}/complete")
    @Operation(summary = "Complete cycle count", description = "Complete a cycle count")
    public ResponseEntity<CycleCountDTO> completeCycleCount(
            @Parameter(description = "Cycle count ID") @PathVariable String cycleCountId) {
        CycleCountDTO count = cycleCountingService.completeCycleCount(cycleCountId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/counts/{cycleCountId}")
    @Operation(summary = "Get cycle count", description = "Get cycle count by ID")
    public ResponseEntity<CycleCountDTO> getCycleCount(
            @Parameter(description = "Cycle count ID") @PathVariable String cycleCountId) {
        CycleCountDTO count = cycleCountingService.getCycleCount(cycleCountId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/warehouses/{warehouseId}/counts")
    @Operation(summary = "Get cycle counts", description = "Get all cycle counts for warehouse")
    public ResponseEntity<List<CycleCountDTO>> getCycleCounts(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        List<CycleCountDTO> counts = cycleCountingService.getCycleCounts(warehouseId);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/counts/{cycleCountId}/discrepancies")
    @Operation(summary = "Get discrepancies", description = "Get discrepancies for cycle count")
    public ResponseEntity<List<CountDiscrepancyDTO>> getDiscrepancies(
            @Parameter(description = "Cycle count ID") @PathVariable String cycleCountId) {
        List<CountDiscrepancyDTO> discrepancies = cycleCountingService.getDiscrepancies(cycleCountId);
        return ResponseEntity.ok(discrepancies);
    }

    @GetMapping("/discrepancies/unresolved")
    @Operation(summary = "Get unresolved discrepancies", description = "Get all unresolved discrepancies")
    public ResponseEntity<List<CountDiscrepancyDTO>> getUnresolvedDiscrepancies() {
        List<CountDiscrepancyDTO> discrepancies = cycleCountingService.getUnresolvedDiscrepancies();
        return ResponseEntity.ok(discrepancies);
    }

    @PostMapping("/discrepancies/{discrepancyId}/reconcile")
    @Operation(summary = "Reconcile discrepancy", description = "Reconcile a discrepancy")
    public ResponseEntity<CountDiscrepancyDTO> reconcileDiscrepancy(
            @Parameter(description = "Discrepancy ID") @PathVariable String discrepancyId,
            @Valid @RequestBody CycleCountingService.ReconcileDiscrepancyCommand command) {
        CountDiscrepancyDTO discrepancy = cycleCountingService.reconcileDiscrepancy(discrepancyId, command);
        return ResponseEntity.ok(discrepancy);
    }
}
