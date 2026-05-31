package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.repository;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.MarketBasket;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MarketBasket aggregates.
 * Defines the contract for segment persistence operations.
 * All implementations MUST filter by tenantId for multi-tenancy.
 */
public interface MarketBasketRepository {

    /**
     * Save a market basket.
     *
     * @param segment the segment to save
     * @return the saved segment
     */
    MarketBasket save(MarketBasket segment);

    /**
     * Find a segment by ID and tenant.
     *
     * @param id       the segment ID
     * @param tenantId the tenant ID
     * @return the segment if found
     */
    Optional<MarketBasket> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find all segments for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of segments
     */
    List<MarketBasket> findByTenantId(String tenantId);

    /**
     * Find segments by tenant and status.
     *
     * @param tenantId the tenant ID
     * @param status   the segment status
     * @return list of segments
     */
    List<MarketBasket> findByTenantIdAndStatus(String tenantId, com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketStatus status);

    /**
     * Find segments by tenant and type.
     *
     * @param tenantId    the tenant ID
     * @param segmentType the segment type
     * @return list of segments
     */
    List<MarketBasket> findByTenantIdAndBasketType(String tenantId,
                                                        com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType segmentType);

    /**
     * Find a segment by name and tenant.
     *
     * @param name     the segment name
     * @param tenantId the tenant ID
     * @return the segment if found
     */
    Optional<MarketBasket> findByNameAndTenantId(String name, String tenantId);

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
    List<MarketBasket> findByTenantIdPaginated(String tenantId, int page, int size);
}
