package com.gogidix.courier.dynamicpricingservice.domain.repository;

import com.gogidix.courier.dynamicpricingservice.domain.entity.SurgeMultiplier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SurgeMultiplierRepository {

    SurgeMultiplier save(SurgeMultiplier surge);

    Optional<SurgeMultiplier> findById(String id);

    Optional<SurgeMultiplier> findActiveByZoneId(String zoneId);

    List<SurgeMultiplier> findByTenantId(String tenantId);

    List<SurgeMultiplier> findByTenantIdAndZoneId(String tenantId, String zoneId);

    void deleteById(String id);

    void deleteExpiredBefore(LocalDateTime time);
}
