package com.gogidix.ecommerce.customer.application.service;

import com.gogidix.ecommerce.customer.application.dto.*;
import com.gogidix.ecommerce.customer.application.mapper.CustomerMapper;
import com.gogidix.ecommerce.customer.domain.model.Customer;
import com.gogidix.ecommerce.customer.domain.repository.CustomerRepository;
import com.gogidix.ecommerce.customer.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CustomerResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public CustomerResponse getById(String id) {
        Customer entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        return mapper.toResponse(entity);
    }

    public CustomerResponse create(CreateCustomerRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Customer entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Customer saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public CustomerResponse update(String id, UpdateCustomerRequest request) {
        Customer entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Customer saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        repository.deleteById(id);
    }
}
