package com.gogidix.aiservices.aiproductrecommendationservice.infrastructure.persistence.repository;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import com.gogidix.aiservices.aiproductrecommendationservice.infrastructure.persistence.document.ProductRecommendationDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for ProductRecommendation.
 */
@Repository
public interface SpringDataProductRecommendationRepository extends MongoRepository<ProductRecommendationDocument, String> {

    /**
     * Find segments by tenant ID with pagination.
     */
    Page<ProductRecommendationDocument> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find segments by tenant ID and segment type.
     */
    Page<ProductRecommendationDocument> findByTenantIdAndRecommendationType(String tenantId, RecommendationType segmentType, Pageable pageable);

    /**
     * Find segments by tenant ID and active status.
     */
    Page<ProductRecommendationDocument> findByTenantIdAndActive(String tenantId, boolean active, Pageable pageable);

    /**
     * Find segments by tenant ID, segment type, and active status.
     */
    Page<ProductRecommendationDocument> findByTenantIdAndRecommendationTypeAndActive(
            String tenantId, RecommendationType segmentType, boolean active, Pageable pageable);

    /**
     * Find active segments by tenant ID.
     */
    @Query("{ 'tenantId': ?0, 'active': true }")
    Page<ProductRecommendationDocument> findActiveByTenantId(String tenantId, Pageable pageable);

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
    Optional<ProductRecommendationDocument> findByIdAndTenantId(String id, String tenantId);

    /**
     * Delete by ID and tenant ID.
     */
    void deleteByIdAndTenantId(String id, String tenantId);
}
