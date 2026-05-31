package com.gogidix.aiservices.aimarketbasketanalysisservice.infrastructure.persistence.repository;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
import com.gogidix.aiservices.aimarketbasketanalysisservice.infrastructure.persistence.document.MarketBasketDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for MarketBasket.
 */
@Repository
public interface SpringDataMarketBasketRepository extends MongoRepository<MarketBasketDocument, String> {

    /**
     * Find segments by tenant ID with pagination.
     */
    Page<MarketBasketDocument> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find segments by tenant ID and segment type.
     */
    Page<MarketBasketDocument> findByTenantIdAndBasketType(String tenantId, BasketType segmentType, Pageable pageable);

    /**
     * Find segments by tenant ID and active status.
     */
    Page<MarketBasketDocument> findByTenantIdAndActive(String tenantId, boolean active, Pageable pageable);

    /**
     * Find segments by tenant ID, segment type, and active status.
     */
    Page<MarketBasketDocument> findByTenantIdAndBasketTypeAndActive(
            String tenantId, BasketType segmentType, boolean active, Pageable pageable);

    /**
     * Find active segments by tenant ID.
     */
    @Query("{ 'tenantId': ?0, 'active': true }")
    Page<MarketBasketDocument> findActiveByTenantId(String tenantId, Pageable pageable);

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
    Optional<MarketBasketDocument> findByIdAndTenantId(String id, String tenantId);

    /**
     * Delete by ID and tenant ID.
     */
    void deleteByIdAndTenantId(String id, String tenantId);
}
