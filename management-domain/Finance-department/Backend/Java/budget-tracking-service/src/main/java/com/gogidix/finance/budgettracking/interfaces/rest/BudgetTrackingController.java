package com.gogidix.finance.budgettracking.interfaces.rest;

import com.gogidix.finance.budgettracking.application.service.BudgetTrackingQueryService;
import com.gogidix.finance.budgettracking.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/budget-tracking")
@RequiredArgsConstructor
@Tag(name = "Budget Tracking", description = "Budget tracking query and summary endpoints")
public class BudgetTrackingController {

    private final BudgetTrackingQueryService queryService;

    @GetMapping("/summary")
    @Operation(summary = "Get budget tracking summary")
    public ResponseEntity<?> getSummary(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String department) {
        YearMonth yearMonth = period != null ? YearMonth.parse(period) : null;
        return ResponseEntity.ok(queryService.getSummary(yearMonth, startDate, endDate, department, category));
    }

    @GetMapping("/variances/{varianceId}")
    @Operation(summary = "Get budget variance by ID")
    public ResponseEntity<?> getVariance(
            @Parameter(description = "Variance ID") @PathVariable String varianceId) {
        return ResponseEntity.ok(queryService.getVarianceById(varianceId));
    }

    @GetMapping("/variances/budget/{budgetId}")
    @Operation(summary = "Get variances by budget ID")
    public ResponseEntity<?> getVariancesByBudget(
            @Parameter(description = "Budget ID") @PathVariable String budgetId) {
        return ResponseEntity.ok(queryService.getVariancesByBudget(budgetId));
    }

    @GetMapping("/variances/significant")
    @Operation(summary = "Get significant variances")
    public ResponseEntity<?> getSignificantVariances(
            @RequestParam(required = false) String period) {
        YearMonth yearMonth = period != null ? YearMonth.parse(period) : null;
        return ResponseEntity.ok(queryService.getSignificantVariances(yearMonth));
    }

    @GetMapping("/variances/pending-investigation")
    @Operation(summary = "Get variances pending investigation")
    public ResponseEntity<?> getPendingInvestigationVariances() {
        return ResponseEntity.ok(queryService.getPendingInvestigationVariances());
    }

    @GetMapping("/alerts/{alertId}")
    @Operation(summary = "Get threshold alert by ID")
    public ResponseEntity<?> getAlert(
            @Parameter(description = "Alert ID") @PathVariable String alertId) {
        return ResponseEntity.ok(queryService.getAlertById(alertId));
    }

    @GetMapping("/alerts/budget/{budgetId}")
    @Operation(summary = "Get alerts by budget ID")
    public ResponseEntity<?> getAlertsByBudget(
            @Parameter(description = "Budget ID") @PathVariable String budgetId) {
        return ResponseEntity.ok(queryService.getAlertsByBudget(budgetId));
    }

    @GetMapping("/alerts/triggered")
    @Operation(summary = "Get triggered alerts")
    public ResponseEntity<?> getTriggeredAlerts() {
        return ResponseEntity.ok(queryService.getTriggeredAlerts());
    }

    @GetMapping("/alerts/enabled")
    @Operation(summary = "Get enabled alerts")
    public ResponseEntity<?> getEnabledAlerts() {
        return ResponseEntity.ok(queryService.getEnabledAlerts());
    }

    @GetMapping("/health")
    @Operation(summary = "Get budget health overview")
    public ResponseEntity<?> getBudgetHealth(
            @RequestParam(required = false) String period) {
        YearMonth yearMonth = period != null ? YearMonth.parse(period) : null;
        return ResponseEntity.ok(queryService.getBudgetHealth(yearMonth));
    }

    @GetMapping("/metrics")
    @Operation(summary = "Get budget tracking metrics")
    public ResponseEntity<?> getMetrics(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String department) {
        YearMonth yearMonth = period != null ? YearMonth.parse(period) : null;
        return ResponseEntity.ok(queryService.getMetrics(yearMonth, category, department));
    }

    @GetMapping("/utilization")
    @Operation(summary = "Get budget utilization report")
    public ResponseEntity<?> getUtilizationReport(
            @RequestParam(required = false) String period) {
        YearMonth yearMonth = period != null ? YearMonth.parse(period) : null;
        return ResponseEntity.ok(queryService.getUtilizationReport(yearMonth));
    }
}
