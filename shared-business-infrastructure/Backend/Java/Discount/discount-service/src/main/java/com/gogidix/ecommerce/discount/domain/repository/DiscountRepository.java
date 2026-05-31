package com.gogidix.ecommerce.discount.domain.repository;

import com.gogidix.ecommerce.discount.domain.model.Discount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends MongoRepository<Discount, String> {
    List<Discount> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
