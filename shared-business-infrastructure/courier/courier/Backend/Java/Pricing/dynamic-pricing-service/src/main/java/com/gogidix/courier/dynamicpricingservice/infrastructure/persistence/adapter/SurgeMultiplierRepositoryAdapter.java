package com.gogidix.courier.dynamicpricingservice.infrastructure.persistence.adapter;

import com.gogidix.courier.dynamicpricingservice.domain.entity.SurgeMultiplier;
import com.gogidix.courier.dynamicpricingservice.domain.repository.SurgeMultiplierRepository;
import com.gogidix.courier.dynamicpricingservice.infrastructure.persistence.repository.MongoSurgeMultiplierRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class SurgeMultiplierRepositoryAdapter implements SurgeMultiplierRepository {

    private final MongoSurgeMultiplierRepository mongoRepository;

    public SurgeMultiplierRepositoryAdapter(MongoSurgeMultiplierRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public SurgeMultiplier save(SurgeMultiplier surge) {
        return mongoRepository.save(surge);
    }

    @Override
    public Optional<SurgeMultiplier> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<SurgeMultiplier> findActiveByZoneId(String zoneId) {
        return mongoRepository.findFirstByZoneIdAndExpiryTimeAfterOrderByEffectiveTimeDesc(
                zoneId, LocalDateTime.now());
    }

    @Override
    public List<SurgeMultiplier> findByTenantId(String tenantId) {
        return mongoRepository.findByTenantId(tenantId);
    }

    @Override
    public List<SurgeMultiplier> findByTenantIdAndZoneId(String tenantId, String zoneId) {
        return mongoRepository.findByTenantIdAndZoneId(tenantId, zoneId);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteExpiredBefore(LocalDateTime time) {
        mongoRepository.findAll().stream()
                .filter(s -> s.getExpiryTime() != null && s.getExpiryTime().isBefore(time))
                .forEach(s -> mongoRepository.delete(s));
    }
}
