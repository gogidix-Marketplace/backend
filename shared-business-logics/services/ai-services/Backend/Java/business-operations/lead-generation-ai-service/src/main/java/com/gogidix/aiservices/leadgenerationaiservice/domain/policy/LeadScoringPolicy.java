package com.gogidix.aiservices.leadgenerationaiservice.domain.policy;

import com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate.Lead;
import com.gogidix.aiservices.leadgenerationaiservice.domain.event.LeadActivity;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.*;

import java.time.Duration;
import java.time.Instant;

public class LeadScoringPolicy {

    private static final int STALE_DAYS_THRESHOLD = 30;
    private static final double TIME_DECAY_FACTOR = 0.1;
    private static final double HIGH_PRIORITY_THRESHOLD = 70.0;
    private static final double LOW_PRIORITY_THRESHOLD = 40.0;

    public double calculateBaseScore(Lead lead) {
        double baseScore = 50.0;

        if (lead.getSource() != null) {
            baseScore += scoreSource(lead.getSource()) * 0.3;
        }

        baseScore += scoreEngagement(lead);
        baseScore += scoreQualification(lead);
        baseScore += scoreDemographics(lead);

        return Math.min(100, baseScore);
    }

    public double scoreSource(LeadSource source) {
        if (source == null) {
            return 0.0;
        }
        return source.getQualityScore();
    }

    public double scoreEngagement(Lead lead) {
        double score = 0.0;
        int recentActivityWindow = 7; // days

        for (LeadActivity activity : lead.getActivities()) {
            double age = Duration.between(activity.getTimestamp(), Instant.now()).toDays();

            if (age <= recentActivityWindow) {
                if (activity.getType().isEngagement()) {
                    score += 3.0;
                } else if (activity.getType().isOutreach()) {
                    score += 1.0;
                }
            } else if (age <= 30) {
                score += 1.0;
            }
        }

        return Math.min(20.0, score);
    }

    public double scoreQualification(Lead lead) {
        int criteriaCount = lead.getQualificationCriteria().size();
        return Math.min(30.0, criteriaCount * 5.0);
    }

    public double scoreDemographics(Lead lead) {
        double score = 0.0;

        if (lead.getContactInfo().getCompanySize() != null) {
            score += switch (lead.getContactInfo().getCompanySize()) {
                case ENTERPRISE -> 15.0;
                case LARGE -> 12.0;
                case MEDIUM_BUSINESS -> 8.0;
                case SMALL_BUSINESS -> 5.0;
                default -> 2.0;
            };
        }

        if (lead.getContactInfo().isDecisionMaker()) {
            score += 10.0;
        }

        if (lead.getContactInfo().hasCompanyInfo()) {
            score += 3.0;
        }

        if (lead.getContactInfo().getIndustry() != null) {
            score += 2.0;
        }

        return Math.min(20.0, score);
    }

    public double applyTimeDecay(double score, Lead lead) {
        int ageInDays = lead.getAgeInDays();
        if (ageInDays <= STALE_DAYS_THRESHOLD) {
            return score;
        }

        double decayFactor = 1.0 - (ageInDays - STALE_DAYS_THRESHOLD) * TIME_DECAY_FACTOR / 100;
        return Math.max(0, score * decayFactor);
    }

    public LeadScore calculateScore(Lead lead) {
        double rawScore = calculateBaseScore(lead);
        double decayedScore = applyTimeDecay(rawScore, lead);

        LeadScore.LeadScoreBuilder builder = LeadScore.builder()
                .leadId(lead.getLeadId().toString())
                .score(decayedScore);

        return builder.build();
    }

    public boolean isHighPriority(Lead lead) {
        LeadScore score = calculateScore(lead);
        return score.getScore() >= HIGH_PRIORITY_THRESHOLD;
    }

    public boolean isLowPriority(Lead lead) {
        LeadScore score = calculateScore(lead);
        return score.getScore() < LOW_PRIORITY_THRESHOLD;
    }

    public boolean shouldEscalate(Lead lead) {
        return lead.isHighQuality() && lead.isStale();
    }
}
