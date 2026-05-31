package com.gogidix.digitalmarketing.emailmarketing.domain.repository;

import com.gogidix.digitalmarketing.emailmarketing.domain.model.EmailList;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * EmailList Repository - Data access for Email List entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface EmailListRepository extends BaseRepository<EmailList> {

    // Basic Queries

    /**
     * Find lists by name.
     */
    List<EmailList> findByName(String name);

    /**
     * Find lists by name with pagination.
     */
    Page<EmailList> findByName(String name, Pageable pageable);

    /**
     * Find lists by status.
     */
    List<EmailList> findByStatus(String status);

    /**
     * Find lists by status with pagination.
     */
    Page<EmailList> findByStatus(String status, Pageable pageable);

    /**
     * Find lists by list type.
     */
    List<EmailList> findByListType(String listType);

    /**
     * Find lists by list type with pagination.
     */
    Page<EmailList> findByListType(String listType, Pageable pageable);

    /**
     * Find lists by owner ID.
     */
    List<EmailList> findByOwnerId(String ownerId);

    /**
     * Find lists by owner with pagination.
     */
    Page<EmailList> findByOwnerId(String ownerId, Pageable pageable);

    /**
     * Find lists by visibility.
     */
    List<EmailList> findByVisibility(String visibility);

    /**
     * Find public lists.
     */
    @Query("{ 'visibility': 'PUBLIC' }")
    List<EmailList> findPublicLists();

    /**
     * Find private lists.
     */
    @Query("{ 'visibility': 'PRIVATE' }")
    List<EmailList> findPrivateLists();

    // System Lists

    /**
     * Find system lists.
     */
    List<EmailList> findByIsSystemTrue();

    /**
     * Find non-system lists.
     */
    List<EmailList> findByIsSystemFalse();

    // Tag Queries

    /**
     * Find lists by tag.
     */
    @Query("{ 'tags': { $in: [?0] } }")
    List<EmailList> findByTag(String tag);

    /**
     * Find lists by multiple tags.
     */
    @Query("{ 'tags': { $in: ?0 } }")
    List<EmailList> findByTagsIn(List<String> tags);

    /**
     * Find lists with all specified tags.
     */
    @Query("{ 'tags': { $all: ?0 } }")
    List<EmailList> findByTagsAll(List<String> tags);

    // Search Queries

    /**
     * Search lists by name or description.
     */
    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'description': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<EmailList> search(String searchTerm);

    /**
     * Search lists with pagination.
     */
    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'description': { $regex: ?0, $options: 'i' } } " +
            "] }")
    Page<EmailList> search(String searchTerm, Pageable pageable);

    // Count Queries

    /**
     * Count lists by status.
     */
    @CountQuery("{ 'status': ?0 }")
    long countByStatus(String status);

    /**
     * Count lists by type.
     */
    @CountQuery("{ 'listType': ?0 }")
    long countByListType(String listType);

    /**
     * Count active lists.
     */
    @CountQuery("{ 'status': 'ACTIVE' }")
    long countActiveLists();

    /**
     * Count archived lists.
     */
    @CountQuery("{ 'status': 'ARCHIVED' }")
    long countArchivedLists();

    /**
     * Count system lists.
     */
    @CountQuery("{ 'isSystem': true }")
    long countSystemLists();

    // Subscriber Count Queries

    /**
     * Find lists by minimum subscriber count.
     */
    @Query("{ 'subscriberCount': { $gte: ?0 } }")
    List<EmailList> findByMinSubscriberCount(Integer minCount);

    /**
     * Find lists ordered by subscriber count.
     */
    List<EmailList> findByStatusOrderBySubscriberCountDesc(String status);

    /**
     * Find lists with zero subscribers.
     */
    @Query("{ $or: [ " +
            "{ 'subscriberCount': { $eq: 0 } }, " +
            "{ 'subscriberCount': { $exists: false } } " +
            "] }")
    List<EmailList> findEmptyLists();

    // Date Queries

    /**
     * Find lists created between dates.
     */
    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailList> findByCreatedAtBetween(Instant start, Instant end);

    /**
     * Find lists updated between dates.
     */
    @Query("{ 'updatedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailList> findByUpdatedAtBetween(Instant start, Instant end);

    /**
     * Find lists cleaned between dates.
     */
    @Query("{ 'lastCleanedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailList> findByLastCleanedAtBetween(Instant start, Instant end);

    /**
     * Find lists needing cleaning.
     */
    @Query("{ $or: [ " +
            "{ 'lastCleanedAt': { $exists: false } }, " +
            "{ 'lastCleanedAt': { $lt: ?0 } } " +
            "] }")
    List<EmailList> findListsNeedingCleaning(Instant threshold);

    // Related Lists

    /**
     * Find lists by parent list ID.
     */
    List<EmailList> findByParentListId(String parentListId);

    /**
     * Find root lists (no parent).
     */
    @Query("{ 'parentListId': { $exists: false } }")
    List<EmailList> findRootLists();

    // Combined Queries

    /**
     * Find lists by status and type.
     */
    List<EmailList> findByStatusAndListType(String status, String listType);

    /**
     * Find lists by status and owner.
     */
    List<EmailList> findByStatusAndOwnerId(String status, String ownerId);

    // Latest Lists

    /**
     * Find latest lists.
     */
    Page<EmailList> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find recently updated lists.
     */
    Page<EmailList> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    // Double Opt-In

    /**
     * Find lists that require double opt-in.
     */
    @Query("{ 'doubleOptIn': true }")
    List<EmailList> findDoubleOptInLists();

    /**
     * Find lists that don't require double opt-in.
     */
    @Query("{ 'doubleOptIn': false }")
    List<EmailList> findSingleOptInLists();

    // Welcome/Goodbye Templates

    /**
     * Find lists with welcome template.
     */
    @Query("{ 'welcomeTemplateId': { $ne: null } }")
    List<EmailList> findWithWelcomeTemplate();

    /**
     * Find lists by welcome template ID.
     */
    List<EmailList> findByWelcomeTemplateId(String templateId);

    /**
     * Find lists by goodbye template ID.
     */
    List<EmailList> findByGoodbyeTemplateId(String templateId);

    // Locale and Timezone

    /**
     * Find lists by locale.
     */
    List<EmailList> findByDefaultLocale(String locale);

    /**
     * Find lists by timezone.
     */
    List<EmailList> findByTimezone(String timezone);

    // Activity Tracking

    /**
     * Find lists with activity tracking enabled.
     */
    @Query("{ 'trackActivity': true }")
    List<EmailList> findWithActivityTracking();

    // Distinct Values

    /**
     * Find all distinct list types.
     */
    @Query("{ 'listType': { $exists: true } }")
    List<String> findDistinctListTypes();

    /**
     * Find all distinct statuses.
     */
    @Query("{ 'status': { $exists: true } }")
    List<String> findDistinctStatuses();

    /**
     * Find all distinct locales.
     */
    @Query("{ 'defaultLocale': { $exists: true } }")
    List<String> findDistinctLocales();

    /**
     * Find all distinct timezones.
     */
    @Query("{ 'timezone': { $exists: true } }")
    List<String> findDistinctTimezones();

    // Combined Status and Type with Pagination

    /**
     * Find lists by status and type with pagination.
     */
    Page<EmailList> findByStatusAndListType(String status, String listType, Pageable pageable);

    // Active Lists Queries

    /**
     * Find active static lists.
     */
    List<EmailList> findByStatusAndListTypeOrderBySubscriberCountDesc(String status, String listType);

    /**
     * Find active dynamic lists.
     */
    @Query("{ 'status': 'ACTIVE', 'listType': { $in: ['DYNAMIC', 'SEGMENT'] } }")
    List<EmailList> findActiveDynamicLists();
}
