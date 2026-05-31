package com.gogidix.ecommerce.customer.application.mapper;

import com.gogidix.ecommerce.customer.application.dto.*;
import com.gogidix.ecommerce.customer.domain.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerResponse toResponse(Customer entity) {
        if (entity == null) return null;
        return new CustomerResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getTier(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Customer toEntity(CreateCustomerRequest request) {
        Customer entity = new Customer();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setEmail(request.email());
        entity.setPhone(request.phone());
        entity.setTier(request.tier());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Customer entity, UpdateCustomerRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.email() != null) entity.setEmail(request.email());
        if (request.phone() != null) entity.setPhone(request.phone());
        if (request.tier() != null) entity.setTier(request.tier());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
