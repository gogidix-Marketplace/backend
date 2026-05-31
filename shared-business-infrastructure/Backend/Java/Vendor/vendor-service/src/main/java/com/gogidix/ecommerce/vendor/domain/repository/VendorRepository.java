package com.gogidix.ecommerce.vendor.domain.repository;

import com.gogidix.ecommerce.vendor.domain.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends MongoRepository<Vendor, String> {
    List<Vendor> findByTenantId(String tenantId);
    Optional<Vendor> findByTenantIdAndId(String tenantId, String id);
}