package com.gogidix.aiservices.aicustomersegmentationservice.infrastructure.persistence.repository;

import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentType;
import com.gogidix.aiservices.aicustomersegmentationservice.infrastructure.persistence.document.CustomerSegmentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for CustomerSegment.
 */
@Repository
public interface SpringDataCustomerSegmentRepository extends MongoRepository<CustomerSegmentDocument, String> {

    /**
     * Find segments by tenant ID with pagination.
     */
    Page<CustomerSegmentDocument> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find segments by tenant ID and segment type.
     */
    Page<CustomerSegmentDocument> findByTenantIdAndSegmentType(String tenantId, SegmentType segmentType, Pageable pageable);

    /**
     * Find segments by tenant ID and active status.
     */
    Page<CustomerSegmentDocument> findByTenantIdAndActive(String tenantId, boolean active, Pageable pageable);

    /**
     * Find segments by tenant ID, segment type, and active status.
     */
    Page<CustomerSegmentDocument> findByTenantIdAndSegmentTypeAndActive(
            String tenantId, SegmentType segmentType, boolean active, Pageable pageable);

    /**
     * Find active segments by tenant ID.
     */
    @Query("{ 'tenantId': ?0, 'active': true }")
    Page<CustomerSegmentDocument> findActiveByTenantId(String tenantId, Pageable pageable);

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
    Optional<CustomerSegmentDocument> findByIdAndTenantId(String id, String tenantId);

    /**
     * Delete by ID and tenant ID.
     */
    void deleteByIdAndTenantId(String id, String tenantId);
}
