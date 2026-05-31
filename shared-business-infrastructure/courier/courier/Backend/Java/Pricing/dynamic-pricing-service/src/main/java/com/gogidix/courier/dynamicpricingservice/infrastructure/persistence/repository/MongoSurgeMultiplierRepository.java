package com.gogidix.courier.dynamicpricingservice.infrastructure.persistence.repository;

import com.gogidix.courier.dynamicpricingservice.domain.entity.SurgeMultiplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MongoSurgeMultiplierRepository extends MongoRepository<SurgeMultiplier, String> {

    @Override
    List<SurgeMultiplier> findAll();

    Optional<SurgeMultiplier> findFirstByZoneIdAndExpiryTimeAfterOrderByEffectiveTimeDesc(
            String zoneId, LocalDateTime now);

    List<SurgeMultiplier> findByTenantId(String tenantId);

    List<SurgeMultiplier> findByTenantIdAndZoneId(String tenantId, String zoneId);
}
