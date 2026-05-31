package com.gogidix.courier.etaservice.domain.repository;

import com.gogidix.courier.etaservice.domain.entity.EtaHistory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EtaHistory aggregates.
 * Defines the contract for ETA history persistence operations.
 */
public interface EtaHistoryRepository {

    /**
     * Save an ETA history entry.
     *
     * @param history the history to save
     * @return the saved history
     */
    EtaHistory save(EtaHistory history);

    /**
     * Find a history entry by ID.
     *
     * @param id the history ID
     * @return the history if found
     */
    Optional<EtaHistory> findById(String id);

    /**
     * Find all history entries for a dispatch.
     *
     * @param dispatchId the dispatch ID
     * @return list of history entries
     */
    List<EtaHistory> findByDispatchId(String dispatchId);

    /**
     * Find all history entries for a dispatch and tenant.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @return list of history entries
     */
    List<EtaHistory> findByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Find all history entries for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of history entries
     */
    List<EtaHistory> findByTenantId(String tenantId);

    /**
     * Find history entries within a time range.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @param from       the start time
     * @param to         the end time
     * @return list of history entries
     */
    List<EtaHistory> findByDispatchIdAndTenantIdAndTimestampBetween(
            String dispatchId, String tenantId, Instant from, Instant to);

    /**
     * Find recent history entries for a dispatch.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @param limit      the maximum number of entries
     * @return list of history entries
     */
    List<EtaHistory> findRecentByDispatchIdAndTenantId(
            String dispatchId, String tenantId, int limit);

    /**
     * Find history entries by change type.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @param changeType the change type
     * @return list of history entries
     */
    List<EtaHistory> findByDispatchIdAndTenantIdAndChangeType(
            String dispatchId, String tenantId, EtaHistory.ChangeType changeType);

    /**
     * Delete old history entries.
     *
     * @param before the timestamp threshold
     * @param tenantId the tenant ID
     * @return the number of deleted entries
     */
    long deleteByTimestampBeforeAndTenantId(Instant before, String tenantId);

    /**
     * Count history entries for a dispatch.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @return the count
     */
    long countByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Delete all history entries for a dispatch.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     */
    void deleteByDispatchIdAndTenantId(String dispatchId, String tenantId);
}
