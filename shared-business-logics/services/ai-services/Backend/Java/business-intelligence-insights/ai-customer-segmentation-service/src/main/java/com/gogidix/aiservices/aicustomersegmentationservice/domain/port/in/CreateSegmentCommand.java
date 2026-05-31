package com.gogidix.aiservices.aicustomersegmentationservice.domain.port.in;

import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria;

/**
 * Input port for creating a new customer segment.
 */
public interface CreateSegmentCommand {

    /**
     * Get the segment name.
     *
     * @return the name
     */
    String getName();

    /**
     * Get the segment description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Get the segment type.
     *
     * @return the segment type
     */
    String getSegmentType();

    /**
     * Get the segment criteria.
     *
     * @return the criteria
     */
    SegmentCriteria getCriteria();

    /**
     * Get the tenant ID from context.
     *
     * @return the tenant ID
     */
    String getTenantId();

    /**
     * Get the user ID from context.
     *
     * @return the user ID
     */
    String getUserId();
}
