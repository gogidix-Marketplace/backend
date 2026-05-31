package com.gogidix.courier.dynamicpricingservice.domain.repository;

import com.gogidix.courier.dynamicpricingservice.domain.entity.DemandLevel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DemandLevelRepository {

    DemandLevel save(DemandLevel demandLevel);

    Optional<DemandLevel> findLatestByZoneId(String zoneId);

    List<DemandLevel> findByZoneIdAndTimestampBetween(String zoneId, LocalDateTime start, LocalDateTime end);

    List<DemandLevel> findByTenantId(String tenantId);

    void deleteById(String id);
}
