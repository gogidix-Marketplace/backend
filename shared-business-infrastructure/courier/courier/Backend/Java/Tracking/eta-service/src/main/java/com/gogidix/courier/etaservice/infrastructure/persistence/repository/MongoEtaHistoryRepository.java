package com.gogidix.courier.etaservice.infrastructure.persistence.repository;

import com.gogidix.courier.etaservice.domain.entity.EtaHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data MongoDB repository for EtaHistory.
 */
@Repository
public interface MongoEtaHistoryRepository extends MongoRepository<EtaHistory, String> {

    List<EtaHistory> findByDispatchIdOrderByTimestampDesc(String dispatchId);

    List<EtaHistory> findByDispatchIdAndTenantIdOrderByTimestampDesc(String dispatchId, String tenantId);

    List<EtaHistory> findByTenantIdOrderByTimestampDesc(String tenantId);

    List<EtaHistory> findByDispatchIdAndTenantIdAndTimestampBetweenOrderByTimestampDesc(
            String dispatchId, String tenantId, Instant from, Instant to);

    @Query("{ 'dispatchId': ?0, 'tenantId': ?1 }")
    List<EtaHistory> findRecentByDispatchIdAndTenantId(String dispatchId, String tenantId, org.springframework.data.domain.Pageable pageable);

    default List<EtaHistory> findRecentByDispatchIdAndTenantId(String dispatchId, String tenantId, int limit) {
        return findRecentByDispatchIdAndTenantId(dispatchId, tenantId,
                org.springframework.data.domain.PageRequest.of(0, limit));
    }

    List<EtaHistory> findByDispatchIdAndTenantIdAndChangeTypeOrderByTimestampDesc(
            String dispatchId, String tenantId, EtaHistory.ChangeType changeType);

    List<EtaHistory> findByTimestampBeforeAndTenantId(Instant before, String tenantId);

    long countByDispatchIdAndTenantId(String dispatchId, String tenantId);

    void deleteByDispatchIdAndTenantId(String dispatchId, String tenantId);
}
