package com.gogidix.aiservices.aicustomersegmentationservice.domain.port.in;

/**
 * Input port for updating an existing customer segment.
 */
public interface UpdateSegmentCommand {

    /**
     * Get the segment ID to update.
     *
     * @return the segment ID
     */
    String getSegmentId();

    /**
     * Get the new segment name.
     *
     * @return the name
     */
    String getName();

    /**
     * Get the new segment description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Get the updated segment criteria.
     *
     * @return the criteria
     */
    com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria getCriteria();

    /**
     * Get the new status.
     *
     * @return the status
     */
    String getStatus();

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
