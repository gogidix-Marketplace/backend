package com.gogidix.digitalmarketing.integration.domain.repository;

import com.gogidix.digitalmarketing.integration.domain.model.Integration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntegrationRepository extends MongoRepository<Integration, String> {
    List<Integration> findByTenantId(String tenantId);
}