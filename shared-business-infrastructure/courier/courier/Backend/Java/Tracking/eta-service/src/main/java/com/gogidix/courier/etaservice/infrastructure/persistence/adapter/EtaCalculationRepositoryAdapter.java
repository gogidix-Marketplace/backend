package com.gogidix.courier.etaservice.infrastructure.persistence.adapter;

import com.gogidix.courier.etaservice.domain.entity.EtaCalculation;
import com.gogidix.courier.etaservice.domain.repository.EtaCalculationRepository;
import com.gogidix.courier.etaservice.infrastructure.persistence.repository.MongoEtaCalculationRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of EtaCalculationRepository.
 */
@Repository
public class EtaCalculationRepositoryAdapter implements EtaCalculationRepository {

    private final MongoEtaCalculationRepository mongoRepository;

    public EtaCalculationRepositoryAdapter(MongoEtaCalculationRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public EtaCalculation save(EtaCalculation calculation) {
        return mongoRepository.save(calculation);
    }

    @Override
    public Optional<EtaCalculation> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<EtaCalculation> findByDispatchId(String dispatchId) {
        return mongoRepository.findByDispatchId(dispatchId);
    }

    @Override
    public Optional<EtaCalculation> findByDispatchIdAndTenantId(String dispatchId, String tenantId) {
        return mongoRepository.findByDispatchIdAndTenantId(dispatchId, tenantId);
    }

    @Override
    public List<EtaCalculation> findByTenantId(String tenantId) {
        return mongoRepository.findByTenantId(tenantId);
    }

    @Override
    public List<EtaCalculation> findAll() {
        return mongoRepository.findAll();
    }

    @Override
    public List<EtaCalculation> findByStatus(EtaCalculation.EtaStatus status) {
        return mongoRepository.findByStatus(status);
    }

    @Override
    public List<EtaCalculation> findByStatusAndTenantId(EtaCalculation.EtaStatus status, String tenantId) {
        return mongoRepository.findByStatusAndTenantId(status, tenantId);
    }

    @Override
    public List<EtaCalculation> findActiveByTenantId(String tenantId) {
        return mongoRepository.findByStatusAndTenantId(EtaCalculation.EtaStatus.IN_TRANSIT, tenantId);
    }

    @Override
    public List<EtaCalculation> findByEstimatedArrivalBeforeAndTenantId(Instant before, String tenantId) {
        return mongoRepository.findByEstimatedArrivalBeforeAndTenantId(before, tenantId);
    }

    @Override
    public List<EtaCalculation> findByEstimatedArrivalAfterAndTenantId(Instant after, String tenantId) {
        return mongoRepository.findByEstimatedArrivalAfterAndTenantId(after, tenantId);
    }

    @Override
    public boolean existsByDispatchId(String dispatchId) {
        return mongoRepository.existsByDispatchId(dispatchId);
    }

    @Override
    public boolean existsByDispatchIdAndTenantId(String dispatchId, String tenantId) {
        return mongoRepository.existsByDispatchIdAndTenantId(dispatchId, tenantId);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteByDispatchId(String dispatchId) {
        mongoRepository.deleteByDispatchId(dispatchId);
    }

    @Override
    public long count() {
        return mongoRepository.count();
    }

    @Override
    public long countByTenantId(String tenantId) {
        return mongoRepository.countByTenantId(tenantId);
    }

    @Override
    public long countByStatus(EtaCalculation.EtaStatus status) {
        return mongoRepository.countByStatus(status);
    }

    @Override
    public long countByStatusAndTenantId(EtaCalculation.EtaStatus status, String tenantId) {
        return mongoRepository.countByStatusAndTenantId(status, tenantId);
    }

    @Override
    public List<EtaCalculation> findByTenantIdPaginated(String tenantId, int page, int size) {
        return mongoRepository.findByTenantIdOrderByCreatedAtDesc(tenantId)
                .stream()
                .skip(page * size)
                .limit(size)
                .toList();
    }

    @Override
    public List<EtaCalculation> findByCreatedAtAfterAndTenantId(Instant after, String tenantId) {
        return mongoRepository.findByCreatedAtAfterAndTenantId(after, tenantId);
    }
}
