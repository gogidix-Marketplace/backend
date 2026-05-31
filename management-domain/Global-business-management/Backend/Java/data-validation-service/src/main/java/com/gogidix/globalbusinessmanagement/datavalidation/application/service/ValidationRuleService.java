package com.gogidix.globalbusinessmanagement.datavalidation.application.service;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationRuleDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.repository.ValidationRuleRepository;
import com.gogidix.globalbusinessmanagement.datavalidation.infrastructure.config.ValidationConfig;
import com.gogidix.globalbusinessmanagement.datavalidation.infrastructure.mapper.ValidationRuleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing validation rules
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationRuleService {

    private final ValidationRuleRepository repository;
    private final ValidationRuleMapper mapper;
    private final ValidationConfig config;

    /**
     * Create a new validation rule
     */
    @Transactional
    public ValidationRuleDTO createRule(ValidationRuleDTO dto, String createdBy) {
        log.info("Creating validation rule: {}", dto.getCode());

        if (repository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Rule with code " + dto.getCode() + " already exists");
        }

        ValidationRule entity = mapper.toEntity(dto);
        entity.setCreatedBy(createdBy);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setLastModifiedBy(createdBy);
        entity.setLastModifiedAt(LocalDateTime.now());

        ValidationRule saved = repository.save(entity);
        log.info("Created validation rule with ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    /**
     * Update an existing validation rule
     */
    @Transactional
    @CacheEvict(value = "validationRules", key = "#id")
    public ValidationRuleDTO updateRule(String id, ValidationRuleDTO dto, String modifiedBy) {
        log.info("Updating validation rule: {}", id);

        ValidationRule entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rule not found with ID: " + id));

        mapper.updateEntityFromDto(dto, entity);
        entity.setLastModifiedBy(modifiedBy);
        entity.setLastModifiedAt(LocalDateTime.now());

        ValidationRule saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    /**
     * Get a rule by ID
     */
    @Cacheable(value = "validationRules", key = "#id")
    public ValidationRuleDTO getRuleById(String id) {
        log.info("Fetching validation rule: {}", id);
        ValidationRule entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rule not found with ID: " + id));
        return mapper.toDto(entity);
    }

    /**
     * Get a rule by code
     */
    @Cacheable(value = "validationRulesByCode", key = "#code")
    public ValidationRuleDTO getRuleByCode(String code) {
        log.info("Fetching validation rule by code: {}", code);
        ValidationRule entity = repository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Rule not found with code: " + code));
        return mapper.toDto(entity);
    }

    /**
     * Get all active rules for an entity type
     */
    public List<ValidationRuleDTO> getActiveRulesByEntityType(String entityType) {
        log.info("Fetching active rules for entity type: {}", entityType);
        return mapper.toDtoList(repository.findByEntityTypeAndEnabledTrueOrderByPriorityDesc(entityType));
    }

    /**
     * Get all active rules
     */
    public List<ValidationRuleDTO> getAllActiveRules() {
        log.info("Fetching all active rules");
        return mapper.toDtoList(repository.findByEnabledTrueOrderByPriorityDesc());
    }

    /**
     * Get all rules
     */
    public Page<ValidationRuleDTO> getAllRules(Pageable pageable) {
        log.info("Fetching all rules with pagination");
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    /**
     * Search rules by keyword
     */
    public List<ValidationRuleDTO> searchRules(String keyword) {
        log.info("Searching rules with keyword: {}", keyword);
        return mapper.toDtoList(repository.searchByKeyword(keyword));
    }

    /**
     * Get rules by type
     */
    public List<ValidationRuleDTO> getRulesByType(ValidationRule.RuleType ruleType) {
        log.info("Fetching rules by type: {}", ruleType);
        return mapper.toDtoList(repository.findByRuleType(ruleType));
    }

    /**
     * Get rules by status
     */
    public List<ValidationRuleDTO> getRulesByStatus(ValidationRule.RuleStatus status) {
        log.info("Fetching rules by status: {}", status);
        return mapper.toDtoList(repository.findByStatus(status));
    }

    /**
     * Get rules by severity
     */
    public List<ValidationRuleDTO> getRulesBySeverity(ValidationRule.SeverityLevel severity) {
        log.info("Fetching rules by severity: {}", severity);
        return mapper.toDtoList(repository.findBySeverity(severity));
    }

    /**
     * Get rules by tag
     */
    public List<ValidationRuleDTO> getRulesByTag(String tag) {
        log.info("Fetching rules by tag: {}", tag);
        return mapper.toDtoList(repository.findByTagsContaining(tag));
    }

    /**
     * Enable or disable a rule
     */
    @Transactional
    @CacheEvict(value = {"validationRules", "validationRulesByCode"}, key = "#id")
    public ValidationRuleDTO toggleRule(String id, boolean enabled, String modifiedBy) {
        log.info("{} rule: {}", enabled ? "Enabling" : "Disabling", id);

        ValidationRule entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rule not found with ID: " + id));

        entity.setEnabled(enabled);
        entity.setLastModifiedBy(modifiedBy);
        entity.setLastModifiedAt(LocalDateTime.now());

        ValidationRule saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    /**
     * Create a new version of a rule
     */
    @Transactional
    public ValidationRuleDTO createNewVersion(String id, String modifiedBy) {
        log.info("Creating new version of rule: {}", id);

        ValidationRule existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rule not found with ID: " + id));

        // Archive existing rule
        existing.setStatus(ValidationRule.RuleStatus.DEPRECATED);
        existing.setLastModifiedBy(modifiedBy);
        existing.setLastModifiedAt(LocalDateTime.now());
        repository.save(existing);

        // Create new version
        ValidationRule newVersion = existing.createNewVersion(modifiedBy);
        ValidationRule saved = repository.save(newVersion);

        log.info("Created new version {} of rule: {}", saved.getVersion(), existing.getCode());
        return mapper.toDto(saved);
    }

    /**
     * Delete a rule
     */
    @Transactional
    @CacheEvict(value = {"validationRules", "validationRulesByCode"}, key = "#id")
    public void deleteRule(String id) {
        log.info("Deleting validation rule: {}", id);

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Rule not found with ID: " + id);
        }

        repository.deleteById(id);
        log.info("Deleted validation rule: {}", id);
    }

    /**
     * Get rule statistics
     */
    public RuleStatistics getStatistics() {
        log.info("Fetching rule statistics");

        List<ValidationRule> allRules = repository.findAll();
        long activeCount = allRules.stream().filter(ValidationRule::isActive).count();
        long disabledCount = allRules.stream().filter(r -> !r.isEnabled()).count();

        return new RuleStatistics(
            allRules.size(),
            (int) activeCount,
            (int) disabledCount,
            repository.findByRequiresContextTrue().size()
        );
    }

    /**
     * Record class for rule statistics
     */
    public record RuleStatistics(
        int totalRules,
        int activeRules,
        int disabledRules,
        int rulesWithContext
    ) {}
}
