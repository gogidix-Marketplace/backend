package com.gogidix.finance.compliance.interfaces.rest;

import com.gogidix.finance.compliance.application.service.ComplianceQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Compliance Query REST Controller
 * Handles HTTP requests for compliance summary and reporting
 */
@RestController
@RequestMapping("/compliance")
@RequiredArgsConstructor
@Tag(name = "Compliance Queries", description = "Compliance summary and reporting endpoints")
public class ComplianceQueryController {

    private final ComplianceQueryService queryService;

    @GetMapping("/summary")
    @Operation(summary = "Get compliance summary")
    public ResponseEntity<ComplianceQueryService.ComplianceSummary> getSummary(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String costCenter) {

        ComplianceQueryService.ComplianceSummary summary =
            queryService.getSummary(startDate, endDate, department, costCenter);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/stats/rules/status")
    @Operation(summary = "Count rules by status")
    public ResponseEntity<Long> countRulesByStatus(@RequestParam com.gogidix.finance.compliance.domain.model.ComplianceRule.RuleStatus status) {
        return ResponseEntity.ok(queryService.countByStatus(status));
    }

    @GetMapping("/stats/checks/result")
    @Operation(summary = "Count checks by result")
    public ResponseEntity<Long> countChecksByResult(@RequestParam com.gogidix.finance.compliance.domain.model.ComplianceCheck.CheckResult result) {
        return ResponseEntity.ok(queryService.countByResult(result));
    }
}
