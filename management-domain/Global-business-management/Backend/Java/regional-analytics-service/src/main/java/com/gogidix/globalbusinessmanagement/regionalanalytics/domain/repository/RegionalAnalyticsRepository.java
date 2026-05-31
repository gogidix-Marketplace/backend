package com.gogidix.globalbusinessmanagement.regionalanalytics.domain.repository;

import com.gogidix.globalbusinessmanagement.regionalanalytics.domain.model.RegionalAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionalAnalyticsRepository extends MongoRepository<RegionalAnalytics, String> {
    List<RegionalAnalytics> findByTenantId(String tenantId);
}
