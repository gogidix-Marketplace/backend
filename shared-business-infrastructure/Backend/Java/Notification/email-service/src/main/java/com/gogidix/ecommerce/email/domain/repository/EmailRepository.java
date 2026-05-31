package com.gogidix.ecommerce.email.domain.repository;

import com.gogidix.ecommerce.email.domain.model.Email;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends MongoRepository<Email, String> {
    List<Email> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
