package com.gogidix.ecommerce.customer.domain.repository;

import com.gogidix.ecommerce.customer.domain.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
