package com.gogidix.digitalmarketing.leadgeneration.domain.repository;

import com.gogidix.digitalmarketing.leadgeneration.domain.model.LeadAssignment;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * LeadAssignment Repository - Data access for LeadAssignment entities
 *
 * <p>All queries automatically filter by current tenant via BaseRepository.</p>
 *
 * <p>Uses compound indexes for efficient queries:</p>
 * <ul>
 *   <li>assign_tenant_lead_idx on (tenantId, leadId, assignedAt)</li>
 *   <li>assign_tenant_rep_idx on (tenantId, assignedSalesRepId, status)</li>
 *   <li>assign_tenant_status_idx on (tenantId, status, assignedAt)</li>
 * </ul>
 */
@Repository
public interface LeadAssignmentRepository extends BaseRepository<LeadAssignment> {

    // ========== Basic Queries ==========

    /**
     * Find assignments by lead ID.
     *
     * @param leadId the lead ID
     * @return list of assignments for the lead
     */
    List<LeadAssignment> findByLeadId(String leadId);

    /**
     * Find current active assignment for a lead.
     *
     * @param leadId the lead ID
     * @return optional containing the active assignment
     */
    @Query("{ 'leadId': ?0, 'status': 'ACTIVE' }")
    List<LeadAssignment> findActiveByLeadId(String leadId);

    /**
     * Find assignments by status.
     *
     * @param status the assignment status
     * @return list of assignments
     */
    List<LeadAssignment> findByStatus(String status);

    /**
     * Find assignments by status with pagination.
     *
     * @param status the assignment status
     * @param pageable pagination parameters
     * @return page of assignments
     */
    Page<LeadAssignment> findByStatus(String status, Pageable pageable);

    /**
     * Find assignments by assigned sales representative.
     *
     * @param assignedSalesRepId the sales rep ID
     * @return list of assignments
     */
    List<LeadAssignment> findByAssignedSalesRepId(String assignedSalesRepId);

    /**
     * Find active assignments by assigned sales representative.
     *
     * @param assignedSalesRepId the sales rep ID
     * @return list of active assignments
     */
    @Query("{ 'assignedSalesRepId': ?0, 'status': 'ACTIVE' }")
    List<LeadAssignment> findActiveBySalesRepId(String assignedSalesRepId);

    /**
     * Find assignments by assigned sales representative with pagination.
     *
     * @param assignedSalesRepId the sales rep ID
     * @param pageable pagination parameters
     * @return page of assignments
     */
    Page<LeadAssignment> findByAssignedSalesRepId(String assignedSalesRepId, Pageable pageable);

    /**
     * Find assignments by sales team.
     *
     * @param salesTeamId the sales team ID
     * @return list of assignments
     */
    List<LeadAssignment> findBySalesTeamId(String salesTeamId);

    /**
     * Find active assignments by sales team.
     *
     * @param salesTeamId the sales team ID
     * @return list of active assignments
     */
    @Query("{ 'salesTeamId': ?0, 'status': 'ACTIVE' }")
    List<LeadAssignment> findActiveBySalesTeamId(String salesTeamId);

    /**
     * Find assignments by assignment strategy.
     *
     * @param assignmentStrategy the strategy used
     * @return list of assignments
     */
    List<LeadAssignment> findByAssignmentStrategy(String assignmentStrategy);

    /**
     * Find assignments by priority.
     *
     * @param priority the priority level
     * @return list of assignments
     */
    List<LeadAssignment> findByPriority(String priority);

    /**
     * Find high priority active assignments.
     *
     * @return list of high priority active assignments
     */
    @Query("{ 'status': 'ACTIVE', 'priority': 'HIGH' }")
    List<LeadAssignment> findHighPriorityActiveAssignments();

    /**
     * Find assignments by lead source.
     *
     * @param leadSource the lead source
     * @return list of assignments
     */
    List<LeadAssignment> findByLeadSource(String leadSource);

    /**
     * Find assignments by campaign ID.
     *
     * @param campaignId the campaign ID
     * @return list of assignments
     */
    List<LeadAssignment> findByCampaignId(String campaignId);

    /**
     * Find assignments assigned after date.
     *
     * @param date the assignment date threshold
     * @return list of assignments
     */
    @Query("{ 'assignedAt': { $gte: ?0 } }")
    List<LeadAssignment> findByAssignedAtAfter(Instant date);

    /**
     * Find assignments assigned between dates.
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of assignments
     */
    @Query("{ 'assignedAt': { $gte: ?0, $lte: ?1 } }")
    List<LeadAssignment> findByAssignedAtBetween(Instant startDate, Instant endDate);

    /**
     * Find unassigned leads (assignments with UNASSIGNED status).
     *
     * @return list of unassigned lead assignments
     */
    @Query("{ 'status': 'UNASSIGNED' }")
    List<LeadAssignment> findUnassigned();

    /**
     * Find reassigned assignments.
     *
     * @return list of reassigned assignments
     */
    @Query("{ 'status': 'REASSIGNED' }")
    List<LeadAssignment> findReassigned();

    /**
     * Find assignments by rep availability.
     *
     * @param repAvailability the availability status
     * @return list of assignments
     */
    List<LeadAssignment> findByRepAvailability(String repAvailability);

    /**
     * Find assignments where rep is available.
     *
     * @return list of assignments with available reps
     */
    @Query("{ 'repAvailability': 'AVAILABLE', 'status': 'ACTIVE' }")
    List<LeadAssignment> findWithAvailableRep();

    /**
     * Find assignments by rep workload range.
     *
     * @param minWorkload minimum workload
     * @param maxWorkload maximum workload
     * @return list of assignments
     */
    @Query("{ 'repWorkload': { $gte: ?0, $lte: ?1 } }")
    List<LeadAssignment> findByRepWorkloadBetween(Integer minWorkload, Integer maxWorkload);

    /**
     * Find assignments where rep is at max workload.
     *
     * @return list of assignments where rep is at max workload
     */
    @Query("{ $expr: { $gte: ['$repWorkload', '$repMaxWorkload'] } }")
    List<LeadAssignment> findRepAtMaxWorkload();

    /**
     * Find assignments by lead temperature.
     *
     * @param leadTemperature the lead temperature
     * @return list of assignments
     */
    List<LeadAssignment> findByLeadTemperature(String leadTemperature);

    /**
     * Find hot lead assignments.
     *
     * @return list of hot lead assignments
     */
    @Query("{ 'leadTemperature': 'HOT', 'status': 'ACTIVE' }")
    List<LeadAssignment> findHotLeadAssignments();

    /**
     * Find assignments by lead score range.
     *
     * @param minScore minimum score
     * @param maxScore maximum score
     * @return list of assignments
     */
    @Query("{ 'leadScore': { $gte: ?0, $lte: ?1 } }")
    List<LeadAssignment> findByLeadScoreBetween(Integer minScore, Integer maxScore);

    /**
     * Find assignments by assigned by user.
     *
     * @param assignedBy the user ID who assigned
     * @return list of assignments
     */
    List<LeadAssignment> findByAssignedBy(String assignedBy);

    /**
     * Find assignments with no first contact yet.
     *
     * @param threshold assigned before this date
     * @return list of assignments awaiting first contact
     */
    @Query("{ 'firstContactAt': null, 'assignedAt': { $lt: ?0 }, 'status': 'ACTIVE' }")
    List<LeadAssignment> findAwaitingFirstContact(Instant threshold);

    /**
     * Find expired assignments.
     *
     * @return list of expired assignments
     */
    @Query("{ 'expiresAt': { $lt: ?0 }, 'status': 'ACTIVE' }")
    List<LeadAssignment> findExpired(Instant now);

    /**
     * Find assignments with pending follow-up.
     *
     * @param threshold follow-up date before this threshold
     * @return list of assignments with pending follow-up
     */
    @Query("{ 'followUpDate': { $lt: ?0 }, 'status': 'ACTIVE' }")
    List<LeadAssignment> findWithPendingFollowUp(Instant threshold);

    /**
     * Find assignments by geographic match.
     *
     * @param geographicMatch the geographic region
     * @return list of assignments
     */
    List<LeadAssignment> findByGeographicMatch(String geographicMatch);

    /**
     * Find assignments by industry expertise match.
     *
     * @param industryExpertiseMatch the industry
     * @return list of assignments
     */
    List<LeadAssignment> findByIndustryExpertiseMatch(String industryExpertiseMatch);

    /**
     * Count active assignments by sales rep.
     *
     * @param assignedSalesRepId the sales rep ID
     * @return count of active assignments
     */
    @Query("{ 'assignedSalesRepId': ?0, 'status': 'ACTIVE' }")
    long countActiveBySalesRepId(String assignedSalesRepId);

    /**
     * Count active assignments by sales team.
     *
     * @param salesTeamId the sales team ID
     * @return count of active assignments
     */
    @Query("{ 'salesTeamId': ?0, 'status': 'ACTIVE' }")
    long countActiveBySalesTeamId(String salesTeamId);

    /**
     * Count assignments by status.
     *
     * @param status the assignment status
     * @return count of assignments
     */
    long countByStatus(String status);

    /**
     * Count assignments by priority.
     *
     * @param priority the priority level
     * @return count of assignments
     */
    long countByPriority(String priority);

    /**
     * Find the latest assignment for a lead.
     *
     * @param leadId the lead ID
     * @return optional containing the latest assignment
     */
    @Query("{ 'leadId': ?0 }")
    List<LeadAssignment> findAllByLeadIdOrderByAssignedAtDesc(String leadId);

    /**
     * Find assignments by multiple statuses.
     *
     * @param statuses list of statuses
     * @return list of assignments
     */
    @Query("{ 'status': { $in: ?0 } }")
    List<LeadAssignment> findByStatusIn(List<String> statuses);

    /**
     * Find assignments with auto-reassign enabled.
     *
     * @return list of assignments with auto-reassign
     */
    @Query("{ 'autoReassign': true, 'status': 'ACTIVE' }")
    List<LeadAssignment> findWithAutoReassign();

    /**
     * Find assignments by lead email.
     *
     * @param leadEmail the lead email
     * @return list of assignments
     */
    List<LeadAssignment> findByLeadEmail(String leadEmail);

    /**
     * Find assignments that need reassignment.
     *
     * @return list of assignments needing reassignment
     */
    @Query("{ $or: [ " +
            "{ 'status': 'UNASSIGNED' }, " +
            "{ 'status': 'ACTIVE', 'expiresAt': { $lt: ?0 } }, " +
            "{ 'status': 'ACTIVE', 'autoReassign': true, 'firstContactAt': null, 'assignedAt': { $lt: ?1 } } " +
            "] }")
    List<LeadAssignment> findNeedingReassignment(Instant now, Instant noContactThreshold);

    /**
     * Find manual assignments.
     *
     * @return list of manual assignments
     */
    @Query("{ 'assignmentStrategy': 'MANUAL' }")
    List<LeadAssignment> findManualAssignments();

    /**
     * Find round-robin assignments.
     *
     * @return list of round-robin assignments
     */
    @Query("{ 'assignmentStrategy': 'ROUND_ROBIN' }")
    List<LeadAssignment> findRoundRobinAssignments();

    /**
     * Find least-loaded assignments.
     *
     * @return list of least-loaded assignments
     */
    @Query("{ 'assignmentStrategy': 'LEAST_LOADED' }")
    List<LeadAssignment> findLeastLoadedAssignments();

    /**
     * Calculate current workload for a sales rep.
     *
     * @param assignedSalesRepId the sales rep ID
     * @return current workload count
     */
    @Query("{ 'assignedSalesRepId': ?0, 'status': 'ACTIVE' }")
    long calculateWorkloadForSalesRep(String assignedSalesRepId);

    /**
     * Find assignments with follow-up due today.
     *
     * @param start start of day
     * @param end end of day
     * @return list of assignments with follow-up due
     */
    @Query("{ 'followUpDate': { $gte: ?0, $lte: ?1 }, 'status': 'ACTIVE' }")
    List<LeadAssignment> findWithFollowUpDue(Instant start, Instant end);

    /**
     * Find assignments by multiple criteria.
     *
     * @param assignedSalesRepId the sales rep ID
     * @param status the status
     * @param priority the priority
     * @return list of assignments
     */
    List<LeadAssignment> findByAssignedSalesRepIdAndStatusAndPriority(
            String assignedSalesRepId, String status, String priority);

    /**
     * Find high-value lead assignments (high score + hot).
     *
     * @return list of high-value assignments
     */
    @Query("{ 'leadScore': { $gte: 80 }, 'leadTemperature': 'HOT', 'status': 'ACTIVE' }")
    List<LeadAssignment> findHighValueAssignments();
}
