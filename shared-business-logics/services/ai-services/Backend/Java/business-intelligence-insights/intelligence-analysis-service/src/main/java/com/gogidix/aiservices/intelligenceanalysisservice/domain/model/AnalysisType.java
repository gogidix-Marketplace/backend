package com.gogidix.aiservices.intelligenceanalysisservice.domain.model;

/**
 * Enum representing the type of IntelligenceAnalysis.
 */
public enum AnalysisType {
    /**
     * Based on customer behavior patterns.
     */
    BEHAVIORAL,

    /**
     * Based on demographic characteristics.
     */
    DEMOGRAPHIC,

    /**
     * Based on transaction history.
     */
    TRANSACTIONAL,

    /**
     * Custom defined segment.
     */
    CUSTOM
}
