package com.gogidix.aiservices.aiuserprofilingservice.domain.port.in;

/**
 * Input port for querying customer segments.
 */
public interface GetProfileQuery {

    /**
     * Get the segment ID to query.
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
}
