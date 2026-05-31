package com.gogidix.courier.etaservice.infrastructure.persistence.repository;

import com.gogidix.courier.etaservice.domain.entity.EtaCalculation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for EtaCalculation.
 */
@Repository
public interface MongoEtaCalculationRepository extends MongoRepository<EtaCalculation, String> {

    Optional<EtaCalculation> findByDispatchId(String dispatchId);

    Optional<EtaCalculation> findByDispatchIdAndTenantId(String dispatchId, String tenantId);

    List<EtaCalculation> findByTenantId(String tenantId);

    List<EtaCalculation> findByStatus(EtaCalculation.EtaStatus status);

    List<EtaCalculation> findByStatusAndTenantId(EtaCalculation.EtaStatus status, String tenantId);

    List<EtaCalculation> findByEstimatedArrivalBeforeAndTenantId(Instant before, String tenantId);

    List<EtaCalculation> findByEstimatedArrivalAfterAndTenantId(Instant after, String tenantId);

    List<EtaCalculation> findByTenantIdOrderByCreatedAtDesc(String tenantId);

    List<EtaCalculation> findByCreatedAtAfterAndTenantId(Instant after, String tenantId);

    boolean existsByDispatchId(String dispatchId);

    boolean existsByDispatchIdAndTenantId(String dispatchId, String tenantId);

    void deleteByDispatchId(String dispatchId);

    long countByTenantId(String tenantId);

    long countByStatus(EtaCalculation.EtaStatus status);

    long countByStatusAndTenantId(EtaCalculation.EtaStatus status, String tenantId);
}
