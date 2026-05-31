package com.gogidix.globalbusinessmanagement.datavalidation.domain.repository;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ValidationRule
 */
@Repository
public interface ValidationRuleRepository extends MongoRepository<ValidationRule, String> {

    /**
     * Find a rule by its unique code
     */
    Optional<ValidationRule> findByCode(String code);

    /**
     * Find all active rules for a specific entity type
     */
    List<ValidationRule> findByEntityTypeAndEnabledTrueOrderByPriorityDesc(String entityType);

    /**
     * Find all active rules
     */
    List<ValidationRule> findByEnabledTrueOrderByPriorityDesc();

    /**
     * Find rules by type
     */
    List<ValidationRule> findByRuleType(ValidationRule.RuleType ruleType);

    /**
     * Find rules by status
     */
    List<ValidationRule> findByStatus(ValidationRule.RuleStatus status);

    /**
     * Find rules by severity
     */
    List<ValidationRule> findBySeverity(ValidationRule.SeverityLevel severity);

    /**
     * Find rules by tags
     */
    List<ValidationRule> findByTagsContaining(String tag);

    /**
     * Search rules by name or description
     */
    @Query("{ '$or': [ " +
           "{ 'name': { '$regex': ?0, '$options': 'i' } }, " +
           "{ 'description': { '$regex': ?0, '$options': 'i' } }, " +
           "{ 'code': { '$regex': ?0, '$options': 'i' } } " +
           "] }")
    List<ValidationRule> searchByKeyword(String keyword);

    /**
     * Find rules by entity type and rule type
     */
    List<ValidationRule> findByEntityTypeAndRuleTypeAndEnabledTrue(
            String entityType, ValidationRule.RuleType ruleType);

    /**
     * Check if a rule code exists
     */
    boolean existsByCode(String code);

    /**
     * Count active rules by entity type
     */
    long countByEntityTypeAndEnabledTrue(String entityType);

    /**
     * Find rules requiring context
     */
    List<ValidationRule> findByRequiresContextTrue();
}
