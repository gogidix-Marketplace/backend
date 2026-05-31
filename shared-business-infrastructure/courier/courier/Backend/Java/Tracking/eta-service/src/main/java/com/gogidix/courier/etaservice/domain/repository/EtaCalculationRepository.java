package com.gogidix.courier.etaservice.domain.repository;

import com.gogidix.courier.etaservice.domain.entity.EtaCalculation;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EtaCalculation aggregates.
 * Defines the contract for ETA calculation persistence operations.
 * All implementations MUST filter by tenantId for multi-tenancy.
 */
public interface EtaCalculationRepository {

    /**
     * Save an ETA calculation.
     *
     * @param calculation the calculation to save
     * @return the saved calculation
     */
    EtaCalculation save(EtaCalculation calculation);

    /**
     * Find an ETA calculation by ID.
     *
     * @param id the calculation ID
     * @return the calculation if found
     */
    Optional<EtaCalculation> findById(String id);

    /**
     * Find an ETA calculation by dispatch ID.
     *
     * @param dispatchId the dispatch ID
     * @return the calculation if found
     */
    Optional<EtaCalculation> findByDispatchId(String dispatchId);

    /**
     * Find an ETA calculation by dispatch ID and tenant ID.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @return the calculation if found
     */
    Optional<EtaCalculation> findByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Find all ETA calculations for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of calculations
     */
    List<EtaCalculation> findByTenantId(String tenantId);

    /**
     * Find all ETA calculations.
     *
     * @return list of all calculations
     */
    List<EtaCalculation> findAll();

    /**
     * Find ETA calculations by status.
     *
     * @param status the ETA status
     * @return list of calculations
     */
    List<EtaCalculation> findByStatus(EtaCalculation.EtaStatus status);

    /**
     * Find ETA calculations by status and tenant.
     *
     * @param status   the ETA status
     * @param tenantId the tenant ID
     * @return list of calculations
     */
    List<EtaCalculation> findByStatusAndTenantId(EtaCalculation.EtaStatus status, String tenantId);

    /**
     * Find active (in-transit) calculations for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of active calculations
     */
    List<EtaCalculation> findActiveByTenantId(String tenantId);

    /**
     * Find calculations with estimated arrival before a given time.
     *
     * @param before the time threshold
     * @param tenantId the tenant ID
     * @return list of calculations
     */
    List<EtaCalculation> findByEstimatedArrivalBeforeAndTenantId(Instant before, String tenantId);

    /**
     * Find calculations with estimated arrival after a given time.
     *
     * @param after the time threshold
     * @param tenantId the tenant ID
     * @return list of calculations
     */
    List<EtaCalculation> findByEstimatedArrivalAfterAndTenantId(Instant after, String tenantId);

    /**
     * Check if a calculation exists for a dispatch.
     *
     * @param dispatchId the dispatch ID
     * @return true if exists, false otherwise
     */
    boolean existsByDispatchId(String dispatchId);

    /**
     * Check if a calculation exists for a dispatch in a tenant.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @return true if exists, false otherwise
     */
    boolean existsByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Delete an ETA calculation by ID.
     *
     * @param id the calculation ID
     */
    void deleteById(String id);

    /**
     * Delete by dispatch ID.
     *
     * @param dispatchId the dispatch ID
     */
    void deleteByDispatchId(String dispatchId);

    /**
     * Count all ETA calculations.
     *
     * @return the count
     */
    long count();

    /**
     * Count ETA calculations by tenant.
     *
     * @param tenantId the tenant ID
     * @return the count
     */
    long countByTenantId(String tenantId);

    /**
     * Count ETA calculations by status.
     *
     * @param status the ETA status
     * @return the count
     */
    long countByStatus(EtaCalculation.EtaStatus status);

    /**
     * Count ETA calculations by status and tenant.
     *
     * @param status   the ETA status
     * @param tenantId the tenant ID
     * @return the count
     */
    long countByStatusAndTenantId(EtaCalculation.EtaStatus status, String tenantId);

    /**
     * Find ETA calculations with pagination.
     *
     * @param tenantId the tenant ID
     * @param page     the page number (0-indexed)
     * @param size     the page size
     * @return list of calculations
     */
    List<EtaCalculation> findByTenantIdPaginated(String tenantId, int page, int size);

    /**
     * Find calculations created after a given timestamp.
     *
     * @param after    the timestamp
     * @param tenantId the tenant ID
     * @return list of calculations
     */
    List<EtaCalculation> findByCreatedAtAfterAndTenantId(Instant after, String tenantId);
}
