package com.gogidix.ecommerce.promotion.domain.repository;

import com.gogidix.ecommerce.promotion.domain.model.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends MongoRepository<Promotion, String> {
    List<Promotion> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
