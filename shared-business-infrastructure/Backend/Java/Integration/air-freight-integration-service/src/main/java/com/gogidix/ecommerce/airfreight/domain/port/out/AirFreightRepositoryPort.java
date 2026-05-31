package com.gogidix.ecommerce.airfreight.domain.port.out;

import com.gogidix.ecommerce.airfreight.domain.model.AirFreight;
import java.util.List;
import java.util.Optional;

public interface AirFreightRepositoryPort {
    AirFreight save(AirFreight entity);
    Optional<AirFreight> findById(String id);
    List<AirFreight> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
