package com.gogidix.ecommerce.promotion.infrastructure.persistence.repository;

import com.gogidix.ecommerce.promotion.domain.model.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PromotionMongoRepository extends MongoRepository<Promotion, String> {
    List<Promotion> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
