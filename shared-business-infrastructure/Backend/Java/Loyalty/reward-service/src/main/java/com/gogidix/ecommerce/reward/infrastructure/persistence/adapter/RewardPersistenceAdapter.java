package com.gogidix.ecommerce.reward.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.reward.domain.model.Reward;
import com.gogidix.ecommerce.reward.domain.port.out.RewardRepositoryPort;
import com.gogidix.ecommerce.reward.infrastructure.persistence.repository.RewardMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RewardPersistenceAdapter implements RewardRepositoryPort {

    private final RewardMongoRepository mongoRepository;

    public RewardPersistenceAdapter(RewardMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Reward save(Reward entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Reward> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Reward> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
