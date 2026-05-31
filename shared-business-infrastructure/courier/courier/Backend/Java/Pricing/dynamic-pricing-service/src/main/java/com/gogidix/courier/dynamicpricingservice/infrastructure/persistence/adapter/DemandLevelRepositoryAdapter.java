package com.gogidix.courier.dynamicpricingservice.infrastructure.persistence.adapter;

import com.gogidix.courier.dynamicpricingservice.domain.entity.DemandLevel;
import com.gogidix.courier.dynamicpricingservice.domain.repository.DemandLevelRepository;
import com.gogidix.courier.dynamicpricingservice.infrastructure.persistence.repository.MongoDemandLevelRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DemandLevelRepositoryAdapter implements DemandLevelRepository {

    private final MongoDemandLevelRepository mongoRepository;

    public DemandLevelRepositoryAdapter(MongoDemandLevelRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public DemandLevel save(DemandLevel demandLevel) {
        return mongoRepository.save(demandLevel);
    }

    @Override
    public Optional<DemandLevel> findLatestByZoneId(String zoneId) {
        return mongoRepository.findFirstByZoneIdOrderByTimestampDesc(zoneId);
    }

    @Override
    public List<DemandLevel> findByZoneIdAndTimestampBetween(String zoneId, LocalDateTime start, LocalDateTime end) {
        return mongoRepository.findByZoneIdAndTimestampBetween(zoneId, start, end);
    }

    @Override
    public List<DemandLevel> findByTenantId(String tenantId) {
        return mongoRepository.findByTenantId(tenantId);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }
}
