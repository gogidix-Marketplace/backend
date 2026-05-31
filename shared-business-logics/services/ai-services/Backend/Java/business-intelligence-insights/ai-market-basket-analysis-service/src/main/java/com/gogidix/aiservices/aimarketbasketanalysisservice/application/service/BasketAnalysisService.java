package com.gogidix.aiservices.aimarketbasketanalysisservice.application.service;

import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.BasketAnalysisResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.MarketBasket;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for analyzing market baskets.
 */
@Service
public class BasketAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(BasketAnalysisService.class);
    private static final String ANALYSIS_CACHE = "segmentAnalysis";

    /**
     * Analyze a market basket and return insights.
     */
    @Cacheable(value = ANALYSIS_CACHE, key = "#segment.id + ':' + #segment.version")
    public BasketAnalysisResponseDto analyzeBasket(MarketBasket segment, Map<String, Object> options) {
        log.debug("Analyzing segment: {} with options: {}", segment.getId(), options);

        int customerCount = segment.getCustomerCount().intValue();

        Map<String, Object> demographics = analyzeDemographics(segment);
        Map<String, Object> behaviors = analyzeBehaviors(segment);
        Map<String, Object> transactions = analyzeTransactions(segment);
        Map<String, Object> predictions = generatePredictions(segment);
        Map<String, Object> recommendations = generateRecommendations(segment);

        double confidenceScore = calculateConfidenceScore(segment);

        BasketAnalysisResponseDto response = new BasketAnalysisResponseDto(
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
    private Map<String, Object> analyzeDemographics(MarketBasket segment) {
        Map<String, Object> demographics = new HashMap<>();

        BasketCriteria criteria = segment.getCriteria();
        demographics.put("field", criteria.getField());
        demographics.put("operator", criteria.getOperator().name());
        demographics.put("customerCount", segment.getCustomerCount());
        demographics.put("analyzedAt", Instant.now().toString());

        return demographics;
    }

    /**
     * Analyze behavioral patterns of the segment.
     */
    private Map<String, Object> analyzeBehaviors(MarketBasket segment) {
        Map<String, Object> behaviors = new HashMap<>();

        BasketCriteria criteria = segment.getCriteria();
        behaviors.put("criteriaField", criteria.getField());
        behaviors.put("criteriaValue", criteria.getValue());

        return behaviors;
    }

    /**
     * Analyze transaction metrics of the segment.
     */
    private Map<String, Object> analyzeTransactions(MarketBasket segment) {
        Map<String, Object> transactions = new HashMap<>();

        transactions.put("customerCount", segment.getCustomerCount());
        transactions.put("lastAnalyzed", segment.getLastAnalyzedAt() != null ? segment.getLastAnalyzedAt().toString() : "Never");

        return transactions;
    }

    /**
     * Generate predictive insights for the segment.
     */
    private Map<String, Object> generatePredictions(MarketBasket segment) {
        Map<String, Object> predictions = new HashMap<>();

        int customerCount = segment.getCustomerCount().intValue();

        predictions.put("churnRisk", customerCount > 100 ? "low" : "variable");
        predictions.put("upsellPotential", "medium");
        predictions.put("predictedGrowthRate", 0.05);

        return predictions;
    }

    /**
     * Generate recommendations for segment optimization.
     */
    private Map<String, Object> generateRecommendations(MarketBasket segment) {
        Map<String, Object> recommendations = new HashMap<>();

        int customerCount = segment.getCustomerCount().intValue();

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
    private double calculateConfidenceScore(MarketBasket segment) {
        int customerCount = segment.getCustomerCount().intValue();

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
