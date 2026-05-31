package com.gogidix.marketing.campaign.domain.repository;

import com.gogidix.marketing.campaign.domain.model.CampaignChannel;
import com.gogidix.marketing.campaign.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * CampaignChannel Repository - Data access for campaign channels
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 */
@Repository
public interface CampaignChannelRepository extends BaseRepository<CampaignChannel> {

    /**
     * Find channels by campaign ID.
     *
     * @param campaignId the campaign ID
     * @return list of channels
     */
    List<CampaignChannel> findByCampaignId(String campaignId);

    /**
     * Find channels by campaign ID with pagination.
     *
     * @param campaignId the campaign ID
     * @param pageable   pagination parameters
     * @return page of channels
     */
    Page<CampaignChannel> findByCampaignId(String campaignId, Pageable pageable);

    /**
     * Find channels by type.
     *
     * @param type the channel type
     * @return list of channels
     */
    List<CampaignChannel> findByType(String type);

    /**
     * Find channels by status.
     *
     * @param status the channel status
     * @return list of channels
     */
    List<CampaignChannel> findByStatus(String status);

    /**
     * Find channels by campaign and type.
     *
     * @param campaignId the campaign ID
     * @param type       the channel type
     * @return list of channels
     */
    List<CampaignChannel> findByCampaignIdAndType(String campaignId, String type);

    /**
     * Find channels by campaign and status.
     *
     * @param campaignId the campaign ID
     * @param status     the channel status
     * @return list of channels
     */
    List<CampaignChannel> findByCampaignIdAndStatus(String campaignId, String status);

    /**
     * Find channels scheduled after date.
     *
     * @param date the date threshold
     * @return list of channels
     */
    List<CampaignChannel> findByScheduledAtAfter(Instant date);

    /**
     * Count channels by campaign.
     *
     * @param campaignId the campaign ID
     * @return count of channels
     */
    @CountQuery("{ 'campaignId': ?0 }")
    long countByCampaignId(String campaignId);

    /**
     * Count channels by status.
     *
     * @param status the channel status
     * @return count of channels
     */
    @CountQuery("{ 'status': ?0 }")
    long countByStatus(String status);

    /**
     * Find channels needing execution.
     *
     * @param now the current time
     * @return list of channels ready to execute
     */
    @Query("{ 'status': 'SCHEDULED', 'scheduledAt': { $lte: ?0 } }")
    List<CampaignChannel> findScheduledChannelsReadyForExecution(Instant now);

    /**
     * Find failed channels.
     *
     * @return list of failed channels
     */
    @Query("{ 'status': 'FAILED' }")
    List<CampaignChannel> findFailedChannels();

    /**
     * Find channels by platform.
     *
     * @param platform the platform name
     * @return list of channels
     */
    @Query("{ 'config.platform': ?0 }")
    List<CampaignChannel> findByPlatform(String platform);

    /**
     * Find channels by type for multiple campaigns.
     *
     * @param campaignIds list of campaign IDs
     * @param type        the channel type
     * @return list of channels
     */
    @Query("{ 'campaignId': { $in: ?0 }, 'type': ?1 }")
    List<CampaignChannel> findByCampaignIdInAndType(List<String> campaignIds, String type);

    /**
     * Delete channels by campaign ID.
     *
     * @param campaignId the campaign ID
     */
    void deleteByCampaignId(String campaignId);
}
