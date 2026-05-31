package com.gogidix.customersupport.qualitymanagement.domain.repository;

import com.gogidix.customersupport.qualitymanagement.domain.model.QaReview;
import com.gogidix.customersupport.qualitymanagement.domain.model.ScorecardTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ScorecardTemplate entity
 */
@Repository
public interface ScorecardTemplateRepository extends MongoRepository<ScorecardTemplate, String> {

    /**
     * Find template by template ID
     */
    Optional<ScorecardTemplate> findByTemplateId(String templateId);

    /**
     * Find templates by tenant ID
     */
    List<ScorecardTemplate> findByTenantIdOrderByCreatedAtDesc(String tenantId);

    /**
     * Find active templates
     */
    List<ScorecardTemplate> findByTenantIdAndIsActiveTrueOrderByCreatedAtDesc(String tenantId);

    /**
     * Find default template
     */
    Optional<ScorecardTemplate> findByTenantIdAndIsDefaultTrue(String tenantId);

    /**
     * Find templates by type
     */
    List<ScorecardTemplate> findByTenantIdAndTemplateTypeOrderByCreatedAtDesc(
            String tenantId, ScorecardTemplate.TemplateType templateType);

    /**
     * Find templates by channel type
     */
    List<ScorecardTemplate> findByTenantIdAndChannelTypeOrderByCreatedAtDesc(
            String tenantId, QaReview.ChannelType channelType);

    /**
     * Find templates by status
     */
    List<ScorecardTemplate> findByTenantIdAndTemplateStatusOrderByCreatedAtDesc(
            String tenantId, ScorecardTemplate.TemplateStatus templateStatus);

    /**
     * Find valid templates for date
     */
    @Query("{ 'tenantId': ?0, 'isActive': true, 'effectiveFrom': { $lte: ?1 }, $or: [ " +
           "{ 'effectiveUntil': null }, { 'effectiveUntil': { $gte: ?1 } } ] }")
    List<ScorecardTemplate> findValidTemplatesForDate(String tenantId, Instant date);

    /**
     * Find templates by category
     */
    List<ScorecardTemplate> findByTenantIdAndCategoryOrderByCreatedAtDesc(String tenantId, String category);

    /**
     * Find templates by creator
     */
    List<ScorecardTemplate> findByTenantIdAndCreatedByOrderByCreatedAtDesc(String tenantId, String createdBy);

    /**
     * Find templates by tags
     */
    @Query("{ 'tenantId': ?0, 'tags': { $in: ?1 } }")
    List<ScorecardTemplate> findByTenantIdAndTagsContaining(String tenantId, List<String> tags);

    /**
     * Search templates by name
     */
    @Query("{ 'tenantId': ?0, 'templateName': { $regex: ?1, $options: 'i' } }")
    List<ScorecardTemplate> searchByTemplateName(String tenantId, String namePattern);

    /**
     * Find templates by version
     */
    List<ScorecardTemplate> findByTenantIdAndTemplateCodeAndVersionOrderByCreatedAtDesc(
            String tenantId, String templateCode, String version);

    /**
     * Find all versions of a template
     */
    List<ScorecardTemplate> findByTenantIdAndTemplateCodeOrderByVersionDesc(String tenantId, String templateCode);

    /**
     * Find templates linked to calibration session
     */
    @Query("{ 'tenantId': ?0, 'linkedCalibrationSessions': { $in: ?1 } }")
    List<ScorecardTemplate> findByLinkedCalibrationSession(String tenantId, String sessionId);

    /**
     * Count active templates
     */
    long countByTenantIdAndIsActiveTrue(String tenantId);

    /**
     * Delete templates older than specified date
     */
    void deleteByTenantIdAndUpdatedAtBefore(String tenantId, Instant cutoffDate);

    /**
     * Find most used templates
     */
    @Query("{ 'tenantId': ?0 }")
    List<ScorecardTemplate> findByTenantIdOrderByUsageCountDesc(String tenantId);

    /**
     * Find templates with critical failure enabled
     */
    List<ScorecardTemplate> findByTenantIdAndCriticalFailureEnabledTrueOrderByCreatedAtDesc(String tenantId);
}
