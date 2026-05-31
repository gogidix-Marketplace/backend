package com.gogidix.ecommerce.reward.domain.repository;

import com.gogidix.ecommerce.reward.domain.model.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends MongoRepository<Reward, String> {
    List<Reward> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
