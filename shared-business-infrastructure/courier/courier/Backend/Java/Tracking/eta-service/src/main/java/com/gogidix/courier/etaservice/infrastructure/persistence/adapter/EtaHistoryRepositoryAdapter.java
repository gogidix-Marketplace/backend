package com.gogidix.courier.etaservice.infrastructure.persistence.adapter;

import com.gogidix.courier.etaservice.domain.entity.EtaHistory;
import com.gogidix.courier.etaservice.domain.repository.EtaHistoryRepository;
import com.gogidix.courier.etaservice.infrastructure.persistence.repository.MongoEtaHistoryRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of EtaHistoryRepository.
 */
@Repository
public class EtaHistoryRepositoryAdapter implements EtaHistoryRepository {

    private final MongoEtaHistoryRepository mongoRepository;

    public EtaHistoryRepositoryAdapter(MongoEtaHistoryRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public EtaHistory save(EtaHistory history) {
        return mongoRepository.save(history);
    }

    @Override
    public Optional<EtaHistory> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public List<EtaHistory> findByDispatchId(String dispatchId) {
        return mongoRepository.findByDispatchIdOrderByTimestampDesc(dispatchId);
    }

    @Override
    public List<EtaHistory> findByDispatchIdAndTenantId(String dispatchId, String tenantId) {
        return mongoRepository.findByDispatchIdAndTenantIdOrderByTimestampDesc(dispatchId, tenantId);
    }

    @Override
    public List<EtaHistory> findByTenantId(String tenantId) {
        return mongoRepository.findByTenantIdOrderByTimestampDesc(tenantId);
    }

    @Override
    public List<EtaHistory> findByDispatchIdAndTenantIdAndTimestampBetween(
            String dispatchId, String tenantId, Instant from, Instant to) {
        return mongoRepository.findByDispatchIdAndTenantIdAndTimestampBetweenOrderByTimestampDesc(
                dispatchId, tenantId, from, to);
    }

    @Override
    public List<EtaHistory> findRecentByDispatchIdAndTenantId(
            String dispatchId, String tenantId, int limit) {
        return mongoRepository.findRecentByDispatchIdAndTenantId(dispatchId, tenantId, limit);
    }

    @Override
    public List<EtaHistory> findByDispatchIdAndTenantIdAndChangeType(
            String dispatchId, String tenantId, EtaHistory.ChangeType changeType) {
        return mongoRepository.findByDispatchIdAndTenantIdAndChangeTypeOrderByTimestampDesc(
                dispatchId, tenantId, changeType);
    }

    @Override
    public long deleteByTimestampBeforeAndTenantId(Instant before, String tenantId) {
        List<EtaHistory> toDelete = mongoRepository.findByTimestampBeforeAndTenantId(before, tenantId);
        mongoRepository.deleteAll(toDelete);
        return toDelete.size();
    }

    @Override
    public long countByDispatchIdAndTenantId(String dispatchId, String tenantId) {
        return mongoRepository.countByDispatchIdAndTenantId(dispatchId, tenantId);
    }

    @Override
    public void deleteByDispatchIdAndTenantId(String dispatchId, String tenantId) {
        mongoRepository.deleteByDispatchIdAndTenantId(dispatchId, tenantId);
    }
}
