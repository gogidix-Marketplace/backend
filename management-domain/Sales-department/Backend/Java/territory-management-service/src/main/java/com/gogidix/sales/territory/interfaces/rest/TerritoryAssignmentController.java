package com.gogidix.sales.territory.interfaces.rest;

import com.gogidix.sales.territory.application.dto.response.TerritoryAssignmentResponseDto;
import com.gogidix.sales.territory.application.service.TerritoryAssignmentCommandService;
import com.gogidix.sales.territory.application.service.TerritoryAssignmentQueryService;
import com.gogidix.sales.territory.domain.model.TerritoryAssignment;
import com.gogidix.sales.territory.domain.port.in.TerritoryAssignmentCommand;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Territory Assignment REST Controller
 * Handles HTTP requests for territory assignment operations
 */
@RestController
@RequestMapping("/territory-assignments")
@RequiredArgsConstructor
@Tag(name = "Territory Assignments", description = "Territory assignment management endpoints")
public class TerritoryAssignmentController {

    private final TerritoryAssignmentCommandService assignmentCommandService;
    private final TerritoryAssignmentQueryService assignmentQueryService;

    @PostMapping
    @Operation(summary = "Create a new territory assignment")
    public ResponseEntity<TerritoryAssignmentResponseDto> createAssignment(
            @Valid @RequestBody CreateAssignmentRequestDto request) {
        TerritoryAssignmentCommand.CreateAssignmentCommand command = new TerritoryAssignmentCommand.CreateAssignmentCommand(
            RequestContextHolder.getTenantId(),
            request.getTerritoryId(),
            request.getSalesRepresentativeId(),
            request.getSalesRepresentativeName(),
            request.getType(),
            request.getEffectiveDate(),
            request.getEndDate(),
            request.getPrimaryAssignment(),
            request.getPriority(),
            request.getNotes()
        );

        TerritoryAssignment assignment = assignmentCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(assignment));
    }

    @GetMapping("/{assignmentId}")
    @Operation(summary = "Get assignment by ID")
    public ResponseEntity<TerritoryAssignmentResponseDto> getAssignment(
            @Parameter(description = "Assignment ID") @PathVariable String assignmentId) {
        TerritoryAssignment assignment = assignmentQueryService.getById(assignmentId);
        return ResponseEntity.ok(toDto(assignment));
    }

    @GetMapping
    @Operation(summary = "Get all assignments for tenant")
    public ResponseEntity<List<TerritoryAssignmentResponseDto>> getAllAssignments() {
        List<TerritoryAssignment> assignments = assignmentQueryService.getAllForTenant();
        return ResponseEntity.ok(assignments.stream().map(this::toDto).toList());
    }

    @GetMapping("/territory/{territoryId}")
    @Operation(summary = "Get assignments by territory")
    public ResponseEntity<List<TerritoryAssignmentResponseDto>> getAssignmentsByTerritory(
            @Parameter(description = "Territory ID") @PathVariable String territoryId) {
        List<TerritoryAssignment> assignments = assignmentQueryService.getByTerritoryId(territoryId);
        return ResponseEntity.ok(assignments.stream().map(this::toDto).toList());
    }

    @GetMapping("/territory/{territoryId}/active")
    @Operation(summary = "Get active assignments by territory")
    public ResponseEntity<List<TerritoryAssignmentResponseDto>> getActiveAssignmentsByTerritory(
            @Parameter(description = "Territory ID") @PathVariable String territoryId) {
        List<TerritoryAssignment> assignments = assignmentQueryService.getActiveByTerritoryId(territoryId);
        return ResponseEntity.ok(assignments.stream().map(this::toDto).toList());
    }

    @GetMapping("/representative/{salesRepresentativeId}")
    @Operation(summary = "Get assignments by sales representative")
    public ResponseEntity<List<TerritoryAssignmentResponseDto>> getAssignmentsByRepresentative(
            @Parameter(description = "Sales Representative ID") @PathVariable String salesRepresentativeId) {
        List<TerritoryAssignment> assignments = assignmentQueryService.getBySalesRepresentativeId(salesRepresentativeId);
        return ResponseEntity.ok(assignments.stream().map(this::toDto).toList());
    }

    @GetMapping("/representative/{salesRepresentativeId}/active")
    @Operation(summary = "Get active assignments by sales representative")
    public ResponseEntity<List<TerritoryAssignmentResponseDto>> getActiveAssignmentsByRepresentative(
            @Parameter(description = "Sales Representative ID") @PathVariable String salesRepresentativeId) {
        List<TerritoryAssignment> assignments = assignmentQueryService.getActiveBySalesRepresentativeId(salesRepresentativeId);
        return ResponseEntity.ok(assignments.stream().map(this::toDto).toList());
    }

    @GetMapping("/representative/{salesRepresentativeId}/primary")
    @Operation(summary = "Get primary assignment by sales representative")
    public ResponseEntity<TerritoryAssignmentResponseDto> getPrimaryAssignmentByRepresentative(
            @Parameter(description = "Sales Representative ID") @PathVariable String salesRepresentativeId) {
        TerritoryAssignment assignment = assignmentQueryService.getPrimaryBySalesRepresentativeId(salesRepresentativeId);
        return ResponseEntity.ok(toDto(assignment));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get assignments by status")
    public ResponseEntity<List<TerritoryAssignmentResponseDto>> getAssignmentsByStatus(
            @Parameter(description = "Assignment Status") @PathVariable TerritoryAssignment.AssignmentStatus status) {
        List<TerritoryAssignment> assignments = assignmentQueryService.getByStatus(status);
        return ResponseEntity.ok(assignments.stream().map(this::toDto).toList());
    }

    @GetMapping("/territory/{territoryId}/primary")
    @Operation(summary = "Get primary assignments by territory")
    public ResponseEntity<List<TerritoryAssignmentResponseDto>> getPrimaryAssignmentsByTerritory(
            @Parameter(description = "Territory ID") @PathVariable String territoryId) {
        List<TerritoryAssignment> assignments = assignmentQueryService.getPrimaryAssignmentsByTerritoryId(territoryId);
        return ResponseEntity.ok(assignments.stream().map(this::toDto).toList());
    }

    @GetMapping("/summary")
    @Operation(summary = "Get assignment summary")
    public ResponseEntity<TerritoryAssignmentQueryService.AssignmentSummary> getSummary() {
        TerritoryAssignmentQueryService.AssignmentSummary summary = assignmentQueryService.getSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/search")
    @Operation(summary = "Search assignments")
    public ResponseEntity<Page<TerritoryAssignmentResponseDto>> searchAssignments(
            @Parameter(description = "Territory ID") @RequestParam(required = false) String territoryId,
            @Parameter(description = "Sales Representative ID") @RequestParam(required = false) String salesRepresentativeId,
            @Parameter(description = "Assignment Status") @RequestParam(required = false) TerritoryAssignment.AssignmentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "assignedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<TerritoryAssignment> assignments = assignmentQueryService.searchAssignments(
            territoryId, salesRepresentativeId, status, pageable);
        return ResponseEntity.ok(assignments.map(this::toDto));
    }

    @PutMapping("/{assignmentId}")
    @Operation(summary = "Update assignment")
    public ResponseEntity<TerritoryAssignmentResponseDto> updateAssignment(
            @Parameter(description = "Assignment ID") @PathVariable String assignmentId,
            @Valid @RequestBody UpdateAssignmentRequestDto request) {

        TerritoryAssignmentCommand.UpdateAssignmentCommand command = new TerritoryAssignmentCommand.UpdateAssignmentCommand(
            RequestContextHolder.getTenantId(),
            assignmentId,
            request.getEndDate(),
            request.getPrimaryAssignment(),
            request.getPriority(),
            request.getNotes()
        );

        TerritoryAssignment assignment = assignmentCommandService.update(command);
        return ResponseEntity.ok(toDto(assignment));
    }

    @PostMapping("/{assignmentId}/activate")
    @Operation(summary = "Activate assignment")
    public ResponseEntity<Void> activateAssignment(
            @Parameter(description = "Assignment ID") @PathVariable String assignmentId) {

        TerritoryAssignmentCommand.ActivateAssignmentCommand command = new TerritoryAssignmentCommand.ActivateAssignmentCommand(
            RequestContextHolder.getTenantId(),
            assignmentId,
            RequestContextHolder.getUserId()
        );

        assignmentCommandService.activate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{assignmentId}/deactivate")
    @Operation(summary = "Deactivate assignment")
    public ResponseEntity<Void> deactivateAssignment(
            @Parameter(description = "Assignment ID") @PathVariable String assignmentId) {

        TerritoryAssignmentCommand.DeactivateAssignmentCommand command = new TerritoryAssignmentCommand.DeactivateAssignmentCommand(
            RequestContextHolder.getTenantId(), assignmentId);

        assignmentCommandService.deactivate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{assignmentId}/revoke")
    @Operation(summary = "Revoke assignment")
    public ResponseEntity<Void> revokeAssignment(
            @Parameter(description = "Assignment ID") @PathVariable String assignmentId,
            @RequestBody RevokeAssignmentRequestDto request) {

        TerritoryAssignmentCommand.RevokeAssignmentCommand command = new TerritoryAssignmentCommand.RevokeAssignmentCommand(
            RequestContextHolder.getTenantId(), assignmentId, request.getReason());

        assignmentCommandService.revoke(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{assignmentId}/performance")
    @Operation(summary = "Update assignment performance")
    public ResponseEntity<Void> updatePerformance(
            @Parameter(description = "Assignment ID") @PathVariable String assignmentId,
            @RequestBody UpdateAssignmentPerformanceRequestDto request) {

        TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand command = new TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand(
            RequestContextHolder.getTenantId(),
            assignmentId,
            request.getSalesGenerated(),
            request.getAccountsManaged(),
            request.getDealsClosed()
        );

        assignmentCommandService.updatePerformance(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{assignmentId}")
    @Operation(summary = "Delete assignment")
    public ResponseEntity<Void> deleteAssignment(
            @Parameter(description = "Assignment ID") @PathVariable String assignmentId) {

        TerritoryAssignmentCommand.DeleteAssignmentCommand command = new TerritoryAssignmentCommand.DeleteAssignmentCommand(
            RequestContextHolder.getTenantId(), assignmentId);

        assignmentCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    private TerritoryAssignmentResponseDto toDto(TerritoryAssignment assignment) {
        return TerritoryAssignmentResponseDto.builder()
            .id(assignment.getId())
            .assignmentId(assignment.getAssignmentId())
            .tenantId(assignment.getTenantId())
            .territoryId(assignment.getTerritoryId())
            .salesRepresentativeId(assignment.getSalesRepresentativeId())
            .salesRepresentativeName(assignment.getSalesRepresentativeName())
            .status(mapStatus(assignment.getStatus()))
            .type(mapType(assignment.getType()))
            .assignedAt(assignment.getAssignedAt())
            .assignedBy(assignment.getAssignedBy())
            .effectiveDate(assignment.getEffectiveDate())
            .endDate(assignment.getEndDate())
            .primaryAssignment(assignment.getPrimaryAssignment())
            .priority(assignment.getPriority())
            .notes(assignment.getNotes())
            .performance(mapPerformance(assignment.getPerformance()))
            .createdAt(assignment.getCreatedAt())
            .updatedAt(assignment.getUpdatedAt())
            .build();
    }

    private TerritoryAssignmentResponseDto.AssignmentStatusDto mapStatus(TerritoryAssignment.AssignmentStatus status) {
        return status != null ? TerritoryAssignmentResponseDto.AssignmentStatusDto.valueOf(status.name()) : null;
    }

    private TerritoryAssignmentResponseDto.AssignmentTypeDto mapType(TerritoryAssignment.AssignmentType type) {
        return type != null ? TerritoryAssignmentResponseDto.AssignmentTypeDto.valueOf(type.name()) : null;
    }

    private TerritoryAssignmentResponseDto.AssignmentPerformanceDto mapPerformance(TerritoryAssignment.AssignmentPerformance performance) {
        if (performance == null) {
            return null;
        }
        return TerritoryAssignmentResponseDto.AssignmentPerformanceDto.builder()
            .salesGenerated(performance.getSalesGenerated())
            .accountsManaged(performance.getAccountsManaged())
            .dealsClosed(performance.getDealsClosed())
            .quotaAttainment(performance.getQuotaAttainment())
            .lastUpdated(performance.getLastUpdated())
            .build();
    }

    @Data
    public static class CreateAssignmentRequestDto {
        public String territoryId;
        public String salesRepresentativeId;
        public String salesRepresentativeName;
        public TerritoryAssignment.AssignmentType type;
        public LocalDate effectiveDate;
        public LocalDate endDate;
        public Boolean primaryAssignment;
        public Integer priority;
        public String notes;
    }

    @Data
    public static class UpdateAssignmentRequestDto {
        public LocalDate endDate;
        public Boolean primaryAssignment;
        public Integer priority;
        public String notes;
    }

    @Data
    public static class RevokeAssignmentRequestDto {
        public String reason;
    }

    @Data
    public static class UpdateAssignmentPerformanceRequestDto {
        public BigDecimal salesGenerated;
        public Integer accountsManaged;
        public Integer dealsClosed;
    }
}
