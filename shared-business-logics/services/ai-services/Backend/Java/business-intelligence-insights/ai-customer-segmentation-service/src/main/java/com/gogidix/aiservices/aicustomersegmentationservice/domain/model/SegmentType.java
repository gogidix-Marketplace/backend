package com.gogidix.aiservices.aicustomersegmentationservice.domain.model;

/**
 * Enum representing the type of CustomerSegment.
 */
public enum SegmentType {
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
