package com.gogidix.ecommerce.storecredit.domain.port.out;

import com.gogidix.ecommerce.storecredit.domain.model.StoreCredit;
import java.util.List;
import java.util.Optional;

public interface StoreCreditRepositoryPort {
    StoreCredit save(StoreCredit entity);
    Optional<StoreCredit> findById(String id);
    List<StoreCredit> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
