package com.gogidix.aiservices.aiuserprofilingservice.domain.port.in;

/**
 * Input port for deleting a customer segment.
 */
public interface DeleteProfileCommand {

    /**
     * Get the segment ID to delete.
     *
     * @return the segment ID
     */
    String getProfileId();

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
