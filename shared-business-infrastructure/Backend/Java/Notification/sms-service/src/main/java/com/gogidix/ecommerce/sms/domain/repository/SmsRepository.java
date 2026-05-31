package com.gogidix.ecommerce.sms.domain.repository;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsRepository extends MongoRepository<Sms, String> {
    List<Sms> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
