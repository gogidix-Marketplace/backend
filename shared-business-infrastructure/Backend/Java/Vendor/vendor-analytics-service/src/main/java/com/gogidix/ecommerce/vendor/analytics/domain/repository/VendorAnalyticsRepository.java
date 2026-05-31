package com.gogidix.ecommerce.vendor.analytics.domain.repository;

import com.gogidix.ecommerce.vendor.analytics.domain.model.VendorAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorAnalyticsRepository extends MongoRepository<VendorAnalytics, String> {
    List<VendorAnalytics> findByTenantId(String tenantId);
    Optional<VendorAnalytics> findByTenantIdAndId(String tenantId, String id);
}