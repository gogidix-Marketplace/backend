package com.gogidix.finance.compliance.interfaces.rest;

import com.gogidix.finance.compliance.application.dto.response.ComplianceRuleResponseDto;
import com.gogidix.finance.compliance.application.service.ComplianceQueryService;
import com.gogidix.finance.compliance.application.service.ComplianceRuleService;
import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import com.gogidix.finance.compliance.domain.port.in.ComplianceRuleCommand;
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
import java.util.List;
import java.util.Map;

/**
 * Compliance Rule REST Controller
 * Handles HTTP requests for compliance rule operations
 */
@RestController
@RequestMapping("/compliance/rules")
@RequiredArgsConstructor
@Tag(name = "Compliance Rules", description = "Compliance rule management endpoints")
public class ComplianceRuleController {

    private final ComplianceRuleService ruleService;
    private final ComplianceQueryService queryService;

    @PostMapping
    @Operation(summary = "Create a new compliance rule")
    public ResponseEntity<ComplianceRuleResponseDto> createRule(
            @Valid @RequestBody CreateRuleRequestDto request) {
        ComplianceRuleCommand.CreateComplianceRuleCommand command = new ComplianceRuleCommand.CreateComplianceRuleCommand();
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setName(request.getName());
        command.setDescription(request.getDescription());
        command.setRuleType(request.getRuleType());
        command.setCategory(request.getCategory());
        command.setSeverity(request.getSeverity());
        command.setCreatedByUserId(RequestContextHolder.getUserId().orElse("system"));

        // Set optional fields
        command.setParameters(request.getParameters());
        command.setThresholdAmount(request.getThresholdAmount());
        command.setThresholdCurrency(request.getThresholdCurrency());
        command.setConditionExpression(request.getConditionExpression());
        command.setApplicableDepartments(request.getApplicableDepartments());
        command.setApplicableCostCenters(request.getApplicableCostCenters());
        command.setApplicableExpenseCategories(request.getApplicableExpenseCategories());
        command.setEffectiveFrom(request.getEffectiveFrom());
        command.setEffectiveTo(request.getEffectiveTo());
        command.setApprovalRequiredBy(request.getApprovalRequiredBy());
        command.setAutoApproveThreshold(request.getAutoApproveThreshold());
        command.setPriority(request.getPriority());
        command.setTags(request.getTags());
        command.setNotes(request.getNotes());

        ComplianceRule rule = ruleService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(rule));
    }

    @GetMapping("/{ruleId}")
    @Operation(summary = "Get compliance rule by ID")
    public ResponseEntity<ComplianceRuleResponseDto> getRule(
            @Parameter(description = "Rule ID") @PathVariable String ruleId) {
        ComplianceRule rule = queryService.getRuleById(ruleId);
        return ResponseEntity.ok(toDto(rule));
    }

    @GetMapping
    @Operation(summary = "Get all compliance rules for tenant")
    public ResponseEntity<Page<ComplianceRuleResponseDto>> getRules(
            @RequestParam(required = false) ComplianceRule.RuleType ruleType,
            @RequestParam(required = false) ComplianceRule.RuleCategory category,
            @RequestParam(required = false) ComplianceRule.RuleStatus status,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<ComplianceRule> rules = queryService.getRules(
            ruleType, category, status, enabled, department, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(rules.map(this::toDto));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active compliance rules")
    public ResponseEntity<List<ComplianceRuleResponseDto>> getActiveRules() {
        List<ComplianceRule> rules = queryService.getActiveRules();
        return ResponseEntity.ok(rules.stream().map(this::toDto).toList());
    }

    @PutMapping("/{ruleId}")
    @Operation(summary = "Update a compliance rule")
    public ResponseEntity<ComplianceRuleResponseDto> updateRule(
            @Parameter(description = "Rule ID") @PathVariable String ruleId,
            @Valid @RequestBody UpdateRuleRequestDto request) {

        ComplianceRuleCommand.UpdateComplianceRuleCommand command = new ComplianceRuleCommand.UpdateComplianceRuleCommand(
            RequestContextHolder.getTenantId(),
            ruleId,
            request.getName(),
            request.getDescription(),
            request.getParameters(),
            request.getThresholdAmount(),
            request.getThresholdCurrency(),
            request.getConditionExpression(),
            request.getApplicableDepartments(),
            request.getApplicableCostCenters(),
            request.getApplicableExpenseCategories(),
            RequestContextHolder.getUserId().orElse("system"),
            request.getEffectiveFrom(),
            request.getEffectiveTo(),
            request.getPriority(),
            request.getTags(),
            request.getNotes()
        );

        ComplianceRule rule = ruleService.update(command);
        return ResponseEntity.ok(toDto(rule));
    }

    @PostMapping("/{ruleId}/activate")
    @Operation(summary = "Activate a compliance rule")
    public ResponseEntity<Void> activateRule(
            @Parameter(description = "Rule ID") @PathVariable String ruleId) {

        ComplianceRuleCommand.ActivateRuleCommand command = new ComplianceRuleCommand.ActivateRuleCommand(
            RequestContextHolder.getTenantId(),
            ruleId,
            RequestContextHolder.getUserId().orElse("system")
        );

        ruleService.activate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{ruleId}/deactivate")
    @Operation(summary = "Deactivate a compliance rule")
    public ResponseEntity<Void> deactivateRule(
            @Parameter(description = "Rule ID") @PathVariable String ruleId) {

        ComplianceRuleCommand.DeactivateRuleCommand command = new ComplianceRuleCommand.DeactivateRuleCommand(
            RequestContextHolder.getTenantId(),
            ruleId,
            RequestContextHolder.getUserId().orElse("system")
        );

        ruleService.deactivate(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{ruleId}")
    @Operation(summary = "Delete a compliance rule")
    public ResponseEntity<Void> deleteRule(
            @Parameter(description = "Rule ID") @PathVariable String ruleId) {

        ComplianceRuleCommand.DeleteRuleCommand command = new ComplianceRuleCommand.DeleteRuleCommand(
            RequestContextHolder.getTenantId(),
            ruleId
        );

        ruleService.delete(command);
        return ResponseEntity.noContent().build();
    }

    private ComplianceRuleResponseDto toDto(ComplianceRule rule) {
        return ComplianceRuleResponseDto.builder()
            .id(rule.getId())
            .ruleId(rule.getRuleId())
            .tenantId(rule.getTenantId())
            .name(rule.getName())
            .description(rule.getDescription())
            .ruleType(mapRuleType(rule.getRuleType()))
            .category(mapCategory(rule.getCategory()))
            .severity(mapSeverity(rule.getSeverity()))
            .enabled(rule.getEnabled())
            .parameters(rule.getParameters())
            .thresholdAmount(rule.getThresholdAmount())
            .thresholdCurrency(rule.getThresholdCurrency())
            .conditionExpression(rule.getConditionExpression())
            .applicableDepartments(rule.getApplicableDepartments())
            .applicableCostCenters(rule.getApplicableCostCenters())
            .applicableExpenseCategories(rule.getApplicableExpenseCategories())
            .createdByUserId(rule.getCreatedByUserId())
            .lastModifiedByUserId(rule.getLastModifiedByUserId())
            .effectiveFrom(rule.getEffectiveFrom())
            .effectiveTo(rule.getEffectiveTo())
            .status(mapStatus(rule.getStatus()))
            .approvalRequiredBy(rule.getApprovalRequiredBy())
            .autoApproveThreshold(rule.getAutoApproveThreshold())
            .priority(rule.getPriority())
            .tags(rule.getTags())
            .notes(rule.getNotes())
            .createdAt(rule.getCreatedAt())
            .updatedAt(rule.getUpdatedAt())
            .build();
    }

    private ComplianceRuleResponseDto.RuleTypeDto mapRuleType(ComplianceRule.RuleType ruleType) {
        return ruleType != null ? ComplianceRuleResponseDto.RuleTypeDto.valueOf(ruleType.name()) : null;
    }

    private ComplianceRuleResponseDto.RuleCategoryDto mapCategory(ComplianceRule.RuleCategory category) {
        return category != null ? ComplianceRuleResponseDto.RuleCategoryDto.valueOf(category.name()) : null;
    }

    private ComplianceRuleResponseDto.SeverityLevelDto mapSeverity(ComplianceRule.SeverityLevel severity) {
        return severity != null ? ComplianceRuleResponseDto.SeverityLevelDto.valueOf(severity.name()) : null;
    }

    private ComplianceRuleResponseDto.RuleStatusDto mapStatus(ComplianceRule.RuleStatus status) {
        return status != null ? ComplianceRuleResponseDto.RuleStatusDto.valueOf(status.name()) : null;
    }

    // Request DTOs
    @lombok.Data
    public static class CreateRuleRequestDto {
        public String name;
        public String description;
        public ComplianceRule.RuleType ruleType;
        public ComplianceRule.RuleCategory category;
        public ComplianceRule.SeverityLevel severity;
        public Map<String, Object> parameters;
        public BigDecimal thresholdAmount;
        public String thresholdCurrency;
        public String conditionExpression;
        public List<String> applicableDepartments;
        public List<String> applicableCostCenters;
        public List<String> applicableExpenseCategories;
        public Instant effectiveFrom;
        public Instant effectiveTo;
        public String approvalRequiredBy;
        public Boolean autoApproveThreshold;
        public Integer priority;
        public List<String> tags;
        public String notes;
    }

    @lombok.Data
    public static class UpdateRuleRequestDto {
        public String name;
        public String description;
        public Map<String, Object> parameters;
        public BigDecimal thresholdAmount;
        public String thresholdCurrency;
        public String conditionExpression;
        public List<String> applicableDepartments;
        public List<String> applicableCostCenters;
        public List<String> applicableExpenseCategories;
        public Instant effectiveFrom;
        public Instant effectiveTo;
        public Integer priority;
        public List<String> tags;
        public String notes;
    }
}
