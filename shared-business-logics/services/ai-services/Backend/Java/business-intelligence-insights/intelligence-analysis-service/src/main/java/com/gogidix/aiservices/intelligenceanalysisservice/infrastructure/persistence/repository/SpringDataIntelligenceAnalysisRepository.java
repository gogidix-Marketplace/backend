package com.gogidix.aiservices.intelligenceanalysisservice.infrastructure.persistence.repository;

import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisType;
import com.gogidix.aiservices.intelligenceanalysisservice.infrastructure.persistence.document.IntelligenceAnalysisDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for IntelligenceAnalysis.
 */
@Repository
public interface SpringDataIntelligenceAnalysisRepository extends MongoRepository<IntelligenceAnalysisDocument, String> {

    /**
     * Find segments by tenant ID with pagination.
     */
    Page<IntelligenceAnalysisDocument> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find segments by tenant ID and segment type.
     */
    Page<IntelligenceAnalysisDocument> findByTenantIdAndAnalysisType(String tenantId, AnalysisType segmentType, Pageable pageable);

    /**
     * Find segments by tenant ID and active status.
     */
    Page<IntelligenceAnalysisDocument> findByTenantIdAndActive(String tenantId, boolean active, Pageable pageable);

    /**
     * Find segments by tenant ID, segment type, and active status.
     */
    Page<IntelligenceAnalysisDocument> findByTenantIdAndAnalysisTypeAndActive(
            String tenantId, AnalysisType segmentType, boolean active, Pageable pageable);

    /**
     * Find active segments by tenant ID.
     */
    @Query("{ 'tenantId': ?0, 'active': true }")
    Page<IntelligenceAnalysisDocument> findActiveByTenantId(String tenantId, Pageable pageable);

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
    Optional<IntelligenceAnalysisDocument> findByIdAndTenantId(String id, String tenantId);

    /**
     * Delete by ID and tenant ID.
     */
    void deleteByIdAndTenantId(String id, String tenantId);
}
