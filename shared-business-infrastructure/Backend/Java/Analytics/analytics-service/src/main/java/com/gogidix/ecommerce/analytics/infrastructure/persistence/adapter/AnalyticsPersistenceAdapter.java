package com.gogidix.ecommerce.analytics.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.analytics.domain.model.Analytics;
import com.gogidix.ecommerce.analytics.domain.port.out.AnalyticsRepositoryPort;
import com.gogidix.ecommerce.analytics.infrastructure.persistence.repository.AnalyticsMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AnalyticsPersistenceAdapter implements AnalyticsRepositoryPort {

    private final AnalyticsMongoRepository mongoRepository;

    public AnalyticsPersistenceAdapter(AnalyticsMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Analytics save(Analytics entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Analytics> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Analytics> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
