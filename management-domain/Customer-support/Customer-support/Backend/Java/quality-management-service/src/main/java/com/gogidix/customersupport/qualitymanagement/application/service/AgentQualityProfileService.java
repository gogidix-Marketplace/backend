package com.gogidix.customersupport.qualitymanagement.application.service;

import com.gogidix.customersupport.qualitymanagement.application.dto.AgentQualityProfileDto;
import com.gogidix.customersupport.qualitymanagement.application.mapper.QualityManagementMapper;
import com.gogidix.customersupport.qualitymanagement.domain.model.AgentQualityProfile;
import com.gogidix.customersupport.qualitymanagement.domain.model.QaReview;
import com.gogidix.customersupport.qualitymanagement.domain.repository.AgentQualityProfileRepository;
import com.gogidix.customersupport.qualitymanagement.domain.repository.QaReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Service for Agent Quality Profile operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgentQualityProfileService {

    private final AgentQualityProfileRepository profileRepository;
    private final QaReviewRepository reviewRepository;
    private final QualityManagementMapper mapper;

    private static final String DEFAULT_TENANT_ID = "default";

    /**
     * Get agent profile by agent ID
     */
    @Cacheable(value = "agentProfiles", key = "#tenantId + ':' + #agentId")
    public AgentQualityProfileDto getProfileByAgentId(String tenantId, String agentId) {
        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        log.debug("Fetching quality profile for tenant: {}, agent: {}", effectiveTenantId, agentId);

        return profileRepository.findByTenantIdAndAgentId(effectiveTenantId, agentId)
                .map(mapper::toDto)
                .orElseGet(() -> {
                    log.info("Profile not found for agent: {}, creating new profile", agentId);
                    return createAndSaveNewProfile(effectiveTenantId, agentId);
                });
    }

    /**
     * Get all profiles for tenant
     */
    public List<AgentQualityProfileDto> getAllProfiles(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return profileRepository.findByTenantIdOrderByAgentNameAsc(tenantId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get profiles with pagination
     */
    public Page<AgentQualityProfileDto> getProfilesPaginated(String tenantId, int page, int size, String sortBy, String sortDir) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return profileRepository.findByTenantIdOrderByAgentNameAsc(tenantId, pageable)
                .map(mapper::toDto);
    }

    /**
     * Get profiles by quality rank
     */
    public List<AgentQualityProfileDto> getProfilesByRank(String tenantId, String rank) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        AgentQualityProfile.QualityRank qualityRank = AgentQualityProfile.QualityRank.valueOf(rank);

        return profileRepository.findByTenantIdAndOverallQualityRankOrderByOverallQualityScoreDesc(tenantId, qualityRank)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get profiles requiring coaching
     */
    public List<AgentQualityProfileDto> getProfilesRequiringCoaching(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return profileRepository.findByTenantIdAndCoachingRequiredTrueOrderByOverallQualityScoreAsc(tenantId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get profiles by coaching priority
     */
    public List<AgentQualityProfileDto> getProfilesByCoachingPriority(String tenantId, String priority) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        AgentQualityProfile.CoachingPriority coachingPriority = AgentQualityProfile.CoachingPriority.valueOf(priority);

        return profileRepository.findByTenantIdAndCoachingPriorityOrderByOverallQualityScoreAsc(tenantId, coachingPriority)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get profiles by team
     */
    public List<AgentQualityProfileDto> getProfilesByTeam(String tenantId, String teamId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return profileRepository.findByTenantIdAndTeamIdOrderByOverallQualityScoreDesc(tenantId, teamId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get top performing agents
     */
    public List<AgentQualityProfileDto.QualityLeaderboardDto> getTopAgents(String tenantId, int limit) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return profileRepository.findByTenantIdOrderByOverallQualityScoreDesc(tenantId)
                .stream()
                .limit(limit)
                .map(profile -> {
                    AgentQualityProfileDto.QualityLeaderboardDto dto = AgentQualityProfileDto.QualityLeaderboardDto.builder()
                            .agentId(profile.getAgentId())
                            .agentName(profile.getAgentName())
                            .teamName(profile.getTeamName())
                            .overallQualityScore(profile.getOverallQualityScore())
                            .overallQualityRank(profile.getOverallQualityRank() != null ? profile.getOverallQualityRank().name() : null)
                            .totalReviews(profile.getTotalReviews())
                            .passRatePercentage(profile.getPassRatePercentage())
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get quality leaderboard
     */
    public List<AgentQualityProfileDto.QualityLeaderboardDto> getLeaderboard(String tenantId, String teamId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        List<AgentQualityProfile> profiles;
        if (teamId != null && !teamId.isEmpty()) {
            profiles = profileRepository.findByTenantIdAndTeamIdOrderByOverallQualityScoreDesc(tenantId, teamId);
        } else {
            profiles = profileRepository.findByTenantIdOrderByOverallQualityScoreDesc(tenantId);
        }

        AtomicInteger rank = new AtomicInteger(1);
        return profiles.stream()
                .map(profile -> AgentQualityProfileDto.QualityLeaderboardDto.builder()
                        .agentId(profile.getAgentId())
                        .agentName(profile.getAgentName())
                        .teamName(profile.getTeamName())
                        .overallQualityScore(profile.getOverallQualityScore())
                        .overallQualityRank(profile.getOverallQualityRank() != null ? profile.getOverallQualityRank().name() : null)
                        .totalReviews(profile.getTotalReviews())
                        .passRatePercentage(profile.getPassRatePercentage())
                        .rank(rank.getAndIncrement())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get quality metrics for tenant
     */
    public AgentQualityProfileDto.QualityMetricsDto getQualityMetrics(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        List<AgentQualityProfile> allProfiles = profileRepository.findByTenantIdOrderByAgentNameAsc(tenantId);

        AgentQualityProfileDto.QualityMetricsDto metrics = AgentQualityProfileDto.QualityMetricsDto.builder()
                .totalAgents((long) allProfiles.size())
                .agentsReviewed(allProfiles.stream().filter(p -> p.getTotalReviews() != null && p.getTotalReviews() > 0).count())
                .exemplaryAgents(allProfiles.stream().filter(p -> AgentQualityProfile.QualityRank.EXEMPLARY.equals(p.getOverallQualityRank())).count())
                .agentsNeedingImprovement(allProfiles.stream().filter(p -> AgentQualityProfile.QualityRank.DEVELOPING.equals(p.getOverallQualityRank()) ||
                        AgentQualityProfile.QualityRank.NEEDS_IMPROVEMENT.equals(p.getOverallQualityRank())).count())
                .criticalRiskAgents(allProfiles.stream().filter(p -> AgentQualityProfile.RiskLevel.CRITICAL.equals(p.getQualityRiskLevel())).count())
                .coachingRequiredAgents(allProfiles.stream().filter(AgentQualityProfile::getCoachingRequired).count())
                .build();

        // Calculate averages
        double avgScore = allProfiles.stream()
                .filter(p -> p.getOverallQualityScore() != null)
                .mapToDouble(AgentQualityProfile::getOverallQualityScore)
                .average()
                .orElse(0.0);

        double avgPassRate = allProfiles.stream()
                .filter(p -> p.getPassRatePercentage() != null)
                .mapToDouble(AgentQualityProfile::getPassRatePercentage)
                .average()
                .orElse(0.0);

        metrics.setAverageQualityScore(avgScore);
        metrics.setOverallPassRate(avgPassRate);

        return metrics;
    }

    /**
     * Get agents needing improvement
     */
    public List<AgentQualityProfileDto.AgentQualityProfileSummaryDto> getAgentsNeedingImprovement(String tenantId, double scoreThreshold) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return profileRepository.findAgentsNeedingImprovement(tenantId, scoreThreshold)
                .stream()
                .map(mapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * Recalculate agent profile
     */
    @Transactional
    @CacheEvict(value = "agentProfiles", allEntries = true)
    public AgentQualityProfileDto recalculateAgentProfile(String tenantId, String agentId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        log.info("Recalculating quality profile for tenant: {}, agent: {}", tenantId, agentId);

        AgentQualityProfile profile = profileRepository.findByTenantIdAndAgentId(tenantId, agentId)
                .orElse(new AgentQualityProfile(tenantId));

        // Get completed reviews for this agent
        List<QaReview> completedReviews = reviewRepository.findByTenantIdAndAgentIdOrderByReviewDateDesc(tenantId, agentId)
                .stream()
                .filter(r -> QaReview.ReviewStatus.COMPLETED.equals(r.getReviewStatus()))
                .collect(Collectors.toList());

        if (completedReviews.isEmpty()) {
            log.info("No completed reviews found for agent: {}", agentId);
            profile.setAgentId(agentId);
            profile.setOverallQualityScore(0.0);
            profile.setTotalReviews(0L);
            profile.setPassRatePercentage(0.0);
        } else {
            // Calculate statistics
            profile.setAgentId(agentId);
            profile.setAgentName(completedReviews.get(0).getAgentName());
            // teamId and teamName not available in QaReview, setting to null
            profile.setTeamId(null);
            profile.setTeamName(null);

            long totalReviews = completedReviews.size();
            long passedReviews = completedReviews.stream()
                    .filter(r -> Boolean.TRUE.equals(r.getPassed()))
                    .count();

            profile.setTotalReviews(totalReviews);
            profile.setPassedReviews(passedReviews);
            profile.setFailedReviews(totalReviews - passedReviews);

            // Calculate average score
            double avgScore = completedReviews.stream()
                    .filter(r -> r.getPercentageScore() != null)
                    .mapToDouble(QaReview::getPercentageScore)
                    .average()
                    .orElse(0.0);

            profile.setAverageScore(avgScore);
            profile.setOverallQualityScore(avgScore);

            // Calculate pass rate
            profile.setPassRatePercentage((passedReviews * 100.0) / totalReviews);

            // Calculate highest and lowest scores
            profile.setHighestScore(completedReviews.stream()
                    .filter(r -> r.getPercentageScore() != null)
                    .mapToDouble(QaReview::getPercentageScore)
                    .max()
                    .orElse(0.0));

            profile.setLowestScore(completedReviews.stream()
                    .filter(r -> r.getPercentageScore() != null)
                    .mapToDouble(QaReview::getPercentageScore)
                    .min()
                    .orElse(0.0));

            // Set last review info
            profile.setLastReviewDate(completedReviews.get(0).getReviewDate());
            profile.setLastReviewScore(completedReviews.get(0).getPercentageScore());
            profile.setLastReviewPassed(completedReviews.get(0).getPassed());

            // Calculate period metrics
            calculatePeriodMetrics(profile, completedReviews);

            // Determine rank, trend, and risk level
            profile.determineQualityRank();
            profile.determineRiskLevel();

            // Set coaching requirements
            profile.setCoachingRequired(avgScore < 70 || profile.getPassRatePercentage() < 70);
            if (avgScore < 60) {
                profile.setCoachingPriority(AgentQualityProfile.CoachingPriority.CRITICAL);
            } else if (avgScore < 70) {
                profile.setCoachingPriority(AgentQualityProfile.CoachingPriority.HIGH);
            } else if (avgScore < 80) {
                profile.setCoachingPriority(AgentQualityProfile.CoachingPriority.MEDIUM);
            } else {
                profile.setCoachingPriority(AgentQualityProfile.CoachingPriority.NONE);
            }

            // Build score history
            updateScoreHistory(profile, completedReviews);

            // Determine quality trend
            profile.setQualityTrend(determineQualityTrend(completedReviews));
        }

        profile.markAsRecalculated();
        AgentQualityProfile saved = profileRepository.save(profile);
        log.info("Recalculated quality profile for agent: {} with score: {}", agentId, saved.getOverallQualityScore());

        return mapper.toDto(saved);
    }

    /**
     * Update profile goal
     */
    @Transactional
    @CacheEvict(value = "agentProfiles", allEntries = true)
    public AgentQualityProfileDto updateGoal(String tenantId, String agentId, double goal, Instant targetDate) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        AgentQualityProfile profile = profileRepository.findByTenantIdAndAgentId(tenantId, agentId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for agent: " + agentId));

        profile.setQualityGoal(goal);
        profile.setGoalTargetDate(targetDate);
        profile.calculateGoalProgress();
        profile.updateTimestamp();

        AgentQualityProfile saved = profileRepository.save(profile);
        return mapper.toDto(saved);
    }

    /**
     * Add coaching note
     */
    @Transactional
    @CacheEvict(value = "agentProfiles", allEntries = true)
    public AgentQualityProfileDto addCoachingNote(String tenantId, String agentId, String note, String category, String createdBy, String createdByName) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        AgentQualityProfile profile = profileRepository.findByTenantIdAndAgentId(tenantId, agentId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for agent: " + agentId));

        AgentQualityProfile.CoachingNote coachingNote = AgentQualityProfile.CoachingNote.builder()
                .noteId(java.util.UUID.randomUUID().toString())
                .note(note)
                .createdBy(createdBy)
                .createdByName(createdByName)
                .createdAt(Instant.now())
                .category(category)
                .isActionable(true)
                .build();

        profile.addCoachingNote(coachingNote);
        profile.setCoachingRequired(true);

        AgentQualityProfile saved = profileRepository.save(profile);
        return mapper.toDto(saved);
    }

    /**
     * Delete profile
     */
    @Transactional
    @CacheEvict(value = "agentProfiles", allEntries = true)
    public void deleteProfile(String tenantId, String agentId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        AgentQualityProfile profile = profileRepository.findByTenantIdAndAgentId(tenantId, agentId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for agent: " + agentId));

        profileRepository.delete(profile);
        log.info("Deleted quality profile for agent: {}", agentId);
    }

    // Private helper methods

    private AgentQualityProfileDto createAndSaveNewProfile(String tenantId, String agentId) {
        AgentQualityProfile profile = new AgentQualityProfile(tenantId);
        profile.setAgentId(agentId);
        profile.setOverallQualityScore(0.0);
        profile.setTotalReviews(0L);
        profile.setPassRatePercentage(0.0);
        profile.setQualityTrend(AgentQualityProfile.QualityTrend.INSUFFICIENT_DATA);
        profile.setOverallQualityRank(AgentQualityProfile.QualityRank.COMPETENT);
        profile.setCoachingRequired(false);
        profile.setCoachingPriority(AgentQualityProfile.CoachingPriority.NONE);
        profile.setQualityRiskLevel(AgentQualityProfile.RiskLevel.LOW);

        AgentQualityProfile saved = profileRepository.save(profile);
        return mapper.toDto(saved);
    }

    private void calculatePeriodMetrics(AgentQualityProfile profile, List<QaReview> completedReviews) {
        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("UTC");

        // Monthly
        LocalDate monthStart = LocalDate.now().minusDays(30);
        Instant monthStartInstant = monthStart.atStartOfDay(zoneId).toInstant();
        List<QaReview> monthlyReviews = completedReviews.stream()
                .filter(r -> r.getReviewDate() != null && !r.getReviewDate().isBefore(monthStartInstant))
                .collect(Collectors.toList());

        if (!monthlyReviews.isEmpty()) {
            profile.setMonthlyReviewsCount((long) monthlyReviews.size());
            profile.setMonthlyAverageScore(monthlyReviews.stream()
                    .filter(r -> r.getPercentageScore() != null)
                    .mapToDouble(QaReview::getPercentageScore)
                    .average()
                    .orElse(0.0));
            long monthlyPassed = monthlyReviews.stream().filter(r -> Boolean.TRUE.equals(r.getPassed())).count();
            profile.setMonthlyPassRate((monthlyPassed * 100.0) / monthlyReviews.size());
        }

        // Quarterly
        LocalDate quarterStart = LocalDate.now().minusDays(90);
        Instant quarterStartInstant = quarterStart.atStartOfDay(zoneId).toInstant();
        List<QaReview> quarterlyReviews = completedReviews.stream()
                .filter(r -> r.getReviewDate() != null && !r.getReviewDate().isBefore(quarterStartInstant))
                .collect(Collectors.toList());

        if (!quarterlyReviews.isEmpty()) {
            profile.setQuarterlyReviewsCount((long) quarterlyReviews.size());
            profile.setQuarterlyAverageScore(quarterlyReviews.stream()
                    .filter(r -> r.getPercentageScore() != null)
                    .mapToDouble(QaReview::getPercentageScore)
                    .average()
                    .orElse(0.0));
            long quarterlyPassed = quarterlyReviews.stream().filter(r -> Boolean.TRUE.equals(r.getPassed())).count();
            profile.setQuarterlyPassRate((quarterlyPassed * 100.0) / quarterlyReviews.size());
        }

        // Yearly
        LocalDate yearStart = LocalDate.now().minusDays(365);
        Instant yearStartInstant = yearStart.atStartOfDay(zoneId).toInstant();
        List<QaReview> yearlyReviews = completedReviews.stream()
                .filter(r -> r.getReviewDate() != null && !r.getReviewDate().isBefore(yearStartInstant))
                .collect(Collectors.toList());

        if (!yearlyReviews.isEmpty()) {
            profile.setYearlyReviewsCount((long) yearlyReviews.size());
            profile.setYearlyAverageScore(yearlyReviews.stream()
                    .filter(r -> r.getPercentageScore() != null)
                    .mapToDouble(QaReview::getPercentageScore)
                    .average()
                    .orElse(0.0));
            long yearlyPassed = yearlyReviews.stream().filter(r -> Boolean.TRUE.equals(r.getPassed())).count();
            profile.setYearlyPassRate((yearlyPassed * 100.0) / yearlyReviews.size());
        }
    }

    private void updateScoreHistory(AgentQualityProfile profile, List<QaReview> completedReviews) {
        List<AgentQualityProfile.ScoreSnapshot> snapshots = completedReviews.stream()
                .limit(50)
                .map(review -> AgentQualityProfile.ScoreSnapshot.builder()
                        .reviewId(review.getReviewId())
                        .score(review.getPercentageScore())
                        .passed(review.getPassed())
                        .reviewType(review.getReviewType() != null ? review.getReviewType().name() : null)
                        .reviewDate(review.getReviewDate())
                        .channel(review.getChannelType() != null ? review.getChannelType().name() : null)
                        .build())
                .collect(Collectors.toList());

        profile.setScoreHistory(snapshots);
    }

    private AgentQualityProfile.QualityTrend determineQualityTrend(List<QaReview> reviews) {
        if (reviews.size() < 3) {
            return AgentQualityProfile.QualityTrend.INSUFFICIENT_DATA;
        }

        // Compare recent 5 reviews to previous 5 reviews
        int recentCount = Math.min(5, reviews.size() / 2);
        List<QaReview> recentReviews = reviews.subList(0, recentCount);
        List<QaReview> previousReviews = reviews.subList(recentCount, Math.min(recentCount * 2, reviews.size()));

        double recentAvg = recentReviews.stream()
                .filter(r -> r.getPercentageScore() != null)
                .mapToDouble(QaReview::getPercentageScore)
                .average()
                .orElse(0.0);

        double previousAvg = previousReviews.stream()
                .filter(r -> r.getPercentageScore() != null)
                .mapToDouble(QaReview::getPercentageScore)
                .average()
                .orElse(0.0);

        double difference = recentAvg - previousAvg;

        if (difference > 5) {
            return AgentQualityProfile.QualityTrend.IMPROVING;
        } else if (difference < -5) {
            return AgentQualityProfile.QualityTrend.DECLINING;
        } else {
            return AgentQualityProfile.QualityTrend.STABLE;
        }
    }
}
