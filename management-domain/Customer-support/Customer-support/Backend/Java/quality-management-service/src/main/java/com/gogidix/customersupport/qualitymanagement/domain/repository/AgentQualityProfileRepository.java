package com.gogidix.customersupport.qualitymanagement.domain.repository;

import com.gogidix.customersupport.qualitymanagement.domain.model.AgentQualityProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AgentQualityProfile entity
 */
@Repository
public interface AgentQualityProfileRepository extends MongoRepository<AgentQualityProfile, String> {

    /**
     * Find profile by agent ID
     */
    Optional<AgentQualityProfile> findByTenantIdAndAgentId(String tenantId, String agentId);

    /**
     * Find all profiles by tenant ID
     */
    List<AgentQualityProfile> findByTenantIdOrderByAgentNameAsc(String tenantId);

    /**
     * Find all profiles by tenant ID with pagination
     */
    Page<AgentQualityProfile> findByTenantIdOrderByAgentNameAsc(String tenantId, Pageable pageable);

    /**
     * Find profiles by quality rank
     */
    List<AgentQualityProfile> findByTenantIdAndOverallQualityRankOrderByOverallQualityScoreDesc(
            String tenantId, AgentQualityProfile.QualityRank qualityRank);

    /**
     * Find profiles by quality trend
     */
    List<AgentQualityProfile> findByTenantIdAndQualityTrendOrderByOverallQualityScoreDesc(
            String tenantId, AgentQualityProfile.QualityTrend qualityTrend);

    /**
     * Find profiles requiring coaching
     */
    List<AgentQualityProfile> findByTenantIdAndCoachingRequiredTrueOrderByOverallQualityScoreAsc(String tenantId);

    /**
     * Find profiles by coaching priority
     */
    List<AgentQualityProfile> findByTenantIdAndCoachingPriorityOrderByOverallQualityScoreAsc(
            String tenantId, AgentQualityProfile.CoachingPriority coachingPriority);

    /**
     * Find profiles by risk level
     */
    List<AgentQualityProfile> findByTenantIdAndQualityRiskLevelOrderByOverallQualityScoreAsc(
            String tenantId, AgentQualityProfile.RiskLevel riskLevel);

    /**
     * Find profiles by team
     */
    List<AgentQualityProfile> findByTenantIdAndTeamIdOrderByOverallQualityScoreDesc(String tenantId, String teamId);

    /**
     * Find top performing agents
     */
    List<AgentQualityProfile> findByTenantIdOrderByOverallQualityScoreDesc(String tenantId);

    /**
     * Find agents needing improvement
     */
    @Query("{ 'tenantId': ?0, 'overallQualityScore': { $lt: ?1 } }")
    List<AgentQualityProfile> findAgentsNeedingImprovement(String tenantId, double scoreThreshold);

    /**
     * Find agents with pass rate below threshold
     */
    @Query("{ 'tenantId': ?0, 'passRatePercentage': { $lt: ?1 } }")
    List<AgentQualityProfile> findAgentsWithLowPassRate(String tenantId, double passRateThreshold);

    /**
     * Find agents by score range
     */
    @Query("{ 'tenantId': ?0, 'overallQualityScore': { $gte: ?1, $lte: ?2 } }")
    List<AgentQualityProfile> findByTenantIdAndScoreRange(
            String tenantId, double minScore, double maxScore);

    /**
     * Find profiles not recently updated
     */
    @Query("{ 'tenantId': ?0, 'profileLastCalculated': { $lt: ?1 } }")
    List<AgentQualityProfile> findProfilesNeedingRecalculation(String tenantId, Instant cutoffDate);

    /**
     * Search profiles by agent name
     */
    @Query("{ 'tenantId': ?0, 'agentName': { $regex: ?1, $options: 'i' } }")
    List<AgentQualityProfile> searchByAgentName(String tenantId, String namePattern);

    /**
     * Find profiles by tags
     */
    @Query("{ 'tenantId': ?0, 'tags': { $in: ?1 } }")
    List<AgentQualityProfile> findByTenantIdAndTagsContaining(String tenantId, List<String> tags);

    /**
     * Count agents by quality rank
     */
    long countByTenantIdAndOverallQualityRank(String tenantId, AgentQualityProfile.QualityRank qualityRank);

    /**
     * Count agents requiring coaching
     */
    long countByTenantIdAndCoachingRequiredTrue(String tenantId);

    /**
     * Count agents by risk level
     */
    long countByTenantIdAndQualityRiskLevel(String tenantId, AgentQualityProfile.RiskLevel riskLevel);

    /**
     * Find agents with recent reviews
     */
    @Query("{ 'tenantId': ?0, 'lastReviewDate': { $gte: ?1 } }")
    List<AgentQualityProfile> findAgentsWithRecentReviews(String tenantId, Instant sinceDate);

    /**
     * Find agents without recent reviews
     */
    @Query("{ 'tenantId': ?0, $or: [ { 'lastReviewDate': null }, { 'lastReviewDate': { $lt: ?1 } } ] }")
    List<AgentQualityProfile> findAgentsWithoutRecentReviews(String tenantId, Instant sinceDate);

    /**
     * Delete profiles older than specified date
     */
    void deleteByTenantIdAndUpdatedAtBefore(String tenantId, Instant cutoffDate);

    /**
     * Find agents with goal achieved
     */
    @Query("{ 'tenantId': ?0, 'goalAchieved': true }")
    List<AgentQualityProfile> findAgentsWithGoalAchieved(String tenantId);

    /**
     * Find agents by manager
     */
    List<AgentQualityProfile> findByTenantIdAndManagerIdOrderByOverallQualityScoreDesc(String tenantId, String managerId);

    /**
     * Get team statistics
     */
    @Query(value = "{ 'tenantId': ?0, 'teamId': ?1 }", count = true)
    long countByTenantIdAndTeamId(String tenantId, String teamId);

    /**
     * Find agents with failing streak
     */
    @Query("{ 'tenantId': ?0, 'currentStreakType': 'FAILING', 'currentStreak': { $gte: ?1 } }")
    List<AgentQualityProfile> findAgentsWithFailingStreak(String tenantId, int streakThreshold);

    /**
     * Find exemplary agents
     */
    @Query("{ 'tenantId': ?0, 'overallQualityRank': 'EXEMPLARY', 'overallQualityScore': { $gte: ?1 } }")
    List<AgentQualityProfile> findExemplaryAgents(String tenantId, double minScore);

    /**
     * Find agents in development
     */
    @Query("{ 'tenantId': ?0, 'overallQualityRank': { $in: ['DEVELOPING', 'NEEDS_IMPROVEMENT'] } }")
    List<AgentQualityProfile> findAgentsInDevelopment(String tenantId);

    /**
     * Find agents by certification
     */
    @Query("{ 'tenantId': ?0, 'certificationsEarned': { $in: ?1 } }")
    List<AgentQualityProfile> findAgentsByCertification(String tenantId, String certification);
}
