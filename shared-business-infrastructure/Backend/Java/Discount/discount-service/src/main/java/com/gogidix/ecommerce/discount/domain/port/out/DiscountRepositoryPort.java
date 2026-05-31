package com.gogidix.ecommerce.discount.domain.port.out;

import com.gogidix.ecommerce.discount.domain.model.Discount;
import java.util.List;
import java.util.Optional;

public interface DiscountRepositoryPort {
    Discount save(Discount entity);
    Optional<Discount> findById(String id);
    List<Discount> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
