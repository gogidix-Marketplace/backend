package com.gogidix.customersupport.globalsupportdashboard.domain.repository;

import com.gogidix.customersupport.globalsupportdashboard.domain.model.AgentPerformance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AgentPerformance entity
 */
@Repository
public interface AgentPerformanceRepository extends MongoRepository<AgentPerformance, String> {

    /**
     * Find all agents for a tenant
     */
    List<AgentPerformance> findByTenantIdOrderByAgentNameAsc(String tenantId);

    /**
     * Find specific agent performance
     */
    Optional<AgentPerformance> findByTenantIdAndAgentId(String tenantId, String agentId);

    /**
     * Find agents by team
     */
    List<AgentPerformance> findByTenantIdAndTeamIdOrderByAgentNameAsc(String tenantId, String teamId);

    /**
     * Find agents by region
     */
    List<AgentPerformance> findByTenantIdAndRegionCodeOrderByAgentNameAsc(String tenantId, String regionCode);

    /**
     * Find active agents
     */
    List<AgentPerformance> findByTenantIdAndStatusIn(String tenantId, List<String> activeStatuses);

    /**
     * Find top performers by CSAT
     */
    List<AgentPerformance> findByTenantIdOrderByAvgCsatScoreDesc(String tenantId);

    /**
     * Find top performers by tickets resolved
     */
    List<AgentPerformance> findByTenantIdOrderByTicketsResolvedDesc(String tenantId);

    /**
     * Find agents needing attention (low CSAT or quality)
     */
    @Query("{ 'tenantId': ?0, '$or': [ { 'avgCsatScore': { $lt: ?1 } }, { 'qualityScore': { $lt: ?2 } } ] }")
    List<AgentPerformance> findAgentsNeedingAttention(String tenantId, double csatThreshold, double qualityThreshold);

    /**
     * Get agent performance trends
     */
    @Query("{ 'tenantId': ?0, 'performancePeriodStart': { $gte: ?1 }, 'performancePeriodEnd': { $lte: ?2 } }")
    List<AgentPerformance> findAgentPerformanceInPeriod(String tenantId, long periodStart, long periodEnd);

    /**
     * Find agents by status
     */
    List<AgentPerformance> findByTenantIdAndStatusOrderByAgentNameAsc(String tenantId, String status);
}
