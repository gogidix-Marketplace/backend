package com.gogidix.ecommerce.loyalty.infrastructure.persistence.repository;

import com.gogidix.ecommerce.loyalty.domain.model.Loyalty;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoyaltyMongoRepository extends MongoRepository<Loyalty, String> {
    List<Loyalty> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
