package com.gogidix.ecommerce.vendor.dropship.domain.repository;

import com.gogidix.ecommerce.vendor.dropship.domain.model.Dropship;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DropshipRepository extends MongoRepository<Dropship, String> {
    List<Dropship> findByTenantId(String tenantId);
    Optional<Dropship> findByTenantIdAndId(String tenantId, String id);
}