package com.gogidix.aiservices.leadgenerationaiservice.domain.port.out;

import com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate.Lead;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadScore;

import java.util.List;
import java.util.Map;

public interface LeadScoringEnginePort {
    LeadScore calculateScore(Lead lead);
    List<LeadScore> calculateScores(List<Lead> leads);
    Lead enrichLead(Lead lead);

    record ConversionPrediction(double probability, String expectedDate, double confidence) {}
    record RecommendationContext(String campaign, String segment, int limit) {
        public static RecommendationContext defaultContext() {
            return new RecommendationContext("default", "all", 10);
        }
    }
}
