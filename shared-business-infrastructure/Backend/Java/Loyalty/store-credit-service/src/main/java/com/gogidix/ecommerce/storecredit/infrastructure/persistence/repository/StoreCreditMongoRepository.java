package com.gogidix.ecommerce.storecredit.infrastructure.persistence.repository;

import com.gogidix.ecommerce.storecredit.domain.model.StoreCredit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StoreCreditMongoRepository extends MongoRepository<StoreCredit, String> {
    List<StoreCredit> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
