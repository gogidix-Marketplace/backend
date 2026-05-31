package com.gogidix.ecommerce.email.infrastructure.persistence.repository;

import com.gogidix.ecommerce.email.domain.model.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmailMongoRepository extends MongoRepository<Email, String> {
    List<Email> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
