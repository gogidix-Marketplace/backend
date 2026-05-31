package com.gogidix.ecommerce.analytics.domain.port.out;

import com.gogidix.ecommerce.analytics.domain.model.Analytics;
import java.util.List;
import java.util.Optional;

public interface AnalyticsRepositoryPort {
    Analytics save(Analytics entity);
    Optional<Analytics> findById(String id);
    List<Analytics> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
