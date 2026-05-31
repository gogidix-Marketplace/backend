package com.gogidix.aiservices.aiuserprofilingservice.domain.repository;

import com.gogidix.aiservices.aiuserprofilingservice.domain.model.UserProfile;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserProfile aggregates.
 * Defines the contract for segment persistence operations.
 * All implementations MUST filter by tenantId for multi-tenancy.
 */
public interface UserProfileRepository {

    /**
     * Save a customer segment.
     *
     * @param segment the segment to save
     * @return the saved segment
     */
    UserProfile save(UserProfile segment);

    /**
     * Find a segment by ID and tenant.
     *
     * @param id       the segment ID
     * @param tenantId the tenant ID
     * @return the segment if found
     */
    Optional<UserProfile> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find all segments for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of segments
     */
    List<UserProfile> findByTenantId(String tenantId);

    /**
     * Find segments by tenant and status.
     *
     * @param tenantId the tenant ID
     * @param status   the segment status
     * @return list of segments
     */
    List<UserProfile> findByTenantIdAndStatus(String tenantId, com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileStatus status);

    /**
     * Find segments by tenant and type.
     *
     * @param tenantId    the tenant ID
     * @param segmentType the segment type
     * @return list of segments
     */
    List<UserProfile> findByTenantIdAndProfileType(String tenantId,
                                                        com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType segmentType);

    /**
     * Find a segment by name and tenant.
     *
     * @param name     the segment name
     * @param tenantId the tenant ID
     * @return the segment if found
     */
    Optional<UserProfile> findByNameAndTenantId(String name, String tenantId);

    /**
     * Count segments by tenant.
     *
     * @param tenantId the tenant ID
     * @return the count
     */
    long countByTenantId(String tenantId);

    /**
     * Check if a segment exists by ID and tenant.
     *
     * @param id       the segment ID
     * @param tenantId the tenant ID
     * @return true if exists, false otherwise
     */
    boolean existsByIdAndTenantId(String id, String tenantId);

    /**
     * Delete a segment by ID and tenant.
     *
     * @param id       the segment ID
     * @param tenantId the tenant ID
     */
    void deleteByIdAndTenantId(String id, String tenantId);

    /**
     * Find all segment names for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of segment names
     */
    List<String> findAllNamesByTenantId(String tenantId);

    /**
     * Find segments by tenant with pagination.
     *
     * @param tenantId the tenant ID
     * @param page      the page number (0-indexed)
     * @param size      the page size
     * @return list of segments
     */
    List<UserProfile> findByTenantIdPaginated(String tenantId, int page, int size);
}
