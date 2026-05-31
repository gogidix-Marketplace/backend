package com.gogidix.finance.budgettracking.interfaces.rest;

import com.gogidix.finance.budgettracking.application.dto.response.BudgetMonitorResponseDto;
import com.gogidix.finance.budgettracking.application.service.BudgetMonitorService;
import com.gogidix.finance.budgettracking.domain.model.BudgetMonitor;
import com.gogidix.finance.budgettracking.domain.port.in.BudgetMonitorCommand;
import com.gogidix.finance.budgettracking.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Budget Monitor REST Controller
 * Handles HTTP requests for budget monitoring operations
 */
@RestController
@RequestMapping("/budget-monitors")
@RequiredArgsConstructor
@Tag(name = "Budget Monitors", description = "Budget monitoring endpoints")
public class BudgetMonitorController {

    private final BudgetMonitorService budgetMonitorService;

    @PostMapping
    @Operation(summary = "Create a new budget monitor")
    public ResponseEntity<BudgetMonitorResponseDto> createMonitor(
            @Valid @RequestBody CreateMonitorRequestDto request) {

        BudgetMonitorCommand.CreateMonitorCommand command =
            new BudgetMonitorCommand.CreateMonitorCommand();
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setBudgetId(request.getBudgetId());
        command.setBudgetCode(request.getBudgetCode());
        command.setBudgetName(request.getBudgetName());
        command.setBudgetPeriod(request.getBudgetPeriod());
        command.setPeriod(request.getPeriod());
        command.setAllocatedAmount(request.getAllocatedAmount());
        command.setCurrency(request.getCurrency());
        command.setCategory(request.getCategory());
        command.setDepartment(request.getDepartment());
        command.setCostCenter(request.getCostCenter());
        command.setFiscalYear(request.getFiscalYear());
        command.setCreatedBy(RequestContextHolder.getUserId().orElse(null));
        command.setAlertRecipients(request.getAlertRecipients());

        BudgetMonitor monitor = budgetMonitorService.createMonitor(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(monitor));
    }

    @PostMapping("/{monitorId}/expenditure")
    @Operation(summary = "Record expenditure against budget")
    public ResponseEntity<BudgetMonitorResponseDto> recordExpenditure(
            @Parameter(description = "Monitor ID") @PathVariable String monitorId,
            @RequestBody ExpenditureRequestDto request) {

        BudgetMonitor monitor = budgetMonitorService.recordExpenditure(monitorId, request.getAmount());
        return ResponseEntity.ok(toDto(monitor));
    }

    @PostMapping("/{monitorId}/commitment")
    @Operation(summary = "Record commitment against budget")
    public ResponseEntity<BudgetMonitorResponseDto> recordCommitment(
            @Parameter(description = "Monitor ID") @PathVariable String monitorId,
            @RequestBody CommitmentRequestDto request) {

        BudgetMonitor monitor = budgetMonitorService.recordCommitment(monitorId, request.getAmount());
        return ResponseEntity.ok(toDto(monitor));
    }

    @PostMapping("/{monitorId}/commitment/release")
    @Operation(summary = "Release commitment")
    public ResponseEntity<BudgetMonitorResponseDto> releaseCommitment(
            @Parameter(description = "Monitor ID") @PathVariable String monitorId,
            @RequestBody CommitmentRequestDto request) {

        BudgetMonitor monitor = budgetMonitorService.releaseCommitment(monitorId, request.getAmount());
        return ResponseEntity.ok(toDto(monitor));
    }

    @PostMapping("/{monitorId}/adjust")
    @Operation(summary = "Adjust budget allocation")
    public ResponseEntity<BudgetMonitorResponseDto> adjustAllocation(
            @Parameter(description = "Monitor ID") @PathVariable String monitorId,
            @RequestBody AdjustmentRequestDto request) {

        BudgetMonitor monitor = budgetMonitorService.adjustAllocation(
            monitorId, request.getNewAllocation());
        return ResponseEntity.ok(toDto(monitor));
    }

    @PostMapping("/{monitorId}/threshold/check")
    @Operation(summary = "Check and update threshold")
    public ResponseEntity<BudgetMonitorResponseDto> checkThreshold(
            @Parameter(description = "Monitor ID") @PathVariable String monitorId,
            @RequestBody ThresholdCheckRequestDto request) {

        BudgetMonitor monitor = budgetMonitorService.checkThreshold(
            monitorId, request.getThresholdType(), request.getThresholdValue(),
            BudgetMonitor.ThresholdLevel.valueOf(request.getLevel()));
        return ResponseEntity.ok(toDto(monitor));
    }

    @PostMapping("/{monitorId}/threshold/acknowledge")
    @Operation(summary = "Acknowledge threshold breach")
    public ResponseEntity<Void> acknowledgeThreshold(
            @Parameter(description = "Monitor ID") @PathVariable String monitorId,
            @RequestBody ThresholdAcknowledgeRequestDto request) {

        budgetMonitorService.acknowledgeThreshold(monitorId,
            request.getThresholdType(), RequestContextHolder.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{monitorId}")
    @Operation(summary = "Get budget monitor by ID")
    public ResponseEntity<BudgetMonitorResponseDto> getMonitor(
            @Parameter(description = "Monitor ID") @PathVariable String monitorId) {

        BudgetMonitor monitor = budgetMonitorService.getMonitorById(monitorId);
        return ResponseEntity.ok(toDto(monitor));
    }

    @GetMapping("/budget/{budgetId}")
    @Operation(summary = "Get monitor by budget ID")
    public ResponseEntity<BudgetMonitorResponseDto> getMonitorByBudget(
            @Parameter(description = "Budget ID") @PathVariable String budgetId) {

        BudgetMonitor monitor = budgetMonitorService.getMonitorByBudget(budgetId);
        return ResponseEntity.ok(toDto(monitor));
    }

    @GetMapping
    @Operation(summary = "Get all monitors for tenant")
    public ResponseEntity<List<BudgetMonitorResponseDto>> getAllMonitors() {
        List<BudgetMonitor> monitors = budgetMonitorService.getAllMonitors();
        return ResponseEntity.ok(monitors.stream()
            .map(this::toDto)
            .collect(Collectors.toList()));
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "Get monitors by period")
    public ResponseEntity<List<BudgetMonitorResponseDto>> getMonitorsByPeriod(
            @Parameter(description = "Period (YYYY-MM)") @PathVariable String period) {

        YearMonth yearMonth = YearMonth.parse(period);
        List<BudgetMonitor> monitors = budgetMonitorService.getMonitorsByPeriod(yearMonth);
        return ResponseEntity.ok(monitors.stream()
            .map(this::toDto)
            .collect(Collectors.toList()));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get monitors by status")
    public ResponseEntity<List<BudgetMonitorResponseDto>> getMonitorsByStatus(
            @Parameter(description = "Status") @PathVariable String status) {

        BudgetMonitor.MonitorStatus monitorStatus = BudgetMonitor.MonitorStatus.valueOf(status);
        List<BudgetMonitor> monitors = budgetMonitorService.getMonitorsByStatus(monitorStatus);
        return ResponseEntity.ok(monitors.stream()
            .map(this::toDto)
            .collect(Collectors.toList()));
    }

    @GetMapping("/critical")
    @Operation(summary = "Get all critical budgets")
    public ResponseEntity<List<BudgetMonitorResponseDto>> getCriticalBudgets() {
        List<BudgetMonitor> monitors = budgetMonitorService.getCriticalBudgets();
        return ResponseEntity.ok(monitors.stream()
            .map(this::toDto)
            .collect(Collectors.toList()));
    }

    private BudgetMonitorResponseDto toDto(BudgetMonitor monitor) {
        return BudgetMonitorResponseDto.builder()
            .id(monitor.getId())
            .monitorId(monitor.getMonitorId())
            .tenantId(monitor.getTenantId())
            .budgetId(monitor.getBudgetId())
            .budgetCode(monitor.getBudgetCode())
            .budgetName(monitor.getBudgetName())
            .budgetPeriod(monitor.getBudgetPeriod())
            .period(monitor.getPeriod())
            .allocatedAmount(monitor.getAllocatedAmount())
            .committedAmount(monitor.getCommittedAmount())
            .actualExpenditure(monitor.getActualExpenditure())
            .availableBalance(monitor.getAvailableBalance())
            .variance(monitor.getVariance())
            .utilizationPercentage(monitor.getUtilizationPercentage())
            .status(mapMonitorStatus(monitor.getStatus()))
            .category(monitor.getCategory())
            .department(monitor.getDepartment())
            .costCenter(monitor.getCostCenter())
            .fiscalYear(monitor.getFiscalYear())
            .createdBy(monitor.getCreatedBy())
            .lastUpdatedBy(monitor.getLastUpdatedBy())
            .lastCalculatedAt(monitor.getLastCalculatedAt())
            .thresholdStatuses(monitor.getThresholdStatuses() != null
                ? monitor.getThresholdStatuses().stream()
                    .map(this::mapThresholdStatus)
                    .toList()
                : List.of())
            .alertRecipients(monitor.getAlertRecipients())
            .currency(monitor.getCurrency())
            .thresholdBreached(monitor.isThresholdBreached())
            .warningCount(monitor.getWarningCount())
            .criticalCount(monitor.getCriticalCount())
            .createdAt(monitor.getCreatedAt())
            .updatedAt(monitor.getUpdatedAt())
            .build();
    }

    private BudgetMonitorResponseDto.MonitorStatusDto mapMonitorStatus(BudgetMonitor.MonitorStatus status) {
        return status != null ? BudgetMonitorResponseDto.MonitorStatusDto.valueOf(status.name()) : null;
    }

    private BudgetMonitorResponseDto.ThresholdStatusDto mapThresholdStatus(
            BudgetMonitor.ThresholdStatus status) {
        return BudgetMonitorResponseDto.ThresholdStatusDto.builder()
            .thresholdType(status.getThresholdType())
            .thresholdValue(status.getThresholdValue())
            .currentValue(status.getCurrentValue())
            .level(mapThresholdLevel(status.getLevel()))
            .breached(status.isBreached())
            .breachedAt(status.getBreachedAt())
            .acknowledged(status.isAcknowledged())
            .acknowledgedBy(status.getAcknowledgedBy())
            .acknowledgedAt(status.getAcknowledgedAt())
            .build();
    }

    private BudgetMonitorResponseDto.ThresholdLevelDto mapThresholdLevel(
            BudgetMonitor.ThresholdLevel level) {
        return level != null ? BudgetMonitorResponseDto.ThresholdLevelDto.valueOf(level.name()) : null;
    }

    // Request DTOs
    @lombok.Data
    @lombok.NoArgsConstructor
    public static class CreateMonitorRequestDto {
        private String budgetId;
        private String budgetCode;
        private String budgetName;
        private String budgetPeriod;
        private YearMonth period;
        private BigDecimal allocatedAmount;
        private String currency;
        private String category;
        private String department;
        private String costCenter;
        private String fiscalYear;
        private List<String> alertRecipients;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    public static class ExpenditureRequestDto {
        private BigDecimal amount;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    public static class CommitmentRequestDto {
        private BigDecimal amount;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    public static class AdjustmentRequestDto {
        private BigDecimal newAllocation;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    public static class ThresholdCheckRequestDto {
        private String thresholdType;
        private BigDecimal thresholdValue;
        private String level;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    public static class ThresholdAcknowledgeRequestDto {
        private String thresholdType;
    }
}
