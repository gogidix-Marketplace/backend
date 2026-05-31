package com.gogidix.globalbusinessmanagement.kafkaingestion.domain.repository;

import com.gogidix.globalbusinessmanagement.kafkaingestion.domain.model.IngestionJob;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngestionJobRepository extends MongoRepository<IngestionJob, String> {
    List<IngestionJob> findByTenantId(String tenantId);
}
