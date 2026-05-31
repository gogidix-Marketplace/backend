package com.gogidix.ecommerce.customer.infrastructure.persistence.repository;

import com.gogidix.ecommerce.customer.domain.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerMongoRepository extends MongoRepository<Customer, String> {
    List<Customer> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
