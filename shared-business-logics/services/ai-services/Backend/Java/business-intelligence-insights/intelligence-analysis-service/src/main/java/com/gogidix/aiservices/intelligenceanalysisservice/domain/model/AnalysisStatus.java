package com.gogidix.aiservices.intelligenceanalysisservice.domain.model;

/**
 * Enum representing the status of a IntelligenceAnalysis.
 */
public enum AnalysisStatus {
    /**
     * Analysis is being created and not yet active.
     */
    DRAFT,

    /**
     * Analysis is active and used for targeting.
     */
    ACTIVE,

    /**
     * Analysis is temporarily disabled.
     */
    INACTIVE,

    /**
     * Analysis is archived and no longer in use.
     */
    ARCHIVED
}
