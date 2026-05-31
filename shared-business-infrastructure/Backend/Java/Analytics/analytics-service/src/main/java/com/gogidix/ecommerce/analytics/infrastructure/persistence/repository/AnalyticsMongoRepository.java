package com.gogidix.ecommerce.analytics.infrastructure.persistence.repository;

import com.gogidix.ecommerce.analytics.domain.model.Analytics;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnalyticsMongoRepository extends MongoRepository<Analytics, String> {
    List<Analytics> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
