package com.gogidix.ecommerce.loyalty.domain.port.out;

import com.gogidix.ecommerce.loyalty.domain.model.Loyalty;
import java.util.List;
import java.util.Optional;

public interface LoyaltyRepositoryPort {
    Loyalty save(Loyalty entity);
    Optional<Loyalty> findById(String id);
    List<Loyalty> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
