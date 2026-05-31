package com.gogidix.courier.dynamicpricingservice.infrastructure.persistence.repository;

import com.gogidix.courier.dynamicpricingservice.domain.entity.DemandLevel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MongoDemandLevelRepository extends MongoRepository<DemandLevel, String> {

    Optional<DemandLevel> findFirstByZoneIdOrderByTimestampDesc(String zoneId);

    List<DemandLevel> findByZoneIdAndTimestampBetween(String zoneId, LocalDateTime start, LocalDateTime end);

    List<DemandLevel> findByTenantId(String tenantId);
}
