package com.gogidix.customersupport.qualitymanagement.application.mapper;

import com.gogidix.customersupport.qualitymanagement.application.dto.*;
import com.gogidix.customersupport.qualitymanagement.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between domain models and DTOs for Quality Management
 */
@Component
public class QualityManagementMapper {

    /**
     * Convert QaReview entity to DTO
     */
    public QaReviewDto toDto(QaReview entity) {
        if (entity == null) {
            return null;
        }

        return QaReviewDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .reviewId(entity.getReviewId())
                .ticketId(entity.getTicketId())
                .interactionId(entity.getInteractionId())
                .agentId(entity.getAgentId())
                .agentName(entity.getAgentName())
                .reviewerId(entity.getReviewerId())
                .reviewerName(entity.getReviewerName())
                .reviewType(entity.getReviewType() != null ? entity.getReviewType().name() : null)
                .reviewStatus(entity.getReviewStatus() != null ? entity.getReviewStatus().name() : null)
                .channelType(entity.getChannelType() != null ? entity.getChannelType().name() : null)
                .scorecardTemplateId(entity.getScorecardTemplateId())
                .scorecardTemplateName(entity.getScorecardTemplateName())
                .totalScore(entity.getTotalScore())
                .maxScore(entity.getMaxScore())
                .percentageScore(entity.getPercentageScore())
                .weightedScore(entity.getWeightedScore())
                .passed(entity.getPassed())
                .criticalFailures(entity.getCriticalFailures())
                .criteriaScores(mapCriteriaScores(entity.getCriteriaScores()))
                .overallComments(entity.getOverallComments())
                .strengths(entity.getStrengths())
                .areasForImprovement(entity.getAreasForImprovement())
                .agentCoachingNotes(entity.getAgentCoachingNotes())
                .reviewDate(entity.getReviewDate())
                .interactionDate(entity.getInteractionDate())
                .completedAt(entity.getCompletedAt())
                .dueDate(entity.getDueDate())
                .calibrationSessionId(entity.getCalibrationSessionId())
                .isCalibrated(entity.getIsCalibrated())
                .calibrationNotes(entity.getCalibrationNotes())
                .requiresEscalation(entity.getRequiresEscalation())
                .escalationReason(entity.getEscalationReason())
                .escalatedTo(entity.getEscalatedTo())
                .escalatedAt(entity.getEscalatedAt())
                .requiresFollowUp(entity.getRequiresFollowUp())
                .followUpDate(entity.getFollowUpDate())
                .followUpCompleted(entity.getFollowUpCompleted())
                .reviewCycle(entity.getReviewCycle())
                .batchId(entity.getBatchId())
                .externalReferenceId(entity.getExternalReferenceId())
                .tags(entity.getTags())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convert QaReview DTO to entity
     */
    public QaReview toEntity(QaReviewDto.CreateQaReviewRequest dto) {
        if (dto == null) {
            return null;
        }

        QaReview review = new QaReview(dto.getTenantId());
        review.setTicketId(dto.getTicketId());
        review.setInteractionId(dto.getInteractionId());
        review.setAgentId(dto.getAgentId());
        review.setAgentName(dto.getAgentName());
        review.setReviewerId(dto.getReviewerId());
        review.setReviewerName(dto.getReviewerName());

        if (dto.getReviewType() != null) {
            review.setReviewType(QaReview.ReviewType.valueOf(dto.getReviewType()));
        }

        if (dto.getChannelType() != null) {
            review.setChannelType(QaReview.ChannelType.valueOf(dto.getChannelType()));
        }

        review.setScorecardTemplateId(dto.getScorecardTemplateId());
        review.setCriteriaScores(mapCriteriaScoresDtos(dto.getCriteriaScores()));
        review.setOverallComments(dto.getOverallComments());
        review.setInteractionDate(dto.getInteractionDate());
        review.setDueDate(dto.getDueDate());
        review.setReviewCycle(dto.getReviewCycle());
        review.setBatchId(dto.getBatchId());
        review.setTags(dto.getTags() != null ? dto.getTags() : new ArrayList<>());

        return review;
    }

    /**
     * Update QaReview entity from DTO
     */
    public void updateEntity(QaReview entity, QaReviewDto.UpdateQaReviewRequest dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getReviewStatus() != null) {
            entity.setReviewStatus(QaReview.ReviewStatus.valueOf(dto.getReviewStatus()));
        }
        if (dto.getCriteriaScores() != null) {
            entity.setCriteriaScores(mapCriteriaScoresDtos(dto.getCriteriaScores()));
        }
        if (dto.getOverallComments() != null) {
            entity.setOverallComments(dto.getOverallComments());
        }
        if (dto.getStrengths() != null) {
            entity.setStrengths(dto.getStrengths());
        }
        if (dto.getAreasForImprovement() != null) {
            entity.setAreasForImprovement(dto.getAreasForImprovement());
        }
        if (dto.getAgentCoachingNotes() != null) {
            entity.setAgentCoachingNotes(dto.getAgentCoachingNotes());
        }
        if (dto.getRequiresEscalation() != null) {
            entity.setRequiresEscalation(dto.getRequiresEscalation());
        }
        if (dto.getEscalationReason() != null) {
            entity.setEscalationReason(dto.getEscalationReason());
        }
        if (dto.getEscalatedTo() != null) {
            entity.setEscalatedTo(dto.getEscalatedTo());
        }
        if (dto.getRequiresFollowUp() != null) {
            entity.setRequiresFollowUp(dto.getRequiresFollowUp());
        }
        if (dto.getFollowUpDate() != null) {
            entity.setFollowUpDate(dto.getFollowUpDate());
        }
        if (dto.getCalibrationSessionId() != null) {
            entity.setCalibrationSessionId(dto.getCalibrationSessionId());
        }
        if (dto.getCalibrationNotes() != null) {
            entity.setCalibrationNotes(dto.getCalibrationNotes());
        }
        if (dto.getTags() != null) {
            entity.setTags(dto.getTags());
        }
        entity.updateTimestamp();
    }

    /**
     * Convert ScorecardTemplate entity to DTO
     */
    public ScorecardTemplateDto toDto(ScorecardTemplate entity) {
        if (entity == null) {
            return null;
        }

        return ScorecardTemplateDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .templateId(entity.getTemplateId())
                .templateName(entity.getTemplateName())
                .templateCode(entity.getTemplateCode())
                .description(entity.getDescription())
                .templateType(entity.getTemplateType() != null ? entity.getTemplateType().name() : null)
                .channelType(entity.getChannelType() != null ? entity.getChannelType().name() : null)
                .category(entity.getCategory())
                .version(entity.getVersion())
                .isActive(entity.getIsActive())
                .isDefault(entity.getIsDefault())
                .maxScore(entity.getMaxScore())
                .passingScore(entity.getPassingScore())
                .passingPercentage(entity.getPassingPercentage())
                .weight(entity.getWeight())
                .allowPartialCredit(entity.getAllowPartialCredit())
                .criteriaSections(mapCriteriaSections(entity.getCriteriaSections()))
                .totalCriteriaCount(entity.getTotalCriteriaCount())
                .criticalFailureEnabled(entity.getCriticalFailureEnabled())
                .criticalFailureThreshold(entity.getCriticalFailureThreshold())
                .createdBy(entity.getCreatedBy())
                .createdByName(entity.getCreatedByName())
                .approvedBy(entity.getApprovedBy())
                .approvedAt(entity.getApprovedAt())
                .templateStatus(entity.getTemplateStatus() != null ? entity.getTemplateStatus().name() : null)
                .usageCount(entity.getUsageCount())
                .lastUsedAt(entity.getLastUsedAt())
                .effectiveFrom(entity.getEffectiveFrom())
                .effectiveUntil(entity.getEffectiveUntil())
                .tags(entity.getTags())
                .linkedCalibrationSessions(entity.getLinkedCalibrationSessions())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convert CalibrationSession entity to DTO
     */
    public CalibrationSessionDto toDto(CalibrationSession entity) {
        if (entity == null) {
            return null;
        }

        return CalibrationSessionDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .sessionId(entity.getSessionId())
                .sessionName(entity.getSessionName())
                .sessionCode(entity.getSessionCode())
                .description(entity.getDescription())
                .sessionType(entity.getSessionType() != null ? entity.getSessionType().name() : null)
                .sessionStatus(entity.getSessionStatus() != null ? entity.getSessionStatus().name() : null)
                .facilitatorId(entity.getFacilitatorId())
                .facilitatorName(entity.getFacilitatorName())
                .scheduledDate(entity.getScheduledDate())
                .scheduledEndDate(entity.getScheduledEndDate())
                .actualStartDate(entity.getActualStartDate())
                .actualEndDate(entity.getActualEndDate())
                .durationMinutes(entity.getDurationMinutes())
                .participants(mapParticipants(entity.getParticipants()))
                .minParticipants(entity.getMinParticipants())
                .maxParticipants(entity.getMaxParticipants())
                .scorecardTemplateId(entity.getScorecardTemplateId())
                .scorecardTemplateName(entity.getScorecardTemplateName())
                .calibrationReviews(mapCalibrationReviews(entity.getCalibrationReviews()))
                .targetInteractionsCount(entity.getTargetInteractionsCount())
                .completedInteractionsCount(entity.getCompletedInteractionsCount())
                .averageScoreVariance(entity.getAverageScoreVariance())
                .maxScoreVariance(entity.getMaxScoreVariance())
                .interRaterReliability(entity.getInterRaterReliability())
                .calibrationScore(entity.getCalibrationScore())
                .calibrationPassed(entity.getCalibrationPassed())
                .calibrationThreshold(entity.getCalibrationThreshold())
                .findings(entity.getFindings())
                .actionItems(mapActionItems(entity.getActionItems()))
                .notes(entity.getNotes())
                .followUpRequired(entity.getFollowUpRequired())
                .followUpDate(entity.getFollowUpDate())
                .location(entity.getLocation())
                .isVirtual(entity.getIsVirtual())
                .meetingLink(entity.getMeetingLink())
                .tags(entity.getTags())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convert AgentQualityProfile entity to DTO
     */
    public AgentQualityProfileDto toDto(AgentQualityProfile entity) {
        if (entity == null) {
            return null;
        }

        return AgentQualityProfileDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .agentId(entity.getAgentId())
                .agentName(entity.getAgentName())
                .agentEmail(entity.getAgentEmail())
                .teamId(entity.getTeamId())
                .teamName(entity.getTeamName())
                .managerId(entity.getManagerId())
                .managerName(entity.getManagerName())
                .overallQualityScore(entity.getOverallQualityScore())
                .overallQualityRank(entity.getOverallQualityRank() != null ? entity.getOverallQualityRank().name() : null)
                .qualityTrend(entity.getQualityTrend() != null ? entity.getQualityTrend().name() : null)
                .rankInTeam(entity.getRankInTeam())
                .percentileInTeam(entity.getPercentileInTeam())
                .totalReviews(entity.getTotalReviews())
                .passedReviews(entity.getPassedReviews())
                .failedReviews(entity.getFailedReviews())
                .passRatePercentage(entity.getPassRatePercentage())
                .averageScore(entity.getAverageScore())
                .highestScore(entity.getHighestScore())
                .lowestScore(entity.getLowestScore())
                .scoreStandardDeviation(entity.getScoreStandardDeviation())
                .lastReviewDate(entity.getLastReviewDate())
                .lastReviewScore(entity.getLastReviewScore())
                .lastReviewPassed(entity.getLastReviewPassed())
                .currentStreak(entity.getCurrentStreak())
                .currentStreakType(entity.getCurrentStreakType() != null ? entity.getCurrentStreakType().name() : null)
                .longestPassingStreak(entity.getLongestPassingStreak())
                .longestFailingStreak(entity.getLongestFailingStreak())
                .monthlyAverageScore(entity.getMonthlyAverageScore())
                .monthlyReviewsCount(entity.getMonthlyReviewsCount())
                .monthlyPassRate(entity.getMonthlyPassRate())
                .quarterlyAverageScore(entity.getQuarterlyAverageScore())
                .quarterlyReviewsCount(entity.getQuarterlyReviewsCount())
                .quarterlyPassRate(entity.getQuarterlyPassRate())
                .yearlyAverageScore(entity.getYearlyAverageScore())
                .yearlyReviewsCount(entity.getYearlyReviewsCount())
                .yearlyPassRate(entity.getYearlyPassRate())
                .categoryPerformance(mapCategoryPerformance(entity.getCategoryPerformance()))
                .strengthAreas(entity.getStrengthAreas())
                .improvementAreas(entity.getImprovementAreas())
                .coachingRequired(entity.getCoachingRequired())
                .coachingPriority(entity.getCoachingPriority() != null ? entity.getCoachingPriority().name() : null)
                .coachingNotes(mapCoachingNotes(entity.getCoachingNotes()))
                .developmentPlanId(entity.getDevelopmentPlanId())
                .certificationsEarned(entity.getCertificationsEarned())
                .calibrationSessionsParticipated(entity.getCalibrationSessionsParticipated())
                .calibrationAverageVariance(entity.getCalibrationAverageVariance())
                .calibrationComplianceScore(entity.getCalibrationComplianceScore())
                .channelPerformance(mapChannelPerformance(entity.getChannelPerformance()))
                .scoreHistory(mapScoreSnapshots(entity.getScoreHistory()))
                .qualityGoal(entity.getQualityGoal())
                .goalProgressPercentage(entity.getGoalProgressPercentage())
                .goalTargetDate(entity.getGoalTargetDate())
                .goalAchieved(entity.getGoalAchieved())
                .qualityRiskLevel(entity.getQualityRiskLevel() != null ? entity.getQualityRiskLevel().name() : null)
                .riskFactors(entity.getRiskFactors())
                .profileLastCalculated(entity.getProfileLastCalculated())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convert to summary DTO
     */
    public QaReviewDto.QaReviewSummaryDto toSummaryDto(QaReview entity) {
        if (entity == null) {
            return null;
        }

        return QaReviewDto.QaReviewSummaryDto.builder()
                .reviewId(entity.getReviewId())
                .ticketId(entity.getTicketId())
                .agentId(entity.getAgentId())
                .agentName(entity.getAgentName())
                .reviewerName(entity.getReviewerName())
                .reviewType(entity.getReviewType() != null ? entity.getReviewType().name() : null)
                .reviewStatus(entity.getReviewStatus() != null ? entity.getReviewStatus().name() : null)
                .percentageScore(entity.getPercentageScore())
                .passed(entity.getPassed())
                .channelType(entity.getChannelType() != null ? entity.getChannelType().name() : null)
                .reviewDate(entity.getReviewDate())
                .dueDate(entity.getDueDate())
                .isOverdue(entity.isOverdue())
                .isCalibrated(entity.getIsCalibrated())
                .build();
    }

    /**
     * Convert to summary DTO
     */
    public AgentQualityProfileDto.AgentQualityProfileSummaryDto toSummaryDto(AgentQualityProfile entity) {
        if (entity == null) {
            return null;
        }

        return AgentQualityProfileDto.AgentQualityProfileSummaryDto.builder()
                .agentId(entity.getAgentId())
                .agentName(entity.getAgentName())
                .teamName(entity.getTeamName())
                .overallQualityScore(entity.getOverallQualityScore())
                .overallQualityRank(entity.getOverallQualityRank() != null ? entity.getOverallQualityRank().name() : null)
                .qualityTrend(entity.getQualityTrend() != null ? entity.getQualityTrend().name() : null)
                .totalReviews(entity.getTotalReviews())
                .passRatePercentage(entity.getPassRatePercentage())
                .qualityRiskLevel(entity.getQualityRiskLevel() != null ? entity.getQualityRiskLevel().name() : null)
                .coachingRequired(entity.getCoachingRequired())
                .coachingPriority(entity.getCoachingPriority() != null ? entity.getCoachingPriority().name() : null)
                .lastReviewDate(entity.getLastReviewDate())
                .build();
    }

    // Helper methods for mapping nested collections

    private List<QaReviewDto.CriteriaScoreDto> mapCriteriaScores(List<QaReview.CriteriaScore> scores) {
        if (scores == null) {
            return new ArrayList<>();
        }
        return scores.stream().map(s -> QaReviewDto.CriteriaScoreDto.builder()
                .criteriaId(s.getCriteriaId())
                .criteriaName(s.getCriteriaName())
                .category(s.getCategory())
                .score(s.getScore())
                .maxScore(s.getMaxScore())
                .weight(s.getWeight())
                .comments(s.getComments())
                .isCritical(s.getIsCritical())
                .passed(s.getPassed())
                .build()).collect(Collectors.toList());
    }

    private List<QaReview.CriteriaScore> mapCriteriaScoresDtos(List<QaReviewDto.CriteriaScoreDto> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }
        return dtos.stream().map(dto -> QaReview.CriteriaScore.builder()
                .criteriaId(dto.getCriteriaId())
                .criteriaName(dto.getCriteriaName())
                .category(dto.getCategory())
                .score(dto.getScore())
                .maxScore(dto.getMaxScore())
                .weight(dto.getWeight())
                .comments(dto.getComments())
                .isCritical(dto.getIsCritical())
                .passed(dto.getPassed())
                .build()).collect(Collectors.toList());
    }

    private List<ScorecardTemplateDto.CriteriaSectionDto> mapCriteriaSections(List<ScorecardTemplate.CriteriaSection> sections) {
        if (sections == null) {
            return new ArrayList<>();
        }
        return sections.stream().map(s -> ScorecardTemplateDto.CriteriaSectionDto.builder()
                .sectionId(s.getSectionId())
                .sectionName(s.getSectionName())
                .description(s.getDescription())
                .order(s.getOrder())
                .weight(s.getWeight())
                .maxScore(s.getMaxScore())
                .criteria(mapCriteria(s.getCriteria()))
                .isRequired(s.getIsRequired())
                .instructions(s.getInstructions())
                .build()).collect(Collectors.toList());
    }

    private List<ScorecardTemplateDto.CriteriaDto> mapCriteria(List<ScorecardTemplate.Criteria> criteria) {
        if (criteria == null) {
            return new ArrayList<>();
        }
        return criteria.stream().map(c -> ScorecardTemplateDto.CriteriaDto.builder()
                .criteriaId(c.getCriteriaId())
                .criteriaName(c.getCriteriaName())
                .description(c.getDescription())
                .categoryId(c.getCategoryId())
                .maxScore(c.getMaxScore())
                .weight(c.getWeight())
                .isCritical(c.getIsCritical())
                .isRequired(c.getIsRequired())
                .order(c.getOrder())
                .scoringGuidance(c.getScoringGuidance())
                .examples(c.getExamples())
                .redFlags(c.getRedFlags())
                .criteriaType(c.getCriteriaType() != null ? c.getCriteriaType().name() : null)
                .build()).collect(Collectors.toList());
    }

    private List<CalibrationSessionDto.ParticipantDto> mapParticipants(List<CalibrationSession.Participant> participants) {
        if (participants == null) {
            return new ArrayList<>();
        }
        return participants.stream().map(p -> CalibrationSessionDto.ParticipantDto.builder()
                .participantId(p.getParticipantId())
                .participantName(p.getParticipantName())
                .participantEmail(p.getParticipantEmail())
                .role(p.getRole() != null ? p.getRole().name() : null)
                .teamId(p.getTeamId())
                .teamName(p.getTeamName())
                .hasAttended(p.getHasAttended())
                .joinedAt(p.getJoinedAt())
                .leftAt(p.getLeftAt())
                .complianceScore(p.getComplianceScore())
                .build()).collect(Collectors.toList());
    }

    private List<CalibrationSessionDto.CalibrationReviewDto> mapCalibrationReviews(List<CalibrationSession.CalibrationReview> reviews) {
        if (reviews == null) {
            return new ArrayList<>();
        }
        return reviews.stream().map(r -> CalibrationSessionDto.CalibrationReviewDto.builder()
                .reviewId(r.getReviewId())
                .interactionId(r.getInteractionId())
                .ticketId(r.getTicketId())
                .reviewerScores(mapReviewerScores(r.getReviewerScores()))
                .averageScore(r.getAverageScore())
                .scoreVariance(r.getScoreVariance())
                .standardDeviation(r.getStandardDeviation())
                .reviewerCount(r.getReviewerCount())
                .isOutlier(r.getIsOutlier())
                .outlierReason(r.getOutlierReason())
                .build()).collect(Collectors.toList());
    }

    private List<CalibrationSessionDto.ReviewerScoreDto> mapReviewerScores(List<CalibrationSession.ReviewerScore> scores) {
        if (scores == null) {
            return new ArrayList<>();
        }
        return scores.stream().map(s -> CalibrationSessionDto.ReviewerScoreDto.builder()
                .reviewerId(s.getReviewerId())
                .reviewerName(s.getReviewerName())
                .score(s.getScore())
                .maxScore(s.getMaxScore())
                .percentageScore(s.getPercentageScore())
                .comments(s.getComments())
                .submittedAt(s.getSubmittedAt())
                .build()).collect(Collectors.toList());
    }

    private List<CalibrationSessionDto.ActionItemDto> mapActionItems(List<CalibrationSession.ActionItem> items) {
        if (items == null) {
            return new ArrayList<>();
        }
        return items.stream().map(i -> CalibrationSessionDto.ActionItemDto.builder()
                .actionItemId(i.getActionItemId())
                .description(i.getDescription())
                .assignedTo(i.getAssignedTo())
                .assignedToName(i.getAssignedToName())
                .status(i.getStatus() != null ? i.getStatus().name() : null)
                .dueDate(i.getDueDate())
                .completedAt(i.getCompletedAt())
                .notes(i.getNotes())
                .build()).collect(Collectors.toList());
    }

    private List<AgentQualityProfileDto.CategoryPerformanceDto> mapCategoryPerformance(List<AgentQualityProfile.CategoryPerformance> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream().map(cp -> AgentQualityProfileDto.CategoryPerformanceDto.builder()
                .category(cp.getCategory())
                .averageScore(cp.getAverageScore())
                .maxScore(cp.getMaxScore())
                .reviewCount(cp.getReviewCount())
                .trend(cp.getTrend() != null ? cp.getTrend().name() : null)
                .rank(cp.getRank())
                .performanceLevel(cp.getPerformanceLevel())
                .build()).collect(Collectors.toList());
    }

    private List<AgentQualityProfileDto.CoachingNoteDto> mapCoachingNotes(List<AgentQualityProfile.CoachingNote> notes) {
        if (notes == null) {
            return new ArrayList<>();
        }
        return notes.stream().map(n -> AgentQualityProfileDto.CoachingNoteDto.builder()
                .noteId(n.getNoteId())
                .note(n.getNote())
                .createdBy(n.getCreatedBy())
                .createdByName(n.getCreatedByName())
                .createdAt(n.getCreatedAt())
                .category(n.getCategory())
                .isActionable(n.getIsActionable())
                .build()).collect(Collectors.toList());
    }

    private List<AgentQualityProfileDto.ScoreSnapshotDto> mapScoreSnapshots(List<AgentQualityProfile.ScoreSnapshot> snapshots) {
        if (snapshots == null) {
            return new ArrayList<>();
        }
        return snapshots.stream().map(s -> AgentQualityProfileDto.ScoreSnapshotDto.builder()
                .reviewId(s.getReviewId())
                .score(s.getScore())
                .passed(s.getPassed())
                .reviewType(s.getReviewType())
                .reviewDate(s.getReviewDate())
                .channel(s.getChannel())
                .build()).collect(Collectors.toList());
    }

    private java.util.Map<String, AgentQualityProfileDto.ChannelStatsDto> mapChannelPerformance(java.util.Map<String, AgentQualityProfile.ChannelStats> channelPerformance) {
        if (channelPerformance == null) {
            return new java.util.HashMap<>();
        }
        java.util.Map<String, AgentQualityProfileDto.ChannelStatsDto> result = new java.util.HashMap<>();
        for (java.util.Map.Entry<String, AgentQualityProfile.ChannelStats> entry : channelPerformance.entrySet()) {
            AgentQualityProfile.ChannelStats stats = entry.getValue();
            AgentQualityProfileDto.ChannelStatsDto dto = AgentQualityProfileDto.ChannelStatsDto.builder()
                    .channel(stats.getChannel())
                    .reviewCount(stats.getReviewCount())
                    .averageScore(stats.getAverageScore())
                    .passRate(stats.getPassRate())
                    .trend(stats.getTrend() != null ? stats.getTrend().name() : null)
                    .build();
            result.put(entry.getKey(), dto);
        }
        return result;
    }
}
