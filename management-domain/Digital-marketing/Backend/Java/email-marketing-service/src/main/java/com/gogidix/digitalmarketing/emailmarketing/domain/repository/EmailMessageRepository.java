package com.gogidix.digitalmarketing.emailmarketing.domain.repository;

import com.gogidix.digitalmarketing.emailmarketing.domain.model.EmailMessage;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * EmailMessage Repository - Data access for Email Message entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface EmailMessageRepository extends BaseRepository<EmailMessage> {

    // Basic Queries

    /**
     * Find messages by recipient email.
     */
    List<EmailMessage> findByRecipientEmail(String recipientEmail);

    /**
     * Find messages by recipient email with pagination.
     */
    Page<EmailMessage> findByRecipientEmail(String recipientEmail, Pageable pageable);

    /**
     * Find messages by status.
     */
    List<EmailMessage> findByStatus(String status);

    /**
     * Find messages by status with pagination.
     */
    Page<EmailMessage> findByStatus(String status, Pageable pageable);

    /**
     * Find messages by campaign ID.
     */
    List<EmailMessage> findByCampaignId(String campaignId);

    /**
     * Find messages by campaign ID with pagination.
     */
    Page<EmailMessage> findByCampaignId(String campaignId, Pageable pageable);

    /**
     * Find messages by list ID.
     */
    List<EmailMessage> findByListId(String listId);

    /**
     * Find messages by template ID.
     */
    List<EmailMessage> findByTemplateId(String templateId);

    /**
     * Find messages by subscriber ID.
     */
    List<EmailMessage> findBySubscriberId(String subscriberId);

    /**
     * Find messages by subscriber ID with pagination.
     */
    Page<EmailMessage> findBySubscriberId(String subscriberId, Pageable pageable);

    /**
     * Find messages by subject.
     */
    List<EmailMessage> findBySubject(String subject);

    /**
     * Find messages by provider message ID.
     */
    List<EmailMessage> findByProviderMessageId(String providerMessageId);

    // Status Queries

    /**
     * Find pending messages (QUEUED, SENDING, DEFERRED).
     */
    @Query("{ 'status': { $in: ['QUEUED', 'SENDING', 'DEFERRED'] } }")
    List<EmailMessage> findPendingMessages();

    /**
     * Find delivered messages.
     */
    List<EmailMessage> findByStatusOrderByDeliveredAtDesc(String status);

    /**
     * Find bounced messages.
     */
    @Query("{ 'status': 'BOUNCED' }")
    List<EmailMessage> findBouncedMessages();

    /**
     * Find hard bounced messages.
     */
    @Query("{ 'status': 'BOUNCED', 'bounceType': 'HARD' }")
    List<EmailMessage> findHardBouncedMessages();

    /**
     * Find soft bounced messages.
     */
    @Query("{ 'status': 'BOUNCED', 'bounceType': 'SOFT' }")
    List<EmailMessage> findSoftBouncedMessages();

    /**
     * Find failed messages.
     */
    @Query("{ 'status': 'FAILED' }")
    List<EmailMessage> findFailedMessages();

    /**
     * Find opened messages.
     */
    @Query("{ 'status': { $in: ['OPENED', 'CLICKED'] } }")
    List<EmailMessage> findOpenedMessages();

    /**
     * Find clicked messages.
     */
    @Query("{ 'status': 'CLICKED' }")
    List<EmailMessage> findClickedMessages();

    /**
     * Find unsubscribed messages.
     */
    @Query("{ 'unsubscribedAt': { $ne: null } }")
    List<EmailMessage> findUnsubscribedMessages();

    /**
     * Find complained messages.
     */
    @Query("{ 'complainedAt': { $ne: null } }")
    List<EmailMessage> findComplainedMessages();

    // Campaign and Status

    /**
     * Find messages by campaign and status.
     */
    List<EmailMessage> findByCampaignIdAndStatus(String campaignId, String status);

    /**
     * Find delivered messages by campaign.
     */
    @Query("{ 'campaignId': ?0, 'status': { $in: ['DELIVERED', 'OPENED', 'CLICKED'] } }")
    List<EmailMessage> findDeliveredByCampaign(String campaignId);

    /**
     * Find bounced messages by campaign.
     */
    @Query("{ 'campaignId': ?0, 'status': 'BOUNCED' }")
    List<EmailMessage> findBouncedByCampaign(String campaignId);

    // List and Status

    /**
     * Find messages by list and status.
     */
    List<EmailMessage> findByListIdAndStatus(String listId, String status);

    // Date Queries

    /**
     * Find messages scheduled before date.
     */
    @Query("{ 'scheduledAt': { $lte: ?0 } }")
    List<EmailMessage> findByScheduledAtBefore(Instant date);

    /**
     * Find messages scheduled between dates.
     */
    @Query("{ 'scheduledAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailMessage> findByScheduledAtBetween(Instant start, Instant end);

    /**
     * Find messages sent between dates.
     */
    @Query("{ 'sentAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailMessage> findBySentAtBetween(Instant start, Instant end);

    /**
     * Find messages delivered between dates.
     */
    @Query("{ 'deliveredAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailMessage> findByDeliveredAtBetween(Instant start, Instant end);

    /**
     * Find messages opened between dates.
     */
    @Query("{ 'openedAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailMessage> findByOpenedAtBetween(Instant start, Instant end);

    /**
     * Find messages created between dates.
     */
    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1 } }")
    List<EmailMessage> findByCreatedAtBetween(Instant start, Instant end);

    // Priority Queries

    /**
     * Find messages by minimum priority.
     */
    @Query("{ 'priority': { $gte: ?0 } }")
    List<EmailMessage> findByPriorityGreaterThanEqual(Integer minPriority);

    /**
     * Find pending messages ordered by priority.
     */
    @Query("{ 'status': { $in: ['QUEUED', 'DEFERRED'] } }")
    List<EmailMessage> findPendingMessagesOrderByPriorityDescScheduledAtAsc();

    // Retry Queries

    /**
     * Find messages that can be retried.
     */
    @Query("{ 'status': { $in: ['FAILED', 'DEFERRED'] }, 'retryCount': { $lt: 3 } }")
    List<EmailMessage> findRetryableMessages();

    /**
     * Find messages by retry count.
     */
    @Query("{ 'retryCount': { $gte: ?0 } }")
    List<EmailMessage> findByRetryCountGreaterThanEqual(Integer minRetries);

    // Tag Queries

    /**
     * Find messages by tag.
     */
    @Query("{ 'tags': { $in: [?0] } }")
    List<EmailMessage> findByTag(String tag);

    /**
     * Find messages by multiple tags.
     */
    @Query("{ 'tags': { $in: ?0 } }")
    List<EmailMessage> findByTagsIn(List<String> tags);

    // Search Queries

    /**
     * Search messages by subject or recipient.
     */
    @Query("{ $or: [ " +
            "{ 'subject': { $regex: ?0, $options: 'i' } }, " +
            "{ 'recipientEmail': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<EmailMessage> search(String searchTerm);

    /**
     * Search messages with pagination.
     */
    @Query("{ $or: [ " +
            "{ 'subject': { $regex: ?0, $options: 'i' } }, " +
            "{ 'recipientEmail': { $regex: ?0, $options: 'i' } }, " +
            "{ 'recipientFullName': { $regex: ?0, $options: 'i' } } " +
            "] }")
    Page<EmailMessage> search(String searchTerm, Pageable pageable);

    // Count Queries

    /**
     * Count messages by status.
     */
    @CountQuery("{ 'status': ?0 }")
    long countByStatus(String status);

    /**
     * Count messages by campaign.
     */
    @CountQuery("{ 'campaignId': ?0 }")
    long countByCampaignId(String campaignId);

    /**
     * Count messages by list.
     */
    @CountQuery("{ 'listId': ?0 }")
    long countByListId(String listId);

    /**
     * Count messages by template.
     */
    @CountQuery("{ 'templateId': ?0 }")
    long countByTemplateId(String templateId);

    /**
     * Count pending messages.
     */
    @CountQuery("{ 'status': { $in: ['QUEUED', 'SENDING', 'DEFERRED'] } }")
    long countPendingMessages();

    /**
     * Count delivered messages.
     */
    @CountQuery("{ 'status': { $in: ['DELIVERED', 'OPENED', 'CLICKED'] } }")
    long countDeliveredMessages();

    /**
     * Count bounced messages.
     */
    @CountQuery("{ 'status': 'BOUNCED' }")
    long countBouncedMessages();

    /**
     * Count opened messages.
     */
    @CountQuery("{ 'status': { $in: ['OPENED', 'CLICKED'] } }")
    long countOpenedMessages();

    /**
     * Count clicked messages.
     */
    @CountQuery("{ 'status': 'CLICKED' }")
    long countClickedMessages();

    /**
     * Count hard bounces.
     */
    @CountQuery("{ 'status': 'BOUNCED', 'bounceType': 'HARD' }")
    long countHardBounces();

    /**
     * Count soft bounces.
     */
    @CountQuery("{ 'status': 'BOUNCED', 'bounceType': 'SOFT' }")
    long countSoftBounces();

    // Engagement Queries

    /**
     * Find messages with opens.
     */
    @Query("{ 'openCount': { $gt: 0 } }")
    List<EmailMessage> findWithOpens();

    /**
     * Find messages with clicks.
     */
    @Query("{ 'clickCount': { $gt: 0 } }")
    List<EmailMessage> findWithClicks();

    /**
     * Find messages ordered by open count.
     */
    List<EmailMessage> findByCampaignIdOrderByOpenCountDesc(String campaignId);

    /**
     * Find messages ordered by click count.
     */
    List<EmailMessage> findByCampaignIdOrderByClickCountDesc(String campaignId);

    // Recipient Queries

    /**
     * Find messages by recipient first name.
     */
    List<EmailMessage> findByRecipientFirstName(String firstName);

    /**
     * Find messages by recipient last name.
     */
    List<EmailMessage> findByRecipientLastName(String lastName);

    /**
     * Find messages by full name.
     */
    List<EmailMessage> findByRecipientFullName(String fullName);

    // Latest Messages

    /**
     * Find latest messages by campaign.
     */
    Page<EmailMessage> findByCampaignIdOrderByCreatedAtDesc(String campaignId, Pageable pageable);

    /**
     * Find latest messages.
     */
    Page<EmailMessage> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Bounce Reason Queries

    /**
     * Find messages by bounce reason.
     */
    @Query("{ 'bounceReason': { $regex: ?0, $options: 'i' } }")
    List<EmailMessage> findByBounceReason(String reason);

    // Device and Location

    /**
     * Find messages by device type.
     */
    @Query("{ 'deviceType': ?0 }")
    List<EmailMessage> findByDeviceType(String deviceType);

    /**
     * Find messages by location.
     */
    @Query("{ 'location': { $regex: ?0, $options: 'i' } }")
    List<EmailMessage> findByLocation(String location);

    // ESP Queries

    /**
     * Find messages by ESP.
     */
    List<EmailMessage> findByEsp(String esp);

    // Tracking Queries

    /**
     * Find messages by tracking ID.
     */
    List<EmailMessage> findByTrackingId(String trackingId);

    // Combined Queries

    /**
     * Find messages by campaign and recipient.
     */
    @Query("{ 'campaignId': ?0, 'recipientEmail': ?1 }")
    List<EmailMessage> findByCampaignIdAndRecipientEmail(String campaignId, String recipientEmail);

    /**
     * Find messages by list and status with pagination.
     */
    Page<EmailMessage> findByListIdAndStatus(String listId, String status, Pageable pageable);

    /**
     * Find messages by template and status.
     */
    List<EmailMessage> findByTemplateIdAndStatus(String templateId, String status);
}
