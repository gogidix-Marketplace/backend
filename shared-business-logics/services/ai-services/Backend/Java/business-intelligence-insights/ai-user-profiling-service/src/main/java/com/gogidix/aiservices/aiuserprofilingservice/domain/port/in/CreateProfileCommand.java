package com.gogidix.aiservices.aiuserprofilingservice.domain.port.in;

import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria;

/**
 * Input port for creating a new customer segment.
 */
public interface CreateProfileCommand {

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
    String getProfileType();

    /**
     * Get the segment criteria.
     *
     * @return the criteria
     */
    ProfileCriteria getCriteria();

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
