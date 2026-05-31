package com.gogidix.shared.infrastructure.services.security.management.infrastructure.sharedinfrastructure.securitymanagementservice.repository;

import com.gogidix.shared.infrastructure.services.security.management.infrastructure.sharedinfrastructure.securitymanagementservice.entity.ThreatDetectionRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThreatDetectionRuleMongoRepository extends MongoRepository<ThreatDetectionRule, String> {

    // Tenant-aware queries
    List<ThreatDetectionRule> findByTenantId(String tenantId);

    // Basic queries
    ThreatDetectionRule findByRuleNameAndTenantId(String ruleName, String tenantId);
    List<ThreatDetectionRule> findByThreatTypeAndTenantId(String threatType, String tenantId);
    List<ThreatDetectionRule> byThreatCategoryAndTenantId(String threatCategory, String tenantId);
    List<ThreatDetectionRule> findBySeverityLevelAndTenantId(String severityLevel, String tenantId);
    List<ThreatDetectionRule> findByRuleStatusAndTenantId(String ruleStatus, String tenantId);
    List<ThreatDetectionRule> findByIsActiveTrueAndTenantId(String tenantId);

    // Complex queries
    List<ThreatDetectionRule> findByIsActiveTrueAndRuleStatusAndTenantId(String ruleStatus, String tenantId);
    List<ThreatDetectionRule> findByThreatTypeAndSeverityLevelAndTenantId(String threatType, String severityLevel, String tenantId);

    // Count queries
    Long countByTenantId(String tenantId);
    Long countByThreatTypeAndTenantId(String threatType, String tenantId);
    Long countByThreatCategoryAndTenantId(String threatCategory, String tenantId);
    Long countBySeverityLevelAndTenantId(String severityLevel, String tenantId);
    Long countByRuleStatusAndTenantId(String ruleStatus, String tenantId);
    Long countByIsActiveTrueAndTenantId(String tenantId);

    // Date-based queries
    @Query("{ 'lastDetectionDate': { $ne: null }, 'tenantId': ?0, 'deleted': false }")
    List<ThreatDetectionRule> findRulesWithDetections(String tenantId);

    @Query("{ 'createdAt': { $gte: ?0 }, 'tenantId': ?1, 'deleted': false }")
    List<ThreatDetectionRule> findRulesCreatedAfter(LocalDateTime startDate, String tenantId);

    @Query("{ 'updatedAt': { $gte: ?0 }, 'tenantId': ?1, 'deleted': false }")
    List<ThreatDetectionRule> findRulesUpdatedAfter(LocalDateTime updateDate, String tenantId);

    // Performance-based queries
    @Query("{ 'accuracyRate': { $gte: ?0 }, 'tenantId': ?1, 'deleted': false }")
    List<ThreatDetectionRule> findHighAccuracyRules(Double minAccuracy, String tenantId);

    @Query("{ 'detectionCount': { $gt: ?0 }, 'tenantId': ?1, 'deleted': false }")
    List<ThreatDetectionRule> findFrequentlyTriggeredRules(Long minDetections, String tenantId);

    // Auto-response enabled rules
    @Query("{ 'autoResponseEnabled': true, 'isActive': true, 'tenantId': ?0, 'deleted': false }")
    List<ThreatDetectionRule> findAutoResponseEnabledRules(String tenantId);

    // Critical severity rules
    @Query("{ 'severityLevel': 'CRITICAL', 'isActive': true, 'tenantId': ?0, 'deleted': false }")
    List<ThreatDetectionRule> findCriticalRules(String tenantId);

    // High severity rules
    @Query("{ 'severityLevel': { $in: ['HIGH', 'CRITICAL'] }, 'isActive': true, 'tenantId': ?0, 'deleted': false }")
    List<ThreatDetectionRule> findHighSeverityRules(String tenantId);

    // Active rules by data source
    @Query("{ 'dataSource': ?0, 'isActive': true, 'tenantId': ?1, 'deleted': false }")
    List<ThreatDetectionRule> findActiveRulesByDataSource(String dataSource, String tenantId);

    // Rules needing attention (low accuracy or many false positives)
    @Query("{ '$or': [ { 'accuracyRate': { $lt: ?0 } }, { 'falsePositiveCount': { $gt: ?1 } } ], 'tenantId': ?2, 'deleted': false }")
    List<ThreatDetectionRule> findRulesNeedingAttention(Double maxAccuracy, Long maxFalsePositives, String tenantId);

    // Additional query methods
    List<ThreatDetectionRule> findByIsActiveTrueAndRuleStatus(ThreatDetectionRule.RuleStatus ruleStatus);
    List<ThreatDetectionRule> findByThreatTypeAndIsActiveTrue(ThreatDetectionRule.ThreatType threatType);
    List<ThreatDetectionRule> findBySeverityLevelAndIsActiveTrue(ThreatDetectionRule.SeverityLevel severityLevel);
    Long countByIsActiveTrueAndRuleStatus(ThreatDetectionRule.RuleStatus ruleStatus);
    Long countBySeverityLevelAndIsActiveTrue(ThreatDetectionRule.SeverityLevel severityLevel);
    List<ThreatDetectionRule> findByRuleName(String ruleName);
}
