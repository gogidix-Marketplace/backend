package com.gogidix.ecommerce.customer.domain.port.out;

import com.gogidix.ecommerce.customer.domain.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {
    Customer save(Customer entity);
    Optional<Customer> findById(String id);
    List<Customer> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
