package com.gogidix.ecommerce.promotion.domain.port.out;

import com.gogidix.ecommerce.promotion.domain.model.Promotion;
import java.util.List;
import java.util.Optional;

public interface PromotionRepositoryPort {
    Promotion save(Promotion entity);
    Optional<Promotion> findById(String id);
    List<Promotion> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
