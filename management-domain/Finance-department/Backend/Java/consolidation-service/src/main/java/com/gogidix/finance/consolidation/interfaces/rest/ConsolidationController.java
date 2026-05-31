package com.gogidix.finance.consolidation.interfaces.rest;

import com.gogidix.finance.consolidation.application.service.ConsolidationRuleService;
import com.gogidix.finance.consolidation.application.service.ConsolidationJobService;
import com.gogidix.finance.consolidation.domain.model.ConsolidationRule;
import com.gogidix.finance.consolidation.domain.model.ConsolidationJob;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consolidation")
@RequiredArgsConstructor
public class ConsolidationController {
    private final ConsolidationRuleService ruleService;
    private final ConsolidationJobService jobService;

    @PostMapping("/rules")
    public ResponseEntity<ConsolidationRule> createRule(@RequestBody ConsolidationRule rule) {
        return ResponseEntity.ok(ruleService.create(rule));
    }

    @GetMapping("/rules")
    public ResponseEntity<List<ConsolidationRule>> getAllRules(@RequestParam String tenantId) {
        return ResponseEntity.ok(ruleService.getByTenantId(tenantId));
    }

    @GetMapping("/rules/{id}")
    public ResponseEntity<ConsolidationRule> getRuleById(@PathVariable String id) {
        return ruleService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable String id) {
        ruleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/jobs")
    public ResponseEntity<ConsolidationJob> createJob(@RequestBody ConsolidationJob job) {
        return ResponseEntity.ok(jobService.create(job));
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<ConsolidationJob>> getAllJobs(@RequestParam String tenantId) {
        return ResponseEntity.ok(jobService.getByTenantId(tenantId));
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<ConsolidationJob> getJobById(@PathVariable String id) {
        return jobService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable String id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
