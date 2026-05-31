package com.gogidix.ecommerce.analytics.domain.repository;

import com.gogidix.ecommerce.analytics.domain.model.Analytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsRepository extends MongoRepository<Analytics, String> {
    List<Analytics> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
