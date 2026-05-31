package com.gogidix.digitalmarketing.emailmarketing.domain.repository;

import com.gogidix.digitalmarketing.emailmarketing.domain.model.EmailSubscriber;
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
 * EmailSubscriber Repository - Data access for Email Subscriber entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface EmailSubscriberRepository extends BaseRepository<EmailSubscriber> {

    // Basic Queries

    /**
     * Find subscriber by email.
     */
    Optional<EmailSubscriber> findByEmail(String email);

    /**
     * Find subscribers by list ID.
     */
    List<EmailSubscriber> findByListId(String listId);

    /**
     * Find subscribers by list ID with pagination.
     */
    Page<EmailSubscriber> findByListId(String listId, Pageable pageable);

    /**
     * Find subscribers by status.
     */
    List<EmailSubscriber> findByStatus(String status);

    /**
     * Find subscribers by status with pagination.
     */
    Page<EmailSubscriber> findByStatus(String status, Pageable pageable);

    /**
     * Find subscribers by list and status.
     */
    List<EmailSubscriber> findByListIdAndStatus(String listId, String status);

    /**
     * Find subscribers by list and status with pagination.
     */
    Page<EmailSubscriber> findByListIdAndStatus(String listId, String status, Pageable pageable);

    // Confirmation Queries

    /**
     * Find subscriber by confirmation token.
     */
    Optional<EmailSubscriber> findByConfirmationToken(String token);

    /**
     * Find unconfirmed subscribers.
     */
    @Query("{ 'status': 'UNCONFIRMED' }")
    List<EmailSubscriber> findUnconfirmedSubscribers();

    /**
     * Find unconfirmed subscribers older than threshold.
     */
    @Query("{ 'status': 'UNCONFIRMED', 'createdAt': { $lt: ?0 } }")
    List<EmailSubscriber> findUnconfirmedSubscribersOlderThan(Instant threshold);

    /**
     * Find pending confirmation subscribers for a list.
     */
    @Query("{ 'listId': ?0, 'status': 'UNCONFIRMED' }")
    List<EmailSubscriber> findPendingConfirmationByList(String listId);

    // Engagement Queries

    /**
     * Find active subscribers (recently opened).
     */
    @Query("{ 'lastOpenedAt': { $gte: ?0 } }")
    List<EmailSubscriber> findActiveSubscribers(Instant since);

    /**
     * Find inactive subscribers (no opens in threshold).
     */
    @Query("{ $or: [ " +
            "{ 'lastOpenedAt': { $lt: ?0 } }, " +
            "{ 'lastOpenedAt': { $exists: false } } " +
            "] }")
    List<EmailSubscriber> findInactiveSubscribers(Instant threshold);

    /**
     * Find subscribers by activity level.
     */
    List<EmailSubscriber> findByActivityLevel(String activityLevel);

    /**
     * Find subscribers ordered by engagement score.
     */
    List<EmailSubscriber> findByListIdOrderByEngagementScoreDesc(String listId);

    /**
     * Find highly engaged subscribers.
     */
    @Query("{ 'engagementScore': { $gte: ?0 } }")
    List<EmailSubscriber> findHighlyEngagedSubscribers(int minScore);

    // Unsubscribe/Bounce Queries

    /**
     * Find unsubscribed subscribers.
     */
    @Query("{ 'status': 'UNSUBSCRIBED' }")
    List<EmailSubscriber> findUnsubscribedSubscribers();

    /**
     * Find bounced subscribers.
     */
    @Query("{ 'status': 'BOUNCED' }")
    List<EmailSubscriber> findBouncedSubscribers();

    /**
     * Find hard bounced subscribers.
     */
    @Query("{ 'status': 'BOUNCED', 'bounceType': 'HARD' }")
    List<EmailSubscriber> findHardBouncedSubscribers();

    /**
     * Find soft bounced subscribers.
     */
    @Query("{ 'status': 'BOUNCED', 'bounceType': 'SOFT' }")
    List<EmailSubscriber> findSoftBouncedSubscribers();

    /**
     * Find complained subscribers.
     */
    @Query("{ 'status': 'COMPLAINED' }")
    List<EmailSubscriber> findComplainedSubscribers();

    // Name Queries

    /**
     * Find subscribers by first name.
     */
    List<EmailSubscriber> findByFirstName(String firstName);

    /**
     * Find subscribers by last name.
     */
    List<EmailSubscriber> findByLastName(String lastName);

    /**
     * Find subscribers by full name.
     */
    List<EmailSubscriber> findByFullName(String fullName);

    // Tag Queries

    /**
     * Find subscribers by tag.
     */
    @Query("{ 'tags': { $in: [?0] } }")
    List<EmailSubscriber> findByTag(String tag);

    /**
     * Find subscribers by multiple tags.
     */
    @Query("{ 'tags': { $in: ?0 } }")
    List<EmailSubscriber> findByTagsIn(List<String> tags);

    /**
     * Find subscribers with all specified tags.
     */
    @Query("{ 'tags': { $all: ?0 } }")
    List<EmailSubscriber> findByTagsAll(List<String> tags);

    // Search Queries

    /**
     * Search subscribers by email or name.
     */
    @Query("{ $or: [ " +
            "{ 'email': { $regex: ?0, $options: 'i' } }, " +
            "{ 'firstName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'lastName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'fullName': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<EmailSubscriber> search(String searchTerm);

    /**
     * Search subscribers with pagination.
     */
    @Query("{ $or: [ " +
            "{ 'email': { $regex: ?0, $options: 'i' } }, " +
            "{ 'firstName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'lastName': { $regex: ?0, $options: 'i' } } " +
            "] }")
    Page<EmailSubscriber> search(String searchTerm, Pageable pageable);

    // Count Queries

    /**
     * Count subscribers by list.
     */
    @CountQuery("{ 'listId': ?0 }")
    long countByListId(String listId);

    /**
     * Count subscribers by list and status.
     */
    @CountQuery("{ 'listId': ?0, 'status': ?1 }")
    long countByListIdAndStatus(String listId, String status);

    /**
     * Count active subscribers by list.
     */
    @CountQuery("{ 'listId': ?0, 'status': 'ACTIVE' }")
    long countActiveByListId(String listId);

    /**
     * Count unsubscribed by list.
     */
    @CountQuery("{ 'listId': ?0, 'status': 'UNSUBSCRIBED' }")
    long countUnsubscribedByListId(String listId);

    /**
     * Count bounced by list.
     */
    @CountQuery("{ 'listId': ?0, 'status': 'BOUNCED' }")
    long countBouncedByListId(String listId);

    /**
     * Count confirmed by list.
     */
    @CountQuery("{ 'listId': ?0, 'confirmedAt': { $ne: null } }")
    long countConfirmedByListId(String listId);

    /**
     * Count unconfirmed by list.
     */
    @CountQuery("{ 'listId': ?0, 'status': 'UNCONFIRMED' }")
    long countUnconfirmedByListId(String listId);

    // Date Queries

    /**
     * Find subscribers created between dates.
     */
    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailSubscriber> findByCreatedAtBetween(Instant start, Instant end);

    /**
     * Find subscribers confirmed between dates.
     */
    @Query("{ 'confirmedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailSubscriber> findByConfirmedAtBetween(Instant start, Instant end);

    /**
     * Find subscribers unsubscribed between dates.
     */
    @Query("{ 'unsubscribedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailSubscriber> findByUnsubscribedAtBetween(Instant start, Instant end);

    /**
     * Find subscribers bounced between dates.
     */
    @Query("{ 'bouncedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailSubscriber> findByBouncedAtBetween(Instant start, Instant end);

    /**
     * Find recently subscribed (confirmed).
     */
    Page<EmailSubscriber> findByStatusOrderByConfirmedAtDesc(String status, Pageable pageable);

    // Location Queries

    /**
     * Find subscribers by country.
     */
    List<EmailSubscriber> findByCountry(String country);

    /**
     * Find subscribers by region.
     */
    List<EmailSubscriber> findByRegion(String region);

    /**
     * Find subscribers by city.
     */
    List<EmailSubscriber> findByCity(String city);

    // Language/Timezone Queries

    /**
     * Find subscribers by language.
     */
    List<EmailSubscriber> findByLanguage(String language);

    /**
     * Find subscribers by timezone.
     */
    List<EmailSubscriber> findByTimezone(String timezone);

    // Signup Source Queries

    /**
     * Find subscribers by signup source.
     */
    List<EmailSubscriber> findBySignupSource(String signupSource);

    /**
     * Find API signups.
     */
    @Query("{ 'signupSource': 'API' }")
    List<EmailSubscriber> findApiSignups();

    /**
     * Find imported subscribers.
     */
    @Query("{ 'signupSource': 'IMPORT' }")
    List<EmailSubscriber> findImportedSubscribers();

    // Email Statistics Queries

    /**
     * Find subscribers who received no emails.
     */
    @Query("{ $or: [ " +
            "{ 'emailsSent': { $eq: 0 } }, " +
            "{ 'emailsSent': { $exists: false } } " +
            "] }")
    List<EmailSubscriber> findNeverEmailed();

    /**
     * Find subscribers who never opened.
     */
    @Query("{ 'emailsSent': { $gt: 0 }, 'emailsOpened': { $eq: 0 } }")
    List<EmailSubscriber> findNeverOpened();

    /**
     * Find subscribers by minimum open count.
     */
    @Query("{ 'emailsOpened': { $gte: ?0 } }")
    List<EmailSubscriber> findByMinOpens(int minOpens);

    /**
     * Find subscribers by minimum click count.
     */
    @Query("{ 'emailsClicked': { $gte: ?0 } }")
    List<EmailSubscriber> findByMinClicks(int minClicks);

    // Combined Queries

    /**
     * Find active subscribers in list ordered by engagement.
     */
    List<EmailSubscriber> findByListIdAndStatusOrderByEngagementScoreDesc(String listId, String status);

    // Custom Field Queries

    /**
     * Find subscribers by custom field value.
     */
    @Query("{ 'customFields.?0': ?1 }")
    List<EmailSubscriber> findByCustomField(String fieldName, Object value);

    // Latest Subscribers

    /**
     * Find latest subscribers by list.
     */
    Page<EmailSubscriber> findByListIdOrderByCreatedAtDesc(String listId, Pageable pageable);

    /**
     * Find latest subscribers.
     */
    Page<EmailSubscriber> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Email Format Queries

    /**
     * Find subscribers by email format.
     */
    List<EmailSubscriber> findByEmailFormat(String emailFormat);

    /**
     * Find HTML accepting subscribers.
     */
    @Query("{ 'acceptsHtml': true }")
    List<EmailSubscriber> findHtmlAcceptingSubscribers();

    /**
     * Find text only subscribers.
     */
    @Query("{ 'acceptsHtml': false }")
    List<EmailSubscriber> findTextOnlySubscribers();

    // Bounce Reason Queries

    /**
     * Find subscribers by bounce reason.
     */
    @Query("{ 'bounceReason': { $regex: ?0, $options: 'i' } }")
    List<EmailSubscriber> findByBounceReason(String reason);

    // Existence Checks

    /**
     * Check if subscriber exists by email.
     */
    @Query(value = "{ 'email': ?0 }", exists = true)
    boolean existsByEmail(String email);

    /**
     * Check if subscriber exists by email in list.
     */
    @Query(value = "{ 'email': ?0, 'listId': ?1 }", exists = true)
    boolean existsByEmailAndListId(String email, String listId);

    // Batch Queries

    /**
     * Find subscribers by IDs.
     */
    @Query("{ '_id': { $in: ?0 } }")
    List<EmailSubscriber> findByIdIn(List<String> ids);

    // Preferential Queries

    /**
     * Find subscribers with newsletter preference enabled.
     */
    @Query("{ 'preferences.receiveNewsletters': true }")
    List<EmailSubscriber> findNewsletterSubscribers();

    /**
     * Find subscribers with promotional preference enabled.
     */
    @Query("{ 'preferences.receivePromotional': true }")
    List<EmailSubscriber> findPromotionalSubscribers();

    /**
     * Find subscribers with transactional preference enabled.
     */
    @Query("{ 'preferences.receiveTransactional': true }")
    List<EmailSubscriber> findTransactionalSubscribers();

    // Unsubscribe Method Queries

    /**
     * Find subscribers by unsubscribe method.
     */
    @Query("{ 'unsubscribeMethod': ?0 }")
    List<EmailSubscriber> findByUnsubscribeMethod(String method);

    // Cleanup Queries

    /**
     * Find subscribers to clean (unconfirmed for long time).
     */
    @Query("{ 'status': 'UNCONFIRMED', 'createdAt': { $lt: ?0 } }")
    List<EmailSubscriber> findStaleUnconfirmedSubscribers(Instant threshold);
}
