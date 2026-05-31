package com.gogidix.ecommerce.paymentmethod.application.mapper;

import com.gogidix.ecommerce.paymentmethod.application.dto.CreatePaymentMethodRequest;
import com.gogidix.ecommerce.paymentmethod.application.dto.UpdatePaymentMethodRequest;
import com.gogidix.ecommerce.paymentmethod.application.dto.PaymentMethodResponse;
import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentMethodMapper {

    public PaymentMethodResponse toResponse(PaymentMethod entity) {
        return new PaymentMethodResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getMethodName(),
            entity.getMethodCode(),
            entity.isActive()
        );
    }

    public PaymentMethod toEntity(CreatePaymentMethodRequest request) {
        PaymentMethod entity = new PaymentMethod();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public void updateFromRequest(PaymentMethod entity, UpdatePaymentMethodRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getType() != null) entity.setType(request.getType());
        if (request.getIsActive() != null) entity.setActive(request.getIsActive());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}