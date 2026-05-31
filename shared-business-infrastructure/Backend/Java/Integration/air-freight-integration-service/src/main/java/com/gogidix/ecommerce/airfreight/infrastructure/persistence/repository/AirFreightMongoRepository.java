package com.gogidix.ecommerce.airfreight.infrastructure.persistence.repository;

import com.gogidix.ecommerce.airfreight.domain.model.AirFreight;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AirFreightMongoRepository extends MongoRepository<AirFreight, String> {
    List<AirFreight> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
