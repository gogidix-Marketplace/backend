package com.gogidix.aiservices.aifeatureextractionservice.infrastructure.persistence;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for FeatureSet.
 */
@Repository
public interface SpringDataFeatureSetRepository extends MongoRepository<FeatureSet, String> {

    Optional<FeatureSet> findByIdAndTenantId(String id, String tenantId);

    Page<FeatureSet> findByTenantId(String tenantId, Pageable pageable);
}
