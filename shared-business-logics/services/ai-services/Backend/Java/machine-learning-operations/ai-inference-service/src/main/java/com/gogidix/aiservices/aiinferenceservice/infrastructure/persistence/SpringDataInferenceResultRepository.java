package com.gogidix.aiservices.aiinferenceservice.infrastructure.persistence;

import com.gogidix.aiservices.aiinferenceservice.domain.model.InferenceResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for InferenceResult.
 */
public interface SpringDataInferenceResultRepository extends MongoRepository<InferenceResult, String> {

    List<InferenceResult> findByTenantId(String tenantId, Pageable pageable);
}
