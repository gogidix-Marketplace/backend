package com.gogidix.shared.warehousing.quality.domain.repository;

import com.gogidix.shared.warehousing.quality.domain.entity.QualityCheck;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QualityCheckRepository extends MongoRepository<QualityCheck, String> {

    List<QualityCheck> findByTenantId(String tenantId);

    List<QualityCheck> findByTenantIdAndReferenceId(String tenantId, String referenceId);

    List<QualityCheck> findByTenantIdAndStatus(String tenantId, QualityCheck.QualityStatus status);

    List<QualityCheck> findByTenantIdAndInspectorId(String tenantId, String inspectorId);

    List<QualityCheck> findByTenantIdAndInspectionDateBetween(
        String tenantId, LocalDateTime start, LocalDateTime end);

    List<QualityCheck> findByTenantIdAndPassed(String tenantId, Boolean passed);
}
