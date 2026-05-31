package com.gogidix.ecommerce.oceanshipping.domain.repository;

import com.gogidix.ecommerce.oceanshipping.domain.model.OceanShipping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OceanShippingRepository extends MongoRepository<OceanShipping, String> {
    List<OceanShipping> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
