package com.gogidix.shared.warehousing.quality.interfaces.rest;

import com.gogidix.shared.warehousing.quality.application.command.CreateQualityCheckCommand;
import com.gogidix.shared.warehousing.quality.application.command.UpdateQualityCheckCommand;
import com.gogidix.shared.warehousing.quality.application.dto.QualityCheckDTO;
import com.gogidix.shared.warehousing.quality.application.service.QualityService;
import com.gogidix.shared.warehousing.quality.domain.entity.QualityCheck;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quality-checks")
@RequiredArgsConstructor
@Tag(name = "Quality Checks", description = "Quality control APIs")
public class QualityController {

    private final QualityService qualityService;

    @PostMapping
    @Operation(summary = "Create a quality check", description = "Creates a new quality check")
    public ResponseEntity<QualityCheckDTO> createQualityCheck(@Valid @RequestBody CreateQualityCheckCommand command) {
        QualityCheckDTO check = qualityService.createQualityCheck(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(check);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get quality check by ID", description = "Retrieves quality check details")
    public ResponseEntity<QualityCheckDTO> getQualityCheck(@Parameter(description = "Quality check ID") @PathVariable String id) {
        QualityCheckDTO check = qualityService.getQualityCheck(id);
        return ResponseEntity.ok(check);
    }

    @GetMapping
    @Operation(summary = "Get all quality checks", description = "Retrieves all quality checks for current tenant")
    public ResponseEntity<List<QualityCheckDTO>> getAllQualityChecks() {
        List<QualityCheckDTO> checks = qualityService.getAllQualityChecks();
        return ResponseEntity.ok(checks);
    }

    @GetMapping("/reference/{referenceId}")
    @Operation(summary = "Get checks by reference", description = "Retrieves quality checks for a specific reference")
    public ResponseEntity<List<QualityCheckDTO>> getChecksByReference(
            @Parameter(description = "Reference ID") @PathVariable String referenceId) {
        List<QualityCheckDTO> checks = qualityService.getChecksByReference(referenceId);
        return ResponseEntity.ok(checks);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get checks by status", description = "Retrieves quality checks by status")
    public ResponseEntity<List<QualityCheckDTO>> getChecksByStatus(
            @Parameter(description = "Status") @PathVariable QualityCheck.QualityStatus status) {
        List<QualityCheckDTO> checks = qualityService.getChecksByStatus(status);
        return ResponseEntity.ok(checks);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update quality check", description = "Updates quality check details")
    public ResponseEntity<QualityCheckDTO> updateQualityCheck(
            @Parameter(description = "Quality check ID") @PathVariable String id,
            @Valid @RequestBody UpdateQualityCheckCommand command) {
        QualityCheckDTO check = qualityService.updateQualityCheck(id, command);
        return ResponseEntity.ok(check);
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete quality check", description = "Marks a quality check as complete with pass/fail result")
    public ResponseEntity<QualityCheckDTO> completeQualityCheck(
            @Parameter(description = "Quality check ID") @PathVariable String id,
            @Parameter(description = "Passed") @RequestParam Boolean passed,
            @Parameter(description = "Quality score (0-100)") @RequestParam Integer score) {
        QualityCheckDTO check = qualityService.completeQualityCheck(id, passed, score);
        return ResponseEntity.ok(check);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete quality check", description = "Deletes a quality check")
    public ResponseEntity<Void> deleteQualityCheck(@Parameter(description = "Quality check ID") @PathVariable String id) {
        qualityService.deleteQualityCheck(id);
        return ResponseEntity.noContent().build();
    }
}
