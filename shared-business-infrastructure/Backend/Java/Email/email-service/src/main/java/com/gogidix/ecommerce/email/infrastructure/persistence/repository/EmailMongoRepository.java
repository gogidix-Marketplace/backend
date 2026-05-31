package com.gogidix.ecommerce.email.infrastructure.persistence.repository;

import com.gogidix.ecommerce.email.domain.model.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmailMongoRepository extends MongoRepository<Email, String> {

    Optional<Email> findByIdAndTenantId(String id, String tenantId);

    Page<Email> findByTenantId(String tenantId, Pageable pageable);
}