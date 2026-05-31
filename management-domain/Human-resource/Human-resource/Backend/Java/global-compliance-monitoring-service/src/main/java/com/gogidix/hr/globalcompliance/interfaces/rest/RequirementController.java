package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.application.service.RequirementCommandService;
import com.gogidix.hr.globalcompliance.application.service.RequirementQueryService;
import com.gogidix.hr.globalcompliance.domain.model.ComplianceRequirement;
import com.gogidix.hr.globalcompliance.domain.port.in.RequirementCommand;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Compliance Requirement REST Controller
 * Handles HTTP requests for compliance requirement operations
 */
@RestController
@RequestMapping("/requirements")
@RequiredArgsConstructor
@Tag(name = "Compliance Requirements", description = "Compliance requirement management endpoints")
public class RequirementController {

    private final RequirementCommandService requirementCommandService;
    private final RequirementQueryService requirementQueryService;

    @PostMapping
    @Operation(summary = "Create a new compliance requirement")
    public ResponseEntity<ComplianceRequirement> createRequirement(
            @Valid @RequestBody RequirementCommand.CreateRequirementCommand command) {
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setCreatedBy(RequestContextHolder.getUserId());
        ComplianceRequirement requirement = requirementCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(requirement);
    }

    @GetMapping
    @Operation(summary = "Get all compliance requirements")
    public ResponseEntity<Page<ComplianceRequirement>> getAllRequirements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceRequirement> requirements = requirementQueryService.getAllForTenant();
        return ResponseEntity.ok(requirements);
    }

    @GetMapping("/{requirementId}")
    @Operation(summary = "Get requirement by ID")
    public ResponseEntity<ComplianceRequirement> getRequirement(
            @Parameter(description = "Requirement ID") @PathVariable String requirementId) {
        ComplianceRequirement requirement = requirementQueryService.getById(requirementId);
        return ResponseEntity.ok(requirement);
    }

    @PutMapping("/{requirementId}")
    @Operation(summary = "Update requirement")
    public ResponseEntity<ComplianceRequirement> updateRequirement(
            @Parameter(description = "Requirement ID") @PathVariable String requirementId,
            @Valid @RequestBody RequirementCommand.UpdateRequirementCommand command) {
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setRequirementId(requirementId);
        ComplianceRequirement requirement = requirementCommandService.update(command);
        return ResponseEntity.ok(requirement);
    }

    @DeleteMapping("/{requirementId}")
    @Operation(summary = "Delete requirement")
    public ResponseEntity<Void> deleteRequirement(
            @Parameter(description = "Requirement ID") @PathVariable String requirementId) {
        RequirementCommand.DeleteRequirementCommand command = new RequirementCommand.DeleteRequirementCommand(
                RequestContextHolder.getTenantId(), requirementId);
        requirementCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-country/{countryCode}")
    @Operation(summary = "Get requirements by country")
    public ResponseEntity<Page<ComplianceRequirement>> getByCountry(
            @Parameter(description = "Country Code") @PathVariable String countryCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceRequirement> requirements = requirementQueryService.getByCountryCode(countryCode, page, size);
        return ResponseEntity.ok(requirements);
    }

    @GetMapping("/by-category/{category}")
    @Operation(summary = "Get requirements by category")
    public ResponseEntity<Page<ComplianceRequirement>> getByCategory(
            @Parameter(description = "Category") @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceRequirement> requirements = requirementQueryService.getByCategory(category, page, size);
        return ResponseEntity.ok(requirements);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active requirements")
    public ResponseEntity<Page<ComplianceRequirement>> getActiveRequirements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceRequirement> requirements = requirementQueryService.getActiveRequirements(page, size);
        return ResponseEntity.ok(requirements);
    }

    @PostMapping("/{requirementId}/activate")
    @Operation(summary = "Activate requirement")
    public ResponseEntity<Void> activateRequirement(
            @Parameter(description = "Requirement ID") @PathVariable String requirementId) {
        RequirementCommand.ActivateRequirementCommand command = new RequirementCommand.ActivateRequirementCommand(
                RequestContextHolder.getTenantId(), requirementId);
        requirementCommandService.activate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{requirementId}/deactivate")
    @Operation(summary = "Deactivate requirement")
    public ResponseEntity<Void> deactivateRequirement(
            @Parameter(description = "Requirement ID") @PathVariable String requirementId,
            @RequestBody DeactivationRequestDto request) {
        RequirementCommand.DeactivateRequirementCommand command = new RequirementCommand.DeactivateRequirementCommand(
                RequestContextHolder.getTenantId(), requirementId, request.getReason());
        requirementCommandService.deactivate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{requirementId}/related-requirements")
    @Operation(summary = "Add related requirement")
    public ResponseEntity<Void> addRelatedRequirement(
            @Parameter(description = "Requirement ID") @PathVariable String requirementId,
            @RequestBody RelatedRequirementRequestDto request) {
        RequirementCommand.AddRelatedRequirementCommand command = new RequirementCommand.AddRelatedRequirementCommand(
                RequestContextHolder.getTenantId(), requirementId, request.getRelatedRequirementId());
        requirementCommandService.addRelatedRequirement(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{requirementId}/update-review-date")
    @Operation(summary = "Update review date")
    public ResponseEntity<Void> updateReviewDate(
            @Parameter(description = "Requirement ID") @PathVariable String requirementId) {
        RequirementCommand.UpdateReviewDateCommand command = new RequirementCommand.UpdateReviewDateCommand(
                RequestContextHolder.getTenantId(), requirementId);
        requirementCommandService.updateReviewDate(command);
        return ResponseEntity.ok().build();
    }

    @lombok.Data
    public static class DeactivationRequestDto {
        private String reason;
    }

    @lombok.Data
    public static class RelatedRequirementRequestDto {
        private String relatedRequirementId;
    }
}
