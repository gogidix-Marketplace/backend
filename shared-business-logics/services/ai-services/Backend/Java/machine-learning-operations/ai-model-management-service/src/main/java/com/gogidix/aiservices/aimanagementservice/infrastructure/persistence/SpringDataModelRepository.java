package com.gogidix.aiservices.aimanagementservice.infrastructure.persistence;

import com.gogidix.aiservices.aimanagementservice.domain.model.MLModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for MLModel.
 */
public interface SpringDataModelRepository extends MongoRepository<MLModel, String> {

    Optional<MLModel> findByIdAndTenantId(String id, String tenantId);

    List<MLModel> findByTenantId(String tenantId, Pageable pageable);

    boolean existsByIdAndTenantId(String id, String tenantId);
}
