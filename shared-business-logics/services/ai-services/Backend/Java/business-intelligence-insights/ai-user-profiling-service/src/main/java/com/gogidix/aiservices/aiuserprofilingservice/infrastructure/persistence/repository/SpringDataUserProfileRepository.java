package com.gogidix.aiservices.aiuserprofilingservice.infrastructure.persistence.repository;

import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType;
import com.gogidix.aiservices.aiuserprofilingservice.infrastructure.persistence.document.UserProfileDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for UserProfile.
 */
@Repository
public interface SpringDataUserProfileRepository extends MongoRepository<UserProfileDocument, String> {

    /**
     * Find segments by tenant ID with pagination.
     */
    Page<UserProfileDocument> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find segments by tenant ID and segment type.
     */
    Page<UserProfileDocument> findByTenantIdAndProfileType(String tenantId, ProfileType segmentType, Pageable pageable);

    /**
     * Find segments by tenant ID and active status.
     */
    Page<UserProfileDocument> findByTenantIdAndActive(String tenantId, boolean active, Pageable pageable);

    /**
     * Find segments by tenant ID, segment type, and active status.
     */
    Page<UserProfileDocument> findByTenantIdAndProfileTypeAndActive(
            String tenantId, ProfileType segmentType, boolean active, Pageable pageable);

    /**
     * Find active segments by tenant ID.
     */
    @Query("{ 'tenantId': ?0, 'active': true }")
    Page<UserProfileDocument> findActiveByTenantId(String tenantId, Pageable pageable);

    /**
     * Count segments by tenant ID.
     */
    long countByTenantId(String tenantId);

    /**
     * Count active segments by tenant ID.
     */
    long countByTenantIdAndActive(String tenantId, boolean active);

    /**
     * Check if segment exists by ID and tenant ID.
     */
    boolean existsByIdAndTenantId(String id, String tenantId);

    /**
     * Find by ID and tenant ID.
     */
    Optional<UserProfileDocument> findByIdAndTenantId(String id, String tenantId);

    /**
     * Delete by ID and tenant ID.
     */
    void deleteByIdAndTenantId(String id, String tenantId);
}
