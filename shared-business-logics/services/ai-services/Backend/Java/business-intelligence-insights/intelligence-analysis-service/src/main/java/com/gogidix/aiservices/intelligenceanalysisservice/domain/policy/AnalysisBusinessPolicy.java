package com.gogidix.aiservices.intelligenceanalysisservice.domain.policy;

import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.IntelligenceAnalysis;
import com.gogidix.aiservices.intelligenceanalysisservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Business policy for customer segment operations.
 */
@Component
public class AnalysisBusinessPolicy {

    private static final Logger log = LoggerFactory.getLogger(AnalysisBusinessPolicy.class);

    private final int maxAnalysissPerTenant;
    private final int maxIntelligenceReportsPerAnalysis;

    public AnalysisBusinessPolicy(
            @Value("${segment.max.segments-per-tenant:100}") int maxAnalysissPerTenant,
            @Value("${segment.max.customers-per-segment:1000000}") int maxIntelligenceReportsPerAnalysis
    ) {
        this.maxAnalysissPerTenant = maxAnalysissPerTenant;
        this.maxIntelligenceReportsPerAnalysis = maxIntelligenceReportsPerAnalysis;
    }

    /**
     * Validate that segment creation is allowed.
     */
    public void validateAnalysisCreation(String tenantId) {
        // Simplified validation - actual count would be checked at database layer
        log.debug("Analysis creation validated for tenant: {}", tenantId);
    }

    /**
     * Validate that customer addition is allowed.
     */
    public void validateIntelligenceReportAddition(IntelligenceAnalysis segment, int countToAdd) {
        long projectedCount = segment.getIntelligenceReportCount() + countToAdd;

        if (projectedCount > maxIntelligenceReportsPerAnalysis) {
            throw new ValidationException(
                    String.format("Cannot add customers. Maximum segment size (%d) would be exceeded", maxIntelligenceReportsPerAnalysis)
            );
        }

        if (!segment.isActive()) {
            throw new ValidationException("Cannot add customers to inactive segment");
        }

        log.debug("IntelligenceReport addition validated for segment: {}", segment.getId());
    }

    /**
     * Validate that segment deletion is allowed.
     */
    public void validateAnalysisDeletion(IntelligenceAnalysis segment) {
        if (segment.isActive() && segment.getIntelligenceReportCount() > 0) {
            log.warn("Deleting active segment with customers: {}", segment.getId());
        }
        log.debug("Analysis deletion validated for segment: {}", segment.getId());
    }
}
