package com.gogidix.ecommerce.reward.infrastructure.persistence.repository;

import com.gogidix.ecommerce.reward.domain.model.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RewardMongoRepository extends MongoRepository<Reward, String> {
    List<Reward> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
