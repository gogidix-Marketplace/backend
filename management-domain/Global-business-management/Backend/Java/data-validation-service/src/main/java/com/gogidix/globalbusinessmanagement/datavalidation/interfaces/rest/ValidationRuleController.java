package com.gogidix.globalbusinessmanagement.datavalidation.interfaces.rest;

import com.gogidix.globalbusinessmanagement.datavalidation.application.service.ValidationRuleService;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationRuleDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for managing validation rules
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/validation-rules")
@RequiredArgsConstructor
public class ValidationRuleController {

    private final ValidationRuleService ruleService;

    /**
     * Create a new validation rule
     */
    @PostMapping
    public ResponseEntity<ValidationRuleDTO> createRule(@Valid @RequestBody ValidationRuleDTO dto,
                                                        @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        log.info("POST /api/v1/validation-rules - Creating rule: {}", dto.getCode());
        ValidationRuleDTO created = ruleService.createRule(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update a validation rule
     */
    @PutMapping("/{id}")
    public ResponseEntity<ValidationRuleDTO> updateRule(@PathVariable String id,
                                                        @Valid @RequestBody ValidationRuleDTO dto,
                                                        @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        log.info("PUT /api/v1/validation-rules/{} - Updating rule", id);
        ValidationRuleDTO updated = ruleService.updateRule(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Get a rule by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ValidationRuleDTO> getRule(@PathVariable String id) {
        log.info("GET /api/v1/validation-rules/{}", id);
        ValidationRuleDTO rule = ruleService.getRuleById(id);
        return ResponseEntity.ok(rule);
    }

    /**
     * Get a rule by code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ValidationRuleDTO> getRuleByCode(@PathVariable String code) {
        log.info("GET /api/v1/validation-rules/code/{}", code);
        ValidationRuleDTO rule = ruleService.getRuleByCode(code);
        return ResponseEntity.ok(rule);
    }

    /**
     * Get all active rules for an entity type
     */
    @GetMapping("/entity-type/{entityType}")
    public ResponseEntity<List<ValidationRuleDTO>> getActiveRulesByEntityType(@PathVariable String entityType) {
        log.info("GET /api/v1/validation-rules/entity-type/{}", entityType);
        List<ValidationRuleDTO> rules = ruleService.getActiveRulesByEntityType(entityType);
        return ResponseEntity.ok(rules);
    }

    /**
     * Get all active rules
     */
    @GetMapping("/active")
    public ResponseEntity<List<ValidationRuleDTO>> getAllActiveRules() {
        log.info("GET /api/v1/validation-rules/active");
        List<ValidationRuleDTO> rules = ruleService.getAllActiveRules();
        return ResponseEntity.ok(rules);
    }

    /**
     * Get all rules with pagination
     */
    @GetMapping
    public ResponseEntity<Page<ValidationRuleDTO>> getAllRules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /api/v1/validation-rules - page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ValidationRuleDTO> rules = ruleService.getAllRules(pageable);
        return ResponseEntity.ok(rules);
    }

    /**
     * Search rules by keyword
     */
    @GetMapping("/search")
    public ResponseEntity<List<ValidationRuleDTO>> searchRules(@RequestParam String keyword) {
        log.info("GET /api/v1/validation-rules/search?keyword={}", keyword);
        List<ValidationRuleDTO> rules = ruleService.searchRules(keyword);
        return ResponseEntity.ok(rules);
    }

    /**
     * Get rules by type
     */
    @GetMapping("/type/{ruleType}")
    public ResponseEntity<List<ValidationRuleDTO>> getRulesByType(@PathVariable ValidationRule.RuleType ruleType) {
        log.info("GET /api/v1/validation-rules/type/{}", ruleType);
        List<ValidationRuleDTO> rules = ruleService.getRulesByType(ruleType);
        return ResponseEntity.ok(rules);
    }

    /**
     * Get rules by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ValidationRuleDTO>> getRulesByStatus(@PathVariable ValidationRule.RuleStatus status) {
        log.info("GET /api/v1/validation-rules/status/{}", status);
        List<ValidationRuleDTO> rules = ruleService.getRulesByStatus(status);
        return ResponseEntity.ok(rules);
    }

    /**
     * Get rules by severity
     */
    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<ValidationRuleDTO>> getRulesBySeverity(@PathVariable ValidationRule.SeverityLevel severity) {
        log.info("GET /api/v1/validation-rules/severity/{}", severity);
        List<ValidationRuleDTO> rules = ruleService.getRulesBySeverity(severity);
        return ResponseEntity.ok(rules);
    }

    /**
     * Get rules by tag
     */
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<ValidationRuleDTO>> getRulesByTag(@PathVariable String tag) {
        log.info("GET /api/v1/validation-rules/tag/{}", tag);
        List<ValidationRuleDTO> rules = ruleService.getRulesByTag(tag);
        return ResponseEntity.ok(rules);
    }

    /**
     * Enable or disable a rule
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ValidationRuleDTO> toggleRule(@PathVariable String id,
                                                        @RequestParam boolean enabled,
                                                        @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        log.info("PATCH /api/v1/validation-rules/{}/toggle?enabled={}", id, enabled);
        ValidationRuleDTO rule = ruleService.toggleRule(id, enabled, userId);
        return ResponseEntity.ok(rule);
    }

    /**
     * Create a new version of a rule
     */
    @PostMapping("/{id}/new-version")
    public ResponseEntity<ValidationRuleDTO> createNewVersion(@PathVariable String id,
                                                             @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        log.info("POST /api/v1/validation-rules/{}/new-version", id);
        ValidationRuleDTO newVersion = ruleService.createNewVersion(id, userId);
        return ResponseEntity.ok(newVersion);
    }

    /**
     * Delete a rule
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable String id) {
        log.info("DELETE /api/v1/validation-rules/{}", id);
        ruleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get rule statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ValidationRuleService.RuleStatistics> getStatistics() {
        log.info("GET /api/v1/validation-rules/statistics");
        ValidationRuleService.RuleStatistics stats = ruleService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}
