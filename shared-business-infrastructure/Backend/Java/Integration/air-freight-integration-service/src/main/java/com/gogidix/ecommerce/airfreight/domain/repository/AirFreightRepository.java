package com.gogidix.ecommerce.airfreight.domain.repository;

import com.gogidix.ecommerce.airfreight.domain.model.AirFreight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirFreightRepository extends MongoRepository<AirFreight, String> {
    List<AirFreight> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
