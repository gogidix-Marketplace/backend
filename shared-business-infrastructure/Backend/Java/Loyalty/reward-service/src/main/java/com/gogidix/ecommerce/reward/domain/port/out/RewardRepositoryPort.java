package com.gogidix.ecommerce.reward.domain.port.out;

import com.gogidix.ecommerce.reward.domain.model.Reward;
import java.util.List;
import java.util.Optional;

public interface RewardRepositoryPort {
    Reward save(Reward entity);
    Optional<Reward> findById(String id);
    List<Reward> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
