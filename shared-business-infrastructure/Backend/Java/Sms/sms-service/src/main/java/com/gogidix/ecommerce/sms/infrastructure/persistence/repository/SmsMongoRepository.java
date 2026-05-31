package com.gogidix.ecommerce.sms.infrastructure.persistence.repository;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SmsMongoRepository extends MongoRepository<Sms, String> {

    Optional<Sms> findByIdAndTenantId(String id, String tenantId);

    Page<Sms> findByTenantId(String tenantId, Pageable pageable);
}