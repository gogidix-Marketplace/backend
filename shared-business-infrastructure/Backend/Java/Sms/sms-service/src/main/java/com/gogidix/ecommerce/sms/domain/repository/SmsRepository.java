package com.gogidix.ecommerce.sms.domain.repository;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsRepository extends MongoRepository<Sms, String> {

    Optional<Sms findByIdAndTenantId(String id, String tenantId);

    Page<Sms> findByTenantId(String tenantId, Pageable pageable);
}