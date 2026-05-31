package com.gogidix.ecommerce.email.domain.repository;

import com.gogidix.ecommerce.email.domain.model.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends MongoRepository<Email, String> {

    Optional<Email findByIdAndTenantId(String id, String tenantId);

    Page<Email> findByTenantId(String tenantId, Pageable pageable);
}