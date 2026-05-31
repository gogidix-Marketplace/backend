package com.gogidix.digitalmarketing.emailmarketing.domain.repository;

import com.gogidix.digitalmarketing.emailmarketing.domain.model.EmailTemplate;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * EmailTemplate Repository - Data access for Email Template entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface EmailTemplateRepository extends BaseRepository<EmailTemplate> {

    // Basic Queries

    /**
     * Find templates by name.
     */
    List<EmailTemplate> findByName(String name);

    /**
     * Find templates by name with pagination.
     */
    Page<EmailTemplate> findByName(String name, Pageable pageable);

    /**
     * Find templates by category.
     */
    List<EmailTemplate> findByCategory(String category);

    /**
     * Find templates by category with pagination.
     */
    Page<EmailTemplate> findByCategory(String category, Pageable pageable);

    /**
     * Find templates by active status.
     */
    List<EmailTemplate> findByIsActive(Boolean isActive);

    /**
     * Find templates by active status with pagination.
     */
    Page<EmailTemplate> findByIsActive(Boolean isActive, Pageable pageable);

    /**
     * Find templates by locale.
     */
    List<EmailTemplate> findByLocale(String locale);

    /**
     * Find templates by format.
     */
    List<EmailTemplate> findByFormat(String format);

    /**
     * Find templates by template type.
     */
    List<EmailTemplate> findByTemplateType(String templateType);

    /**
     * Find system templates.
     */
    List<EmailTemplate> findByIsSystemTrue();

    /**
     * Find non-system templates.
     */
    List<EmailTemplate> findByIsSystemFalse();

    /**
     * Find default templates.
     */
    List<EmailTemplate> findByIsDefaultTrue();

    /**
     * Find responsive templates.
     */
    @Query("{ 'isResponsive': true }")
    List<EmailTemplate> findResponsiveTemplates();

    // Parent Template Queries

    /**
     * Find templates by parent template ID.
     */
    List<EmailTemplate> findByParentTemplateId(String parentTemplateId);

    /**
     * Find root templates (no parent).
     */
    @Query("{ 'parentTemplateId': { $exists: false } }")
    List<EmailTemplate> findRootTemplates();

    // Tag Queries

    /**
     * Find templates by tag.
     */
    @Query("{ 'tags': { $in: [?0] } }")
    List<EmailTemplate> findByTag(String tag);

    /**
     * Find templates by multiple tags.
     */
    @Query("{ 'tags': { $in: ?0 } }")
    List<EmailTemplate> findByTagsIn(List<String> tags);

    /**
     * Find templates with all specified tags.
     */
    @Query("{ 'tags': { $all: ?0 } }")
    List<EmailTemplate> findByTagsAll(List<String> tags);

    // Search Queries

    /**
     * Search templates by name or description.
     */
    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'description': { $regex: ?0, $options: 'i' } }, " +
            "{ 'subject': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<EmailTemplate> search(String searchTerm);

    /**
     * Search templates with pagination.
     */
    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'description': { $regex: ?0, $options: 'i' } } " +
            "] }")
    Page<EmailTemplate> search(String searchTerm, Pageable pageable);

    // Count Queries

    /**
     * Count templates by category.
     */
    @CountQuery("{ 'category': ?0 }")
    long countByCategory(String category);

    /**
     * Count active templates.
     */
    @CountQuery("{ 'isActive': true }")
    long countActiveTemplates();

    /**
     * Count system templates.
     */
    @CountQuery("{ 'isSystem': true }")
    long countSystemTemplates();

    /**
     * Count templates by format.
     */
    @CountQuery("{ 'format': ?0 }")
    long countByFormat(String format);

    /**
     * Count templates by locale.
     */
    @CountQuery("{ 'locale': ?0 }")
    long countByLocale(String locale);

    // Usage Queries

    /**
     * Find most used templates.
     */
    @Query("{ 'usageCount': { $gt: 0 } }")
    List<EmailTemplate> findMostUsedTemplates();

    /**
     * Find templates sorted by usage count.
     */
    List<EmailTemplate> findByUsageCountGreaterThanOrderByUsageCountDesc(Integer minUsage);

    /**
     * Find recently used templates.
     */
    @Query("{ 'lastUsedAt': { $ne: null } }")
    List<EmailTemplate> findRecentlyUsedTemplates();

    /**
     * Find unused templates.
     */
    @Query("{ $or: [ " +
            "{ 'usageCount': { $eq: 0 } }, " +
            "{ 'usageCount': { $exists: false } } " +
            "] }")
    List<EmailTemplate> findUnusedTemplates();

    // Date Queries

    /**
     * Find templates created between dates.
     */
    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailTemplate> findByCreatedAtBetween(Instant start, Instant end);

    /**
     * Find templates updated between dates.
     */
    @Query("{ 'updatedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailTemplate> findByUpdatedAtBetween(Instant start, Instant end);

    /**
     * Find templates used between dates.
     */
    @Query("{ 'lastUsedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailTemplate> findByLastUsedAtBetween(Instant start, Instant end);

    // Combined Queries

    /**
     * Find active templates by category.
     */
    List<EmailTemplate> findByIsActiveAndCategory(Boolean isActive, String category);

    /**
     * Find active templates by locale.
     */
    List<EmailTemplate> findByIsActiveAndLocale(Boolean isActive, String locale);

    // Latest Templates

    /**
     * Find latest templates.
     */
    Page<EmailTemplate> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find recently updated templates.
     */
    Page<EmailTemplate> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    // Distinct Values

    /**
     * Find all distinct categories.
     */
    @Query("{ 'category': { $exists: true } }")
    List<String> findDistinctCategories();

    /**
     * Find all distinct formats.
     */
    @Query("{ 'format': { $exists: true } }")
    List<String> findDistinctFormats();

    /**
     * Find all distinct locales.
     */
    @Query("{ 'locale': { $exists: true } }")
    List<String> findDistinctLocales();

    /**
     * Find all distinct template types.
     */
    @Query("{ 'templateType': { $exists: true } }")
    List<String> findDistinctTemplateTypes();

    // Template Type Queries

    /**
     * Find templates by type with pagination.
     */
    Page<EmailTemplate> findByTemplateType(String templateType, Pageable pageable);

    /**
     * Find active templates by type.
     */
    List<EmailTemplate> findByIsActiveAndTemplateType(Boolean isActive, String templateType);

    // Combined Status Queries

    /**
     * Find templates that are active and responsive.
     */
    @Query("{ 'isActive': true, 'isResponsive': true }")
    List<EmailTemplate> findActiveAndResponsiveTemplates();

    /**
     * Find templates that are active and not system templates.
     */
    @Query("{ 'isActive': true, 'isSystem': false }")
    List<EmailTemplate> findActiveNonSystemTemplates();

    /**
     * Find default templates for a category.
     */
    @Query("{ 'isDefault': true, 'category': ?0 }")
    List<EmailTemplate> findDefaultTemplatesByCategory(String category);

    // Version Queries

    /**
     * Find templates by version.
     */
    List<EmailTemplate> findByVersion(String version);

    /**
     * Find templates by parent and version.
     */
    List<EmailTemplate> findByParentTemplateIdAndVersion(String parentTemplateId, String version);
}
