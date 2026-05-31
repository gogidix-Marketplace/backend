package com.gogidix.aiservices.aiinferenceservice.infrastructure.persistence;

import com.gogidix.aiservices.aiinferenceservice.domain.model.LoadedModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for LoadedModel.
 */
public interface SpringDataLoadedModelRepository extends MongoRepository<LoadedModel, String> {

    Optional<LoadedModel> findByModelIdAndTenantId(String modelId, String tenantId);

    void deleteByModelIdAndTenantId(String modelId, String tenantId);
}
