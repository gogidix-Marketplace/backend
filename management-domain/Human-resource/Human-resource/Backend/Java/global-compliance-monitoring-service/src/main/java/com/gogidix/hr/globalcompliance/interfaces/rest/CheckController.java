package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.application.service.CheckCommandService;
import com.gogidix.hr.globalcompliance.application.service.CheckQueryService;
import com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck;
import com.gogidix.hr.globalcompliance.domain.port.in.CheckCommand;
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
 * Compliance Check REST Controller
 * Handles HTTP requests for compliance check operations
 */
@RestController
@RequestMapping("/checks")
@RequiredArgsConstructor
@Tag(name = "Compliance Checks", description = "Compliance check management endpoints")
public class CheckController {

    private final CheckCommandService checkCommandService;
    private final CheckQueryService checkQueryService;

    @PostMapping
    @Operation(summary = "Create a new compliance check")
    public ResponseEntity<ComplianceCheck> createCheck(
            @Valid @RequestBody CheckCommand.CreateCheckCommand command) {
        command.setTenantId(RequestContextHolder.getTenantId());
        ComplianceCheck check = checkCommandService.create(command);
        return ResponseEntity.status(201).body(check);
    }

    @GetMapping
    @Operation(summary = "Get all compliance checks")
    public ResponseEntity<Page<ComplianceCheck>> getAllChecks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceCheck> checks = checkQueryService.getAllForTenant();
        return ResponseEntity.ok(checks);
    }

    @GetMapping("/{checkId}")
    @Operation(summary = "Get check by ID")
    public ResponseEntity<ComplianceCheck> getCheck(
            @Parameter(description = "Check ID") @PathVariable String checkId) {
        ComplianceCheck check = checkQueryService.getById(checkId);
        return ResponseEntity.ok(check);
    }

    @PutMapping("/{checkId}")
    @Operation(summary = "Update check")
    public ResponseEntity<Void> updateCheck(
            @Parameter(description = "Check ID") @PathVariable String checkId,
            @RequestBody UpdateCheckRequestDto request) {
        if (request.getCorrectiveAction() != null || request.getActualCompletionDate() != null) {
            CheckCommand.UpdateCorrectiveActionCommand command = new CheckCommand.UpdateCorrectiveActionCommand(
                    RequestContextHolder.getTenantId(), checkId,
                    request.getCorrectiveAction(), request.getActualCompletionDate());
            checkCommandService.updateCorrectiveAction(command);
        }
        if (request.getComment() != null) {
            CheckCommand.AddCommentCommand commentCommand = new CheckCommand.AddCommentCommand(
                    RequestContextHolder.getTenantId(), checkId, request.getComment());
            checkCommandService.addComment(commentCommand);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{checkId}/start")
    @Operation(summary = "Start check")
    public ResponseEntity<Void> startCheck(
            @Parameter(description = "Check ID") @PathVariable String checkId,
            @RequestBody StartCheckRequestDto request) {
        CheckCommand.StartCheckCommand command = new CheckCommand.StartCheckCommand(
                RequestContextHolder.getTenantId(), checkId,
                RequestContextHolder.getUserId(), request.getCheckedByName());
        checkCommandService.start(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{checkId}/complete")
    @Operation(summary = "Complete check")
    public ResponseEntity<Void> completeCheck(
            @Parameter(description = "Check ID") @PathVariable String checkId,
            @RequestBody CompleteCheckRequestDto request) {
        CheckCommand.CompleteCheckCommand command = new CheckCommand.CompleteCheckCommand(
                RequestContextHolder.getTenantId(), checkId,
                request.getResult(), request.getFindings(), request.getCorrectiveAction(),
                request.getTargetCompletionDate(), request.getSupportingDocuments());
        checkCommandService.complete(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{checkId}/pass")
    @Operation(summary = "Mark check as passed")
    public ResponseEntity<Void> markAsPassed(
            @Parameter(description = "Check ID") @PathVariable String checkId,
            @RequestBody PassCheckRequestDto request) {
        CheckCommand.PassCheckCommand command = new CheckCommand.PassCheckCommand(
                RequestContextHolder.getTenantId(), checkId,
                request.getFindings(), request.getSupportingDocuments());
        checkCommandService.pass(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{checkId}/fail")
    @Operation(summary = "Mark check as failed")
    public ResponseEntity<Void> markAsFailed(
            @Parameter(description = "Check ID") @PathVariable String checkId,
            @RequestBody FailCheckRequestDto request) {
        CheckCommand.FailCheckCommand command = new CheckCommand.FailCheckCommand(
                RequestContextHolder.getTenantId(), checkId,
                request.getFindings(), request.getCorrectiveAction(),
                request.getTargetCompletionDate(), request.getSupportingDocuments());
        checkCommandService.fail(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{checkId}/waive")
    @Operation(summary = "Waive check")
    public ResponseEntity<Void> waiveCheck(
            @Parameter(description = "Check ID") @PathVariable String checkId,
            @RequestBody WaiveCheckRequestDto request) {
        CheckCommand.WaiveCheckCommand command = new CheckCommand.WaiveCheckCommand(
                RequestContextHolder.getTenantId(), checkId,
                request.getReason(), RequestContextHolder.getUserId());
        checkCommandService.waive(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-requirement/{requirementId}")
    @Operation(summary = "Get checks by requirement")
    public ResponseEntity<Page<ComplianceCheck>> getByRequirement(
            @Parameter(description = "Requirement ID") @PathVariable String requirementId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceCheck> checks = checkQueryService.getByRequirementId(requirementId, page, size);
        return ResponseEntity.ok(checks);
    }

    @GetMapping("/by-country/{countryCode}")
    @Operation(summary = "Get checks by country")
    public ResponseEntity<Page<ComplianceCheck>> getByCountry(
            @Parameter(description = "Country Code") @PathVariable String countryCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceCheck> checks = checkQueryService.getByCountryCode(countryCode, page, size);
        return ResponseEntity.ok(checks);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue checks")
    public ResponseEntity<Page<ComplianceCheck>> getOverdueChecks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceCheck> checks = checkQueryService.getOverdueChecks(page, size);
        return ResponseEntity.ok(checks);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming checks")
    public ResponseEntity<Page<ComplianceCheck>> getUpcomingChecks(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceCheck> checks = checkQueryService.getUpcomingChecks(fromDate, toDate, page, size);
        return ResponseEntity.ok(checks);
    }

    @GetMapping("/by-status/{status}")
    @Operation(summary = "Get checks by status")
    public ResponseEntity<Page<ComplianceCheck>> getByStatus(
            @Parameter(description = "Status") @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceCheck> checks = checkQueryService.getByStatus(status, page, size);
        return ResponseEntity.ok(checks);
    }

    @GetMapping("/by-result/{result}")
    @Operation(summary = "Get checks by result")
    public ResponseEntity<Page<ComplianceCheck>> getByResult(
            @Parameter(description = "Result") @PathVariable String result,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceCheck> checks = checkQueryService.getByResult(result, page, size);
        return ResponseEntity.ok(checks);
    }

    @DeleteMapping("/{checkId}")
    @Operation(summary = "Delete check")
    public ResponseEntity<Void> deleteCheck(
            @Parameter(description = "Check ID") @PathVariable String checkId) {
        CheckCommand.DeleteCheckCommand command = new CheckCommand.DeleteCheckCommand(
                RequestContextHolder.getTenantId(), checkId);
        checkCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @lombok.Data
    public static class UpdateCheckRequestDto {
        private String correctiveAction;
        private LocalDate actualCompletionDate;
        private String comment;
    }

    @lombok.Data
    public static class StartCheckRequestDto {
        private String checkedByName;
    }

    @lombok.Data
    public static class CompleteCheckRequestDto {
        private String result;
        private String findings;
        private String correctiveAction;
        private LocalDate targetCompletionDate;
        private List<String> supportingDocuments;
    }

    @lombok.Data
    public static class PassCheckRequestDto {
        private String findings;
        private List<String> supportingDocuments;
    }

    @lombok.Data
    public static class FailCheckRequestDto {
        private String findings;
        private String correctiveAction;
        private LocalDate targetCompletionDate;
        private List<String> supportingDocuments;
    }

    @lombok.Data
    public static class WaiveCheckRequestDto {
        private String reason;
    }
}
