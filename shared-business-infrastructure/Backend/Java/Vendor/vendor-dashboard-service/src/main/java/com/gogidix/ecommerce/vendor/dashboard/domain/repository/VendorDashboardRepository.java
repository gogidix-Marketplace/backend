package com.gogidix.ecommerce.vendor.dashboard.domain.repository;

import com.gogidix.ecommerce.vendor.dashboard.domain.model.VendorDashboard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorDashboardRepository extends MongoRepository<VendorDashboard, String> {
    List<VendorDashboard> findByTenantId(String tenantId);
    Optional<VendorDashboard> findByTenantIdAndId(String tenantId, String id);
}