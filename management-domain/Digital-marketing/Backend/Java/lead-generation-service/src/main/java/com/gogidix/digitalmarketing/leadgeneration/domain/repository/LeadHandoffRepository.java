package com.gogidix.digitalmarketing.leadgeneration.domain.repository;

import com.gogidix.digitalmarketing.leadgeneration.domain.model.LeadHandoff;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * LeadHandoff Repository - Data access for LeadHandoff entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 *
 * <p>Uses compound indexes for efficient queries:</p>
 * <ul>
 *   <li>handoff_tenant_lead_idx on (tenantId, leadId, initiatedAt)</li>
 *   <li>handoff_tenant_status_idx on (tenantId, status, initiatedAt)</li>
 *   <li>handoff_tenant_sales_idx on (tenantId, assignedSalesRepId, status)</li>
 * </ul>
 */
@Repository
public interface LeadHandoffRepository extends BaseRepository<LeadHandoff> {

    // ========== Basic Queries ==========

    /**
     * Find handoffs by lead ID.
     *
     * @param leadId the lead ID
     * @return list of handoffs for the lead
     */
    List<LeadHandoff> findByLeadId(String leadId);

    /**
     * Find handoffs by status.
     *
     * @param status the handoff status
     * @return list of handoffs
     */
    List<LeadHandoff> findByStatus(String status);

    /**
     * Find handoffs by status with pagination.
     *
     * @param status the handoff status
     * @param pageable pagination parameters
     * @return page of handoffs
     */
    Page<LeadHandoff> findByStatus(String status, Pageable pageable);

    /**
     * Find handoffs by assigned sales representative.
     *
     * @param assignedSalesRepId the sales rep ID
     * @return list of handoffs
     */
    List<LeadHandoff> findByAssignedSalesRepId(String assignedSalesRepId);

    /**
     * Find handoffs by assigned sales representative with pagination.
     *
     * @param assignedSalesRepId the sales rep ID
     * @param pageable pagination parameters
     * @return page of handoffs
     */
    Page<LeadHandoff> findByAssignedSalesRepId(String assignedSalesRepId, Pageable pageable);

    /**
     * Find handoffs by sales team.
     *
     * @param salesTeamId the sales team ID
     * @return list of handoffs
     */
    List<LeadHandoff> findBySalesTeamId(String salesTeamId);

    /**
     * Find pending handoffs.
     *
     * @return list of pending handoffs
     */
    @Query("{ 'status': 'PENDING' }")
    List<LeadHandoff> findPendingHandoffs();

    /**
     * Find accepted handoffs.
     *
     * @return list of accepted handoffs
     */
    @Query("{ 'status': 'ACCEPTED' }")
    List<LeadHandoff> findAcceptedHandoffs();

    /**
     * Find rejected handoffs.
     *
     * @return list of rejected handoffs
     */
    @Query("{ 'status': 'REJECTED' }")
    List<LeadHandoff> findRejectedHandoffs();

    /**
     * Find returned handoffs.
     *
     * @return list of returned handoffs
     */
    @Query("{ 'status': 'RETURNED' }")
    List<LeadHandoff> findReturnedHandoffs();

    /**
     * Find converted handoffs.
     *
     * @return list of converted handoffs
     */
    @Query("{ 'status': 'CONVERTED' }")
    List<LeadHandoff> findConvertedHandoffs();

    /**
     * Find handoffs by status and assigned sales rep.
     *
     * @param status the handoff status
     * @param assignedSalesRepId the sales rep ID
     * @return list of handoffs
     */
    List<LeadHandoff> findByStatusAndAssignedSalesRepId(String status, String assignedSalesRepId);

    /**
     * Find handoffs by campaign ID.
     *
     * @param campaignId the campaign ID
     * @return list of handoffs
     */
    List<LeadHandoff> findByCampaignId(String campaignId);

    /**
     * Find handoffs initiated after date.
     *
     * @param date the initiation date threshold
     * @return list of handoffs
     */
    @Query("{ 'initiatedAt': { $gte: ?0 } }")
    List<LeadHandoff> findByInitiatedAtAfter(Instant date);

    /**
     * Find handoffs initiated between dates.
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of handoffs
     */
    @Query("{ 'initiatedAt': { $gte: ?0, $lte: ?1 } }")
    List<LeadHandoff> findByInitiatedAtBetween(Instant startDate, Instant endDate);

    /**
     * Find handoffs by priority.
     *
     * @param priority the priority level
     * @return list of handoffs
     */
    List<LeadHandoff> findByPriority(String priority);

    /**
     * Find high priority pending handoffs.
     *
     * @return list of high priority pending handoffs
     */
    @Query("{ 'status': 'PENDING', 'priority': 'HIGH' }")
    List<LeadHandoff> findHighPriorityPendingHandoffs();

    /**
     * Find handoffs by SLA compliance status.
     *
     * @param slaCompliance the SLA compliance status
     * @return list of handoffs
     */
    List<LeadHandoff> findBySlaCompliance(String slaCompliance);

    /**
     * Find handoffs with missed SLA.
     *
     * @return list of handoffs with missed SLA
     */
    @Query("{ 'slaCompliance': 'MISSED' }")
    List<LeadHandoff> findMissedSLAHandoffs();

    /**
     * Find handoffs by temperature.
     *
     * @param temperature the lead temperature
     * @return list of handoffs
     */
    List<LeadHandoff> findByTemperature(String temperature);

    /**
     * Find hot lead handoffs.
     *
     * @return list of hot lead handoffs
     */
    @Query("{ 'temperature': 'HOT', 'status': { $in: ['PENDING', 'ACCEPTED'] } }")
    List<LeadHandoff> findHotLeadHandoffs();

    /**
     * Find handoffs by minimum qualification score.
     *
     * @param score the minimum score
     * @return list of handoffs
     */
    @Query("{ 'qualificationScore': { $gte: ?0 } }")
    List<LeadHandoff> findByQualificationScoreGreaterThanEqual(Integer score);

    /**
     * Find handoffs by handoff method.
     *
     * @param handoffMethod the handoff method
     * @return list of handoffs
     */
    List<LeadHandoff> findByHandoffMethod(String handoffMethod);

    /**
     * Find handoffs by integration status.
     *
     * @param integrationStatus the integration status
     * @return list of handoffs
     */
    List<LeadHandoff> findByIntegrationStatus(String integrationStatus);

    /**
     * Find handoffs with failed integration.
     *
     * @return list of handoffs with failed integration
     */
    @Query("{ 'integrationStatus': 'FAILED' }")
    List<LeadHandoff> findFailedIntegrationHandoffs();

    /**
     * Find handoffs pending integration.
     *
     * @return list of handoffs pending integration
     */
    @Query("{ 'integrationStatus': 'PENDING' }")
    List<LeadHandoff> findPendingIntegrationHandoffs();

    /**
     * Find handoffs by initiated by user.
     *
     * @param initiatedBy the user ID
     * @return list of handoffs
     */
    List<LeadHandoff> findByInitiatedBy(String initiatedBy);

    /**
     * Find handoffs by risk level.
     *
     * @param riskLevel the risk level
     * @return list of handoffs
     */
    List<LeadHandoff> findByRiskLevel(String riskLevel);

    /**
     * Find high risk handoffs.
     *
     * @return list of high risk handoffs
     */
    @Query("{ 'riskLevel': 'HIGH', 'status': { $in: ['PENDING', 'ACCEPTED'] } }")
    List<LeadHandoff> findHighRiskHandoffs();

    /**
     * Find handoffs where first contact is overdue.
     *
     * @return list of handoffs with overdue first contact
     */
    @Query("{ 'status': 'ACCEPTED', 'firstContactAt': null, 'initiatedAt': { $lt: ?0 } }")
    List<LeadHandoff> findOverdueFirstContact(Instant threshold);

    /**
     * Count handoffs by status.
     *
     * @param status the handoff status
     * @return count of handoffs
     */
    long countByStatus(String status);

    /**
     * Count handoffs by assigned sales rep.
     *
     * @param assignedSalesRepId the sales rep ID
     * @return count of handoffs
     */
    long countByAssignedSalesRepId(String assignedSalesRepId);

    /**
     * Count handoffs by campaign.
     *
     * @param campaignId the campaign ID
     * @return count of handoffs
     */
    long countByCampaignId(String campaignId);

    /**
     * Find the latest handoff for a lead.
     *
     * @param leadId the lead ID
     * @return optional containing the latest handoff
     */
    @Query("{ 'leadId': ?0 }")
    List<LeadHandoff> findAllByLeadIdOrderByInitiatedAtDesc(String leadId);

    /**
     * Find handoffs with no follow-up activity.
     *
     * @param thresholdDate the date threshold
     * @return list of handoffs with no follow-up
     */
    @Query("{ 'status': 'ACCEPTED', 'followUpCount': 0, 'acceptedAt': { $lt: ?0 } }")
    List<LeadHandoff> findNoFollowUpHandoffs(Instant thresholdDate);

    /**
     * Find handoffs by multiple statuses.
     *
     * @param statuses list of statuses
     * @return list of handoffs
     */
    @Query("{ 'status': { $in: ?0 } }")
    List<LeadHandoff> findByStatusIn(List<String> statuses);

    /**
     * Count converted handoffs by sales rep.
     *
     * @param assignedSalesRepId the sales rep ID
     * @return count of converted handoffs
     */
    @Query("{ 'assignedSalesRepId': ?0, 'status': 'CONVERTED' }")
    long countConvertedBySalesRepId(String assignedSalesRepId);

    /**
     * Calculate average time to accept for a sales rep.
     *
     * @param assignedSalesRepId the sales rep ID
     * @return average time to accept in hours
     */
    @Query("{ 'assignedSalesRepId': ?0, 'timeToAcceptHours': { $ne: null } }")
    List<LeadHandoff> findWithTimeToAcceptBySalesRepId(String assignedSalesRepId);

    /**
     * Find handoffs by lead email.
     *
     * @param leadEmail the lead email
     * @return list of handoffs
     */
    List<LeadHandoff> findByLeadEmail(String leadEmail);

    /**
     * Find handoffs with SLA deadline approaching.
     *
     * @param from start of window
     * @param to end of window
     * @return list of handoffs with approaching SLA deadline
     */
    @Query("{ 'status': 'PENDING', 'slaDeadline': { $gte: ?0, $lte: ?1 } }")
    List<LeadHandoff> findWithApproachingSLADeadline(Instant from, Instant to);
}
