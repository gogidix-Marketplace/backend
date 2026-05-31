package com.gogidix.aiservices.aisalesforecastingservice.application.service;

import com.gogidix.aiservices.aisalesforecastingservice.application.dto.ForecastAnalysisResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.SalesForecast;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for analyzing sales forecasts.
 */
@Service
public class ForecastAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(ForecastAnalysisService.class);
    private static final String ANALYSIS_CACHE = "segmentAnalysis";

    /**
     * Analyze a sales forecast and return insights.
     */
    @Cacheable(value = ANALYSIS_CACHE, key = "#segment.id + ':' + #segment.version")
    public ForecastAnalysisResponseDto analyzeForecast(SalesForecast segment, Map<String, Object> options) {
        log.debug("Analyzing segment: {} with options: {}", segment.getId(), options);

        int customerCount = segment.getForecastModelCount().intValue();

        Map<String, Object> demographics = analyzeDemographics(segment);
        Map<String, Object> behaviors = analyzeBehaviors(segment);
        Map<String, Object> transactions = analyzeTransactions(segment);
        Map<String, Object> predictions = generatePredictions(segment);
        Map<String, Object> recommendations = generateRecommendations(segment);

        double confidenceScore = calculateConfidenceScore(segment);

        ForecastAnalysisResponseDto response = new ForecastAnalysisResponseDto(
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
    private Map<String, Object> analyzeDemographics(SalesForecast segment) {
        Map<String, Object> demographics = new HashMap<>();

        ForecastCriteria criteria = segment.getCriteria();
        demographics.put("field", criteria.getField());
        demographics.put("operator", criteria.getOperator().name());
        demographics.put("customerCount", segment.getForecastModelCount());
        demographics.put("analyzedAt", Instant.now().toString());

        return demographics;
    }

    /**
     * Analyze behavioral patterns of the segment.
     */
    private Map<String, Object> analyzeBehaviors(SalesForecast segment) {
        Map<String, Object> behaviors = new HashMap<>();

        ForecastCriteria criteria = segment.getCriteria();
        behaviors.put("criteriaField", criteria.getField());
        behaviors.put("criteriaValue", criteria.getValue());

        return behaviors;
    }

    /**
     * Analyze transaction metrics of the segment.
     */
    private Map<String, Object> analyzeTransactions(SalesForecast segment) {
        Map<String, Object> transactions = new HashMap<>();

        transactions.put("customerCount", segment.getForecastModelCount());
        transactions.put("lastAnalyzed", segment.getLastAnalyzedAt() != null ? segment.getLastAnalyzedAt().toString() : "Never");

        return transactions;
    }

    /**
     * Generate predictive insights for the segment.
     */
    private Map<String, Object> generatePredictions(SalesForecast segment) {
        Map<String, Object> predictions = new HashMap<>();

        int customerCount = segment.getForecastModelCount().intValue();

        predictions.put("churnRisk", customerCount > 100 ? "low" : "variable");
        predictions.put("upsellPotential", "medium");
        predictions.put("predictedGrowthRate", 0.05);

        return predictions;
    }

    /**
     * Generate recommendations for segment optimization.
     */
    private Map<String, Object> generateRecommendations(SalesForecast segment) {
        Map<String, Object> recommendations = new HashMap<>();

        int customerCount = segment.getForecastModelCount().intValue();

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
    private double calculateConfidenceScore(SalesForecast segment) {
        int customerCount = segment.getForecastModelCount().intValue();

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
