package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.application.service.IssueCommandService;
import com.gogidix.hr.globalcompliance.application.service.IssueQueryService;
import com.gogidix.hr.globalcompliance.domain.model.NonComplianceIssue;
import com.gogidix.hr.globalcompliance.domain.port.in.IssueCommand;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Non-Compliance Issue REST Controller
 * Handles HTTP requests for non-compliance issue operations
 */
@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
@Tag(name = "Non-Compliance Issues", description = "Non-compliance issue management endpoints")
public class IssueController {

    private final IssueCommandService issueCommandService;
    private final IssueQueryService issueQueryService;

    @PostMapping
    @Operation(summary = "Create a new non-compliance issue")
    public ResponseEntity<NonComplianceIssue> createIssue(
            @Valid @RequestBody IssueCommand.CreateIssueCommand command) {
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setIdentifiedBy(RequestContextHolder.getUserId());
        NonComplianceIssue issue = issueCommandService.create(command);
        return ResponseEntity.status(201).body(issue);
    }

    @GetMapping
    @Operation(summary = "Get all non-compliance issues")
    public ResponseEntity<Page<NonComplianceIssue>> getAllIssues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NonComplianceIssue> issues = issueQueryService.getAllForTenant();
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/{issueId}")
    @Operation(summary = "Get issue by ID")
    public ResponseEntity<NonComplianceIssue> getIssue(
            @Parameter(description = "Issue ID") @PathVariable String issueId) {
        NonComplianceIssue issue = issueQueryService.getById(issueId);
        return ResponseEntity.ok(issue);
    }

    @PutMapping("/{issueId}")
    @Operation(summary = "Update issue")
    public ResponseEntity<Void> updateIssue(
            @Parameter(description = "Issue ID") @PathVariable String issueId,
            @RequestBody UpdateIssueRequestDto request) {
        if (request.getAssignedTo() != null) {
            IssueCommand.AssignIssueCommand assignCommand = new IssueCommand.AssignIssueCommand(
                    RequestContextHolder.getTenantId(), issueId,
                    request.getAssignedTo(), request.getAssignedToName());
            issueCommandService.assign(assignCommand);
        }
        if (request.getFinancialImpact() != null) {
            IssueCommand.SetFinancialImpactCommand financialCommand = new IssueCommand.SetFinancialImpactCommand(
                    RequestContextHolder.getTenantId(), issueId,
                    request.getFinancialImpact(), request.getCurrency());
            issueCommandService.setFinancialImpact(financialCommand);
        }
        if (request.getAction() != null) {
            IssueCommand.AddActionCommand actionCommand = new IssueCommand.AddActionCommand(
                    RequestContextHolder.getTenantId(), issueId, request.getAction());
            issueCommandService.addAction(actionCommand);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{issueId}/resolve")
    @Operation(summary = "Resolve issue")
    public ResponseEntity<Void> resolveIssue(
            @Parameter(description = "Issue ID") @PathVariable String issueId,
            @RequestBody ResolveIssueRequestDto request) {
        IssueCommand.ResolveIssueCommand command = new IssueCommand.ResolveIssueCommand(
                RequestContextHolder.getTenantId(), issueId,
                request.getResolution(), request.getRootCause(), RequestContextHolder.getUserId());
        issueCommandService.resolve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{issueId}/escalate")
    @Operation(summary = "Escalate issue")
    public ResponseEntity<Void> escalateIssue(
            @Parameter(description = "Issue ID") @PathVariable String issueId,
            @RequestBody EscalateIssueRequestDto request) {
        IssueCommand.EscalateIssueCommand command = new IssueCommand.EscalateIssueCommand(
                RequestContextHolder.getTenantId(), issueId,
                request.getReason(), RequestContextHolder.getUserId());
        issueCommandService.escalate(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-severity/{severity}")
    @Operation(summary = "Get issues by severity")
    public ResponseEntity<Page<NonComplianceIssue>> getBySeverity(
            @Parameter(description = "Severity") @PathVariable String severity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NonComplianceIssue> issues = issueQueryService.getBySeverity(severity, page, size);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/by-status/{status}")
    @Operation(summary = "Get issues by status")
    public ResponseEntity<Page<NonComplianceIssue>> getByStatus(
            @Parameter(description = "Status") @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NonComplianceIssue> issues = issueQueryService.getByStatus(status, page, size);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/open")
    @Operation(summary = "Get open issues")
    public ResponseEntity<Page<NonComplianceIssue>> getOpenIssues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NonComplianceIssue> issues = issueQueryService.getOpenIssues(page, size);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue issues")
    public ResponseEntity<Page<NonComplianceIssue>> getOverdueIssues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NonComplianceIssue> issues = issueQueryService.getOverdueIssues(page, size);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/critical")
    @Operation(summary = "Get critical issues")
    public ResponseEntity<Page<NonComplianceIssue>> getCriticalIssues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NonComplianceIssue> issues = issueQueryService.getCriticalIssues(page, size);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/by-assignee/{assignedTo}")
    @Operation(summary = "Get issues by assignee")
    public ResponseEntity<Page<NonComplianceIssue>> getByAssignedTo(
            @Parameter(description = "Assigned To") @PathVariable String assignedTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NonComplianceIssue> issues = issueQueryService.getByAssignedTo(assignedTo, page, size);
        return ResponseEntity.ok(issues);
    }

    @PostMapping("/{issueId}/start-progress")
    @Operation(summary = "Start progress on issue")
    public ResponseEntity<Void> startProgress(
            @Parameter(description = "Issue ID") @PathVariable String issueId) {
        IssueCommand.StartProgressCommand command = new IssueCommand.StartProgressCommand(
                RequestContextHolder.getTenantId(), issueId);
        issueCommandService.startProgress(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{issueId}/close")
    @Operation(summary = "Close issue")
    public ResponseEntity<Void> closeIssue(
            @Parameter(description = "Issue ID") @PathVariable String issueId) {
        IssueCommand.CloseIssueCommand command = new IssueCommand.CloseIssueCommand(
                RequestContextHolder.getTenantId(), issueId, RequestContextHolder.getUserId());
        issueCommandService.close(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{issueId}/reopen")
    @Operation(summary = "Reopen issue")
    public ResponseEntity<Void> reopenIssue(
            @Parameter(description = "Issue ID") @PathVariable String issueId,
            @RequestBody ReopenIssueRequestDto request) {
        IssueCommand.ReopenIssueCommand command = new IssueCommand.ReopenIssueCommand(
                RequestContextHolder.getTenantId(), issueId, request.getReason());
        issueCommandService.reopen(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{issueId}/update-severity")
    @Operation(summary = "Update issue severity")
    public ResponseEntity<Void> updateSeverity(
            @Parameter(description = "Issue ID") @PathVariable String issueId,
            @RequestBody UpdateSeverityRequestDto request) {
        IssueCommand.UpdateSeverityCommand command = new IssueCommand.UpdateSeverityCommand(
                RequestContextHolder.getTenantId(), issueId,
                request.getNewSeverity(), request.getReason());
        issueCommandService.updateSeverity(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{issueId}/update-due-date")
    @Operation(summary = "Update issue due date")
    public ResponseEntity<Void> updateDueDate(
            @Parameter(description = "Issue ID") @PathVariable String issueId,
            @RequestBody UpdateDueDateRequestDto request) {
        IssueCommand.UpdateDueDateCommand command = new IssueCommand.UpdateDueDateCommand(
                RequestContextHolder.getTenantId(), issueId,
                request.getNewDueDate(), request.getReason());
        issueCommandService.updateDueDate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{issueId}/affected-employees")
    @Operation(summary = "Add affected employee")
    public ResponseEntity<Void> addAffectedEmployee(
            @Parameter(description = "Issue ID") @PathVariable String issueId,
            @RequestBody AffectedEmployeeRequestDto request) {
        IssueCommand.AddAffectedEmployeeCommand command = new IssueCommand.AddAffectedEmployeeCommand(
                RequestContextHolder.getTenantId(), issueId, request.getEmployeeId());
        issueCommandService.addAffectedEmployee(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{issueId}")
    @Operation(summary = "Delete issue")
    public ResponseEntity<Void> deleteIssue(
            @Parameter(description = "Issue ID") @PathVariable String issueId) {
        IssueCommand.DeleteIssueCommand command = new IssueCommand.DeleteIssueCommand(
                RequestContextHolder.getTenantId(), issueId);
        issueCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @lombok.Data
    public static class UpdateIssueRequestDto {
        private String assignedTo;
        private String assignedToName;
        private Double financialImpact;
        private String currency;
        private String action;
    }

    @lombok.Data
    public static class ResolveIssueRequestDto {
        private String resolution;
        private String rootCause;
    }

    @lombok.Data
    public static class EscalateIssueRequestDto {
        private String reason;
    }

    @lombok.Data
    public static class ReopenIssueRequestDto {
        private String reason;
    }

    @lombok.Data
    public static class UpdateSeverityRequestDto {
        private String newSeverity;
        private String reason;
    }

    public static class UpdateDueDateRequestDto {
        private LocalDate newDueDate;
        private String reason;

        public LocalDate getNewDueDate() {
            return newDueDate;
        }

        public String getReason() {
            return reason;
        }

        public void setNewDueDate(LocalDate newDueDate) {
            this.newDueDate = newDueDate;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public static class AffectedEmployeeRequestDto {
        private String employeeId;

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }
    }
}
