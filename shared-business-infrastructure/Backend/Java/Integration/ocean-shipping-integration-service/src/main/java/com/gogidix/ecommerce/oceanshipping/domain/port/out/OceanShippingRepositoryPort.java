package com.gogidix.ecommerce.oceanshipping.domain.port.out;

import com.gogidix.ecommerce.oceanshipping.domain.model.OceanShipping;
import java.util.List;
import java.util.Optional;

public interface OceanShippingRepositoryPort {
    OceanShipping save(OceanShipping entity);
    Optional<OceanShipping> findById(String id);
    List<OceanShipping> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
