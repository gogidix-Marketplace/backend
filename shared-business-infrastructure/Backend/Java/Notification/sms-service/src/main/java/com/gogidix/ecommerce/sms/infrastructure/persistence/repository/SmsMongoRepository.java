package com.gogidix.ecommerce.sms.infrastructure.persistence.repository;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SmsMongoRepository extends MongoRepository<Sms, String> {
    List<Sms> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
