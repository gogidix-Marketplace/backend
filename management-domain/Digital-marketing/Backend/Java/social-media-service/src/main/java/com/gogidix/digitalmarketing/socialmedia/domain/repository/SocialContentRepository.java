package com.gogidix.digitalmarketing.socialmedia.domain.repository;

import com.gogidix.digitalmarketing.socialmedia.domain.model.SocialContent;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * SocialContent Repository - Data access for social content library
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface SocialContentRepository extends BaseRepository<SocialContent> {

    // ========== Content type queries ==========

    /**
     * Find content by content type.
     *
     * @param contentType the content type
     * @return list of content
     */
    List<SocialContent> findByContentType(String contentType);

    /**
     * Find content by type with pagination.
     *
     * @param contentType the content type
     * @param pageable pagination parameters
     * @return page of content
     */
    Page<SocialContent> findByContentType(String contentType, Pageable pageable);

    /**
     * Find content by content types.
     *
     * @param contentTypes list of content types
     * @return list of content
     */
    List<SocialContent> findByContentTypeIn(List<String> contentTypes);

    // ========== Category queries ==========

    /**
     * Find content by category.
     *
     * @param category the category
     * @return list of content
     */
    List<SocialContent> findByCategory(String category);

    /**
     * Find content by category with pagination.
     *
     * @param category the category
     * @param pageable pagination parameters
     * @return page of content
     */
    Page<SocialContent> findByCategory(String category, Pageable pageable);

    /**
     * Find content by categories.
     *
     * @param categories list of categories
     * @return list of content
     */
    List<SocialContent> findByCategoryIn(List<String> categories);

    // ========== Status queries ==========

    /**
     * Find content by status.
     *
     * @param status the status
     * @return list of content
     */
    List<SocialContent> findByStatus(String status);

    /**
     * Find active content.
     *
     * @return list of active content
     */
    @Query("{ 'status': 'ACTIVE' }")
    List<SocialContent> findActiveContent();

    /**
     * Find archived content.
     *
     * @return list of archived content
     */
    @Query("{ 'status': 'ARCHIVED' }")
    List<SocialContent> findArchivedContent();

    /**
     * Find draft content.
     *
     * @return list of draft content
     */
    @Query("{ 'status': 'DRAFT' }")
    List<SocialContent> findDraftContent();

    // ========== Template queries ==========

    /**
     * Find template content.
     *
     * @return list of templates
     */
    List<SocialContent> findByIsTemplateTrue();

    /**
     * Find non-template content.
     *
     * @return list of non-templates
     */
    List<SocialContent> findByIsTemplateFalse();

    // ========== Approval queries ==========

    /**
     * Find content by approval status.
     *
     * @param approvalStatus the approval status
     * @return list of content
     */
    List<SocialContent> findByApprovalStatus(String approvalStatus);

    /**
     * Find content pending approval.
     *
     * @return list of pending content
     */
    @Query("{ 'approvalStatus': 'PENDING_APPROVAL' }")
    List<SocialContent> findPendingApproval();

    // ========== Visibility queries ==========

    /**
     * Find content by visibility.
     *
     * @param visibility the visibility
     * @return list of content
     */
    List<SocialContent> findByVisibility(String visibility);

    /**
     * Find public content.
     *
     * @return list of public content
     */
    @Query("{ 'visibility': 'PUBLIC' }")
    List<SocialContent> findPublicContent();

    // ========== Owner/Creator queries ==========

    /**
     * Find content by owner.
     *
     * @param ownerUserId the owner user ID
     * @return list of content
     */
    List<SocialContent> findByOwnerUserId(String ownerUserId);

    /**
     * Find content by creator.
     *
     * @param createdById the creator ID
     * @return list of content
     */
    List<SocialContent> findByCreatedById(String createdById);

    // ========== Campaign queries ==========

    /**
     * Find content by campaign ID.
     *
     * @param campaignId the campaign ID
     * @return list of content
     */
    List<SocialContent> findByCampaignId(String campaignId);

    // ========== Tag queries ==========

    /**
     * Find content with tag.
     *
     * @param tag the tag
     * @return list of content
     */
    @Query("{ 'tags': { $in: [?0] } }")
    List<SocialContent> findByTag(String tag);

    /**
     * Find content with any of the tags.
     *
     * @param tags list of tags
     * @return list of content
     */
    @Query("{ 'tags': { $in: ?0 } }")
    List<SocialContent> findByTagsIn(List<String> tags);

    /**
     * Find content with all tags.
     *
     * @param tags list of tags
     * @return list of content
     */
    @Query("{ 'tags': { $all: ?0 } }")
    List<SocialContent> findByTagsAll(List<String> tags);

    // ========== Language/Locale queries ==========

    /**
     * Find content by language.
     *
     * @param language the language code
     * @return list of content
     */
    List<SocialContent> findByLanguage(String language);

    /**
     * Find content by locale.
     *
     * @param locale the locale
     * @return list of content
     */
    List<SocialContent> findByLocale(String locale);

    // ========== Usage queries ==========

    /**
     * Find most used content.
     *
     * @param pageable pagination with sort
     * @return page of content
     */
    Page<SocialContent> findAllByOrderByUsageCountDesc(Pageable pageable);

    /**
     * Find recently used content.
     *
     * @return list of recently used content
     */
    List<SocialContent> findByLastUsedAtNotNullOrderByLastUsedAtDesc();

    /**
     * Find unused content.
     *
     * @return list of unused content
     */
    @Query("{ '$or': [ " +
            "{ 'usageCount': null }, " +
            "{ 'usageCount': 0 } " +
            "] }")
    List<SocialContent> findUnusedContent();

    // ========== Featured queries ==========

    /**
     * Find featured content.
     *
     * @return list of featured content
     */
    List<SocialContent> findByFeaturedTrue();

    // ========== Performance queries ==========

    /**
     * Find high performing content.
     *
     * @param minScore minimum performance score
     * @return list of high performing content
     */
    List<SocialContent> findByPerformanceScoreGreaterThanEqual(Integer minScore);

    /**
     * Find top performing content.
     *
     * @param pageable pagination with sort
     * @return page of content
     */
    Page<SocialContent> findAllByOrderByPerformanceScoreDesc(Pageable pageable);

    // ========== Version queries ==========

    /**
     * Find content by parent content ID.
     *
     * @param parentContentId the parent content ID
     * @return list of versions
     */
    List<SocialContent> findByParentContentId(String parentContentId);

    /**
     * Find content by version.
     *
     * @param version the version number
     * @return list of content
     */
    List<SocialContent> findByVersion(Integer version);

    // ========== Expiration queries ==========

    /**
     * Find expired content.
     *
     * @param now current time
     * @return list of expired content
     */
    @Query("{ 'expiresAt': { $lt: ?0 } }")
    List<SocialContent> findExpiredContent(Instant now);

    /**
     * Find content expiring soon.
     *
     * @param threshold time threshold
     * @return list of content expiring soon
     */
    @Query("{ 'expiresAt': { $gte: ?0, $lte: ?1 } }")
    List<SocialContent> findExpiringSoon(Instant start, Instant end);

    // ========== Search queries ==========

    /**
     * Search content by name.
     *
     * @param searchTerm the search term
     * @return list of matching content
     */
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<SocialContent> searchByName(String searchTerm);

    /**
     * Search content by text.
     *
     * @param searchTerm the search term
     * @return list of matching content
     */
    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'content': { $regex: ?0, $options: 'i' } }, " +
            "{ 'plainText': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<SocialContent> search(String searchTerm);

    // ========== Count queries ==========

    /**
     * Count content by category.
     *
     * @param category the category
     * @return count of content
     */
    @CountQuery("{ 'category': ?0 }")
    long countByCategory(String category);

    /**
     * Count content by content type.
     *
     * @param contentType the content type
     * @return count of content
     */
    @CountQuery("{ 'contentType': ?0 }")
    long countByContentType(String contentType);

    /**
     * Count content by status.
     *
     * @param status the status
     * @return count of content
     */
    long countByStatus(String status);

    // ========== Unique values ==========

    /**
     * Find all unique categories.
     *
     * @return list of categories
     */
    @Query("{ 'category': { $exists: true } }")
    List<String> findDistinctCategories();

    /**
     * Find all unique content types.
     *
     * @return list of content types
     */
    @Query("{ 'contentType': { $exists: true } }")
    List<String> findDistinctContentTypes();

    /**
     * Find all unique tags.
     *
     * @return list of tags
     */
    @Query("{ 'tags': { $exists: true } }")
    List<String> findDistinctTags();

    /**
     * Find all unique languages.
     *
     * @return list of languages
     */
    @Query("{ 'language': { $exists: true } }")
    List<String> findDistinctLanguages();
}
