package com.gogidix.aiservices.aicustomersegmentationservice.domain.port.in;

/**
 * Input port for deleting a customer segment.
 */
public interface DeleteSegmentCommand {

    /**
     * Get the segment ID to delete.
     *
     * @return the segment ID
     */
    String getSegmentId();

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
