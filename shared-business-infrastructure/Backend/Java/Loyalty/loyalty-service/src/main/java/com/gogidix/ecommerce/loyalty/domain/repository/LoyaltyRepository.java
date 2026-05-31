package com.gogidix.ecommerce.loyalty.domain.repository;

import com.gogidix.ecommerce.loyalty.domain.model.Loyalty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoyaltyRepository extends MongoRepository<Loyalty, String> {
    List<Loyalty> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
