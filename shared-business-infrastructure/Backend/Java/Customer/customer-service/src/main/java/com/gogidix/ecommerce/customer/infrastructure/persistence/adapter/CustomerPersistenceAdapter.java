package com.gogidix.ecommerce.customer.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.customer.domain.model.Customer;
import com.gogidix.ecommerce.customer.domain.port.out.CustomerRepositoryPort;
import com.gogidix.ecommerce.customer.infrastructure.persistence.repository.CustomerMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerPersistenceAdapter implements CustomerRepositoryPort {

    private final CustomerMongoRepository mongoRepository;

    public CustomerPersistenceAdapter(CustomerMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Customer save(Customer entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Customer> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Customer> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
