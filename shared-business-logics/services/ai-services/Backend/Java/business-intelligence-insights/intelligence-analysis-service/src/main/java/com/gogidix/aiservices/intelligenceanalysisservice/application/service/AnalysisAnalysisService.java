package com.gogidix.aiservices.intelligenceanalysisservice.application.service;

import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.AnalysisAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.IntelligenceAnalysis;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for analyzing customer segments.
 */
@Service
public class AnalysisAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisAnalysisService.class);
    private static final String ANALYSIS_CACHE = "segmentAnalysis";

    /**
     * Analyze a customer segment and return insights.
     */
    @Cacheable(value = ANALYSIS_CACHE, key = "#segment.id + ':' + #segment.version")
    public AnalysisAnalysisResponseDto analyzeAnalysis(IntelligenceAnalysis segment, Map<String, Object> options) {
        log.debug("Analyzing segment: {} with options: {}", segment.getId(), options);

        int customerCount = segment.getIntelligenceReportCount().intValue();

        Map<String, Object> demographics = analyzeDemographics(segment);
        Map<String, Object> behaviors = analyzeBehaviors(segment);
        Map<String, Object> transactions = analyzeTransactions(segment);
        Map<String, Object> predictions = generatePredictions(segment);
        Map<String, Object> recommendations = generateRecommendations(segment);

        double confidenceScore = calculateConfidenceScore(segment);

        AnalysisAnalysisResponseDto response = new AnalysisAnalysisResponseDto(
                segment.getId(),
                Instant.now(),
                customerCount,
                demographics,
                behaviors,
                transactions,
                predictions,
                recommendations,
                confidenceScore
        );

        log.info("Analysis completed for segment: {} with confidence: {}", segment.getId(), confidenceScore);

        return response;
    }

    /**
     * Analyze demographic characteristics of the segment.
     */
    private Map<String, Object> analyzeDemographics(IntelligenceAnalysis segment) {
        Map<String, Object> demographics = new HashMap<>();

        AnalysisCriteria criteria = segment.getCriteria();
        demographics.put("field", criteria.getField());
        demographics.put("operator", criteria.getOperator().name());
        demographics.put("customerCount", segment.getIntelligenceReportCount());
        demographics.put("analyzedAt", Instant.now().toString());

        return demographics;
    }

    /**
     * Analyze behavioral patterns of the segment.
     */
    private Map<String, Object> analyzeBehaviors(IntelligenceAnalysis segment) {
        Map<String, Object> behaviors = new HashMap<>();

        AnalysisCriteria criteria = segment.getCriteria();
        behaviors.put("criteriaField", criteria.getField());
        behaviors.put("criteriaValue", criteria.getValue());

        return behaviors;
    }

    /**
     * Analyze transaction metrics of the segment.
     */
    private Map<String, Object> analyzeTransactions(IntelligenceAnalysis segment) {
        Map<String, Object> transactions = new HashMap<>();

        transactions.put("customerCount", segment.getIntelligenceReportCount());
        transactions.put("lastAnalyzed", segment.getLastAnalyzedAt() != null ? segment.getLastAnalyzedAt().toString() : "Never");

        return transactions;
    }

    /**
     * Generate predictive insights for the segment.
     */
    private Map<String, Object> generatePredictions(IntelligenceAnalysis segment) {
        Map<String, Object> predictions = new HashMap<>();

        int customerCount = segment.getIntelligenceReportCount().intValue();

        predictions.put("churnRisk", customerCount > 100 ? "low" : "variable");
        predictions.put("upsellPotential", "medium");
        predictions.put("predictedGrowthRate", 0.05);

        return predictions;
    }

    /**
     * Generate recommendations for segment optimization.
     */
    private Map<String, Object> generateRecommendations(IntelligenceAnalysis segment) {
        Map<String, Object> recommendations = new HashMap<>();

        int customerCount = segment.getIntelligenceReportCount().intValue();

        recommendations.put("optimalContactFrequency", "monthly");
        recommendations.put("recommendedOfferType", "percentage_discount");

        if (customerCount < 100) {
            recommendations.put("expansionSuggestion", "Consider expanding criteria");
        } else if (customerCount > 100000) {
            recommendations.put("refinementSuggestion", "Consider narrowing criteria");
        }

        return recommendations;
    }

    /**
     * Calculate the confidence score of the analysis.
     */
    private double calculateConfidenceScore(IntelligenceAnalysis segment) {
        int customerCount = segment.getIntelligenceReportCount().intValue();

        if (customerCount < 10) {
            return 0.3;
        } else if (customerCount < 100) {
            return 0.6;
        } else if (customerCount < 1000) {
            return 0.8;
        } else {
            return 0.95;
        }
    }
}
