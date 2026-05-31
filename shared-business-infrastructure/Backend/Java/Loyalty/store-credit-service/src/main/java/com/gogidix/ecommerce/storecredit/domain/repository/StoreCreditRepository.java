package com.gogidix.ecommerce.storecredit.domain.repository;

import com.gogidix.ecommerce.storecredit.domain.model.StoreCredit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreCreditRepository extends MongoRepository<StoreCredit, String> {
    List<StoreCredit> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
