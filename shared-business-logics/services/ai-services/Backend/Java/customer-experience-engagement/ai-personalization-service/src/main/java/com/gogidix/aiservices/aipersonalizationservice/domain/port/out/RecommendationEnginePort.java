package com.gogidix.aiservices.aipersonalizationservice.domain.port.out;

import com.gogidix.aiservices.aipersonalizationservice.domain.aggregate.UserProfile;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.Recommendation;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.Segment;

import java.util.List;
import java.util.Map;

public interface RecommendationEnginePort {
    List<Recommendation> generateRecommendations(UserProfile profile, RecommendationContext context);
    Map<String, Double> calculateAffinityScores(UserProfile profile);
    Segment predictSegment(UserProfile profile);

    record RecommendationContext(int limit, String type, String context) {}
}
