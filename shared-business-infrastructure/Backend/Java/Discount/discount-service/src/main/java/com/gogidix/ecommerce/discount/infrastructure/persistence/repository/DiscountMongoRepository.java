package com.gogidix.ecommerce.discount.infrastructure.persistence.repository;

import com.gogidix.ecommerce.discount.domain.model.Discount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DiscountMongoRepository extends MongoRepository<Discount, String> {
    List<Discount> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
