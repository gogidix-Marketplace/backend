package com.gogidix.globalbusinessmanagement.batchaggregation.domain.repository;

import com.gogidix.globalbusinessmanagement.batchaggregation.domain.model.BatchAggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchAggregationRepository extends MongoRepository<BatchAggregation, String> {
    List<BatchAggregation> findByTenantId(String tenantId);
}
