package com.gogidix.aiservices.aichurnpredictionservice.infrastructure.persistence.repository;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
import com.gogidix.aiservices.aichurnpredictionservice.infrastructure.persistence.document.ChurnPredictionDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for ChurnPrediction.
 */
@Repository
public interface SpringDataChurnPredictionRepository extends MongoRepository<ChurnPredictionDocument, String> {

    /**
     * Find segments by tenant ID with pagination.
     */
    Page<ChurnPredictionDocument> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find segments by tenant ID and segment type.
     */
    Page<ChurnPredictionDocument> findByTenantIdAndPredictionType(String tenantId, PredictionType segmentType, Pageable pageable);

    /**
     * Find segments by tenant ID and active status.
     */
    Page<ChurnPredictionDocument> findByTenantIdAndActive(String tenantId, boolean active, Pageable pageable);

    /**
     * Find segments by tenant ID, segment type, and active status.
     */
    Page<ChurnPredictionDocument> findByTenantIdAndPredictionTypeAndActive(
            String tenantId, PredictionType segmentType, boolean active, Pageable pageable);

    /**
     * Find active segments by tenant ID.
     */
    @Query("{ 'tenantId': ?0, 'active': true }")
    Page<ChurnPredictionDocument> findActiveByTenantId(String tenantId, Pageable pageable);

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
    Optional<ChurnPredictionDocument> findByIdAndTenantId(String id, String tenantId);

    /**
     * Delete by ID and tenant ID.
     */
    void deleteByIdAndTenantId(String id, String tenantId);
}
