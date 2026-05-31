package com.gogidix.ecommerce.oceanshipping.infrastructure.persistence.repository;

import com.gogidix.ecommerce.oceanshipping.domain.model.OceanShipping;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OceanShippingMongoRepository extends MongoRepository<OceanShipping, String> {
    List<OceanShipping> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
