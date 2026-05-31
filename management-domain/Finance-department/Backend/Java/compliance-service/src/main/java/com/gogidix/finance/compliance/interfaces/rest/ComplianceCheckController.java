package com.gogidix.finance.compliance.interfaces.rest;

import com.gogidix.finance.compliance.application.dto.response.ComplianceCheckResponseDto;
import com.gogidix.finance.compliance.application.service.ComplianceCheckService;
import com.gogidix.finance.compliance.application.service.ComplianceQueryService;
import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import com.gogidix.finance.compliance.domain.port.in.ComplianceCheckCommand;
import com.gogidix.finance.compliance.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Compliance Check REST Controller
 * Handles HTTP requests for compliance check operations
 */
@RestController
@RequestMapping("/compliance/checks")
@RequiredArgsConstructor
@Tag(name = "Compliance Checks", description = "Compliance check management endpoints")
public class ComplianceCheckController {

    private final ComplianceCheckService checkService;
    private final ComplianceQueryService queryService;

    @PostMapping
    @Operation(summary = "Create a new compliance check")
    public ResponseEntity<ComplianceCheckResponseDto> createCheck(
            @Valid @RequestBody CreateCheckRequestDto request) {
        ComplianceCheckCommand.CreateCheckCommand command = new ComplianceCheckCommand.CreateCheckCommand();
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setRuleId(request.getRuleId());
        command.setRuleName(request.getRuleName());
        command.setEntityType(request.getEntityType());
        command.setEntityId(request.getEntityId());
        command.setReferenceNumber(request.getReferenceNumber());
        command.setEvaluatedByUserId(RequestContextHolder.getUserId().orElse("system"));
        command.setContext(request.getContext());
        command.setEvaluatedAmount(request.getEvaluatedAmount());
        command.setEvaluatedCurrency(request.getEvaluatedCurrency());
        command.setThresholdAmount(request.getThresholdAmount());
        command.setThresholdCurrency(request.getThresholdCurrency());
        command.setDepartment(request.getDepartment());
        command.setCostCenter(request.getCostCenter());
        command.setExpenseCategory(request.getExpenseCategory());
        command.setCorrelationId(request.getCorrelationId());

        ComplianceCheck check = checkService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(check));
    }

    @PostMapping("/{checkId}/execute")
    @Operation(summary = "Execute a compliance check")
    public ResponseEntity<ComplianceCheckResponseDto> executeCheck(
            @Parameter(description = "Check ID") @PathVariable String checkId,
            @Valid @RequestBody ExecuteCheckRequestDto request) {

        ComplianceRule.SeverityLevel severity = request.getSeverity() != null
            ? ComplianceRule.SeverityLevel.valueOf(request.getSeverity().name())
            : null;

        ComplianceCheckCommand.ExecuteCheckCommand command = new ComplianceCheckCommand.ExecuteCheckCommand(
            RequestContextHolder.getTenantId(),
            checkId,
            RequestContextHolder.getUserId().orElse("system"),
            request.getResult(),
            request.getViolationDescription(),
            severity,
            request.getContext(),
            request.getVariance()
        );

        ComplianceCheck check = checkService.executeCheck(command);
        return ResponseEntity.ok(toDto(check));
    }

    @PostMapping("/evaluate")
    @Operation(summary = "Evaluate an entity against active rules")
    public ResponseEntity<ComplianceCheckResponseDto> evaluate(
            @Valid @RequestBody EvaluateRequestDto request) {

        ComplianceCheck check = checkService.evaluateAgainstRule(
            RequestContextHolder.getTenantId(),
            request.getRuleId(),
            request.getEntityType(),
            request.getEntityId(),
            request.getContext(),
            RequestContextHolder.getUserId().orElse("system")
        );

        return ResponseEntity.ok(toDto(check));
    }

    @GetMapping("/{checkId}")
    @Operation(summary = "Get compliance check by ID")
    public ResponseEntity<ComplianceCheckResponseDto> getCheck(
            @Parameter(description = "Check ID") @PathVariable String checkId) {
        ComplianceCheck check = queryService.getCheckById(checkId);
        return ResponseEntity.ok(toDto(check));
    }

    @GetMapping
    @Operation(summary = "Get all compliance checks for tenant")
    public ResponseEntity<Page<ComplianceCheckResponseDto>> getChecks(
            @RequestParam(required = false) String ruleId,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String entityId,
            @RequestParam(required = false) ComplianceCheck.CheckResult result,
            @RequestParam(required = false) ComplianceCheck.SeverityLevel severity,
            @RequestParam(required = false) Boolean requiresAction,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "evaluatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<ComplianceCheck> checks = queryService.getChecks(
            ruleId, entityType, entityId, result, severity, requiresAction,
            startDate, endDate, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(checks.map(this::toDto));
    }

    @GetMapping("/violations")
    @Operation(summary = "Get all violations")
    public ResponseEntity<List<ComplianceCheckResponseDto>> getViolations(
            @RequestParam(required = false) ComplianceCheck.SeverityLevel minSeverity,
            @RequestParam(defaultValue = "false") Boolean includeWaived,
            @RequestParam(defaultValue = "false") Boolean includeRemediated) {

        List<ComplianceCheck> checks = queryService.getViolations(minSeverity, includeWaived, includeRemediated);
        return ResponseEntity.ok(checks.stream().map(this::toDto).toList());
    }

    @GetMapping("/pending-remediation")
    @Operation(summary = "Get pending remediations")
    public ResponseEntity<List<ComplianceCheckResponseDto>> getPendingRemediation(
            @RequestParam(required = false) String assignedTo,
            @RequestParam(required = false) LocalDate dueBefore) {

        List<ComplianceCheck> checks = queryService.getPendingRemediation(assignedTo, dueBefore);
        return ResponseEntity.ok(checks.stream().map(this::toDto).toList());
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "Get checks by entity")
    public ResponseEntity<List<ComplianceCheckResponseDto>> getChecksByEntity(
            @Parameter(description = "Entity Type") @PathVariable String entityType,
            @Parameter(description = "Entity ID") @PathVariable String entityId) {

        List<ComplianceCheck> checks = queryService.getChecksByEntity(entityType, entityId);
        return ResponseEntity.ok(checks.stream().map(this::toDto).toList());
    }

    @PostMapping("/{checkId}/approve")
    @Operation(summary = "Approve a compliance check")
    public ResponseEntity<Void> approveCheck(
            @Parameter(description = "Check ID") @PathVariable String checkId,
            @RequestBody ApproveCheckRequestDto request) {

        ComplianceCheckCommand.ApproveCheckCommand command = new ComplianceCheckCommand.ApproveCheckCommand(
            RequestContextHolder.getTenantId(),
            checkId,
            RequestContextHolder.getUserId().orElse("system"),
            request.getNotes()
        );

        checkService.approve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{checkId}/waive")
    @Operation(summary = "Waive a compliance violation")
    public ResponseEntity<Void> waiveViolation(
            @Parameter(description = "Check ID") @PathVariable String checkId,
            @RequestBody WaiveViolationRequestDto request) {

        ComplianceCheckCommand.WaiveViolationCommand command = new ComplianceCheckCommand.WaiveViolationCommand(
            RequestContextHolder.getTenantId(),
            checkId,
            RequestContextHolder.getUserId().orElse("system"),
            request.getReason()
        );

        checkService.waiveViolation(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{checkId}/remediation/assign")
    @Operation(summary = "Assign remediation")
    public ResponseEntity<Void> assignRemediation(
            @Parameter(description = "Check ID") @PathVariable String checkId,
            @RequestBody AssignRemediationRequestDto request) {

        ComplianceCheckCommand.AssignRemediationCommand command = new ComplianceCheckCommand.AssignRemediationCommand(
            RequestContextHolder.getTenantId(),
            checkId,
            request.getAssignedTo(),
            request.getAction(),
            request.getDueDate()
        );

        checkService.assignRemediation(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{checkId}/remediation/complete")
    @Operation(summary = "Complete remediation")
    public ResponseEntity<Void> completeRemediation(
            @Parameter(description = "Check ID") @PathVariable String checkId) {

        ComplianceCheckCommand.CompleteRemediationCommand command = new ComplianceCheckCommand.CompleteRemediationCommand(
            RequestContextHolder.getTenantId(),
            checkId,
            RequestContextHolder.getUserId().orElse("system")
        );

        checkService.completeRemediation(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{checkId}")
    @Operation(summary = "Delete a compliance check")
    public ResponseEntity<Void> deleteCheck(
            @Parameter(description = "Check ID") @PathVariable String checkId) {

        ComplianceCheckCommand.DeleteCheckCommand command = new ComplianceCheckCommand.DeleteCheckCommand(
            RequestContextHolder.getTenantId(),
            checkId
        );

        checkService.delete(command);
        return ResponseEntity.noContent().build();
    }

    private ComplianceCheckResponseDto toDto(ComplianceCheck check) {
        return ComplianceCheckResponseDto.builder()
            .id(check.getId())
            .checkId(check.getCheckId())
            .tenantId(check.getTenantId())
            .ruleId(check.getRuleId())
            .ruleName(check.getRuleName())
            .entityType(check.getEntityType())
            .entityId(check.getEntityId())
            .referenceNumber(check.getReferenceNumber())
            .status(mapStatus(check.getStatus()))
            .result(mapResult(check.getResult()))
            .severity(mapSeverity(check.getSeverity()))
            .violationDescription(check.getViolationDescription())
            .evaluatedContext(check.getEvaluatedContext())
            .evaluatedAmount(check.getEvaluatedAmount())
            .evaluatedCurrency(check.getEvaluatedCurrency())
            .thresholdAmount(check.getThresholdAmount())
            .thresholdCurrency(check.getThresholdCurrency())
            .variance(check.getVariance())
            .department(check.getDepartment())
            .costCenter(check.getCostCenter())
            .expenseCategory(check.getExpenseCategory())
            .evaluatedBy(check.getEvaluatedBy())
            .evaluatedByUserId(check.getEvaluatedByUserId())
            .evaluatedAt(check.getEvaluatedAt())
            .approvedBy(check.getApprovedBy())
            .approvedAt(check.getApprovedAt())
            .approvalNotes(check.getApprovalNotes())
            .waived(check.getWaived())
            .waivedBy(check.getWaivedBy())
            .waivedAt(check.getWaivedAt())
            .waiverReason(check.getWaiverReason())
            .remediationRequired(check.getRemediationRequired())
            .remediationAction(check.getRemediationAction())
            .remediationAssignedTo(check.getRemediationAssignedTo())
            .remediationDueDate(check.getRemediationDueDate())
            .remediationCompletedAt(check.getRemediationCompletedAt())
            .tags(check.getTags())
            .notes(check.getNotes())
            .correlationId(check.getCorrelationId())
            .createdAt(check.getCreatedAt())
            .updatedAt(check.getUpdatedAt())
            .build();
    }

    private ComplianceCheckResponseDto.CheckStatusDto mapStatus(ComplianceCheck.CheckStatus status) {
        return status != null ? ComplianceCheckResponseDto.CheckStatusDto.valueOf(status.name()) : null;
    }

    private ComplianceCheckResponseDto.CheckResultDto mapResult(ComplianceCheck.CheckResult result) {
        return result != null ? ComplianceCheckResponseDto.CheckResultDto.valueOf(result.name()) : null;
    }

    private ComplianceCheckResponseDto.SeverityLevelDto mapSeverity(ComplianceCheck.SeverityLevel severity) {
        return severity != null ? ComplianceCheckResponseDto.SeverityLevelDto.valueOf(severity.name()) : null;
    }

    // Request DTOs
    @lombok.Data
    public static class CreateCheckRequestDto {
        public String ruleId;
        public String ruleName;
        public String entityType;
        public String entityId;
        public String referenceNumber;
        public Map<String, Object> context;
        public BigDecimal evaluatedAmount;
        public String evaluatedCurrency;
        public BigDecimal thresholdAmount;
        public String thresholdCurrency;
        public String department;
        public String costCenter;
        public String expenseCategory;
        public String correlationId;
    }

    @lombok.Data
    public static class ExecuteCheckRequestDto {
        public ComplianceCheck.CheckResult result;
        public String violationDescription;
        public ComplianceCheck.SeverityLevel severity;
        public Map<String, Object> context;
        public BigDecimal variance;
    }

    @lombok.Data
    public static class EvaluateRequestDto {
        public String ruleId;
        public String entityType;
        public String entityId;
        public Map<String, Object> context;
    }

    @lombok.Data
    public static class ApproveCheckRequestDto {
        public String notes;
    }

    @lombok.Data
    public static class WaiveViolationRequestDto {
        public String reason;
    }

    @lombok.Data
    public static class AssignRemediationRequestDto {
        public String assignedTo;
        public String action;
        public Instant dueDate;
    }
}
