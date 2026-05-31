package com.gogidix.aiservices.aicustomersegmentationservice.domain.model;

/**
 * Enum representing the status of a CustomerSegment.
 */
public enum SegmentStatus {
    /**
     * Segment is being created and not yet active.
     */
    DRAFT,

    /**
     * Segment is active and used for targeting.
     */
    ACTIVE,

    /**
     * Segment is temporarily disabled.
     */
    INACTIVE,

    /**
     * Segment is archived and no longer in use.
     */
    ARCHIVED
}
