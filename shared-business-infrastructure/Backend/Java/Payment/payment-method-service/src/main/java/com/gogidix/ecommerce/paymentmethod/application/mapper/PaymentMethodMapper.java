package com.gogidix.ecommerce.paymentmethod.application.mapper;

import com.gogidix.ecommerce.paymentmethod.application.dto.*;
import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper {

    public PaymentMethodResponse toResponse(PaymentMethod entity) {
        if (entity == null) return null;
        return new PaymentMethodResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getMethodName(),
            entity.getMethodCode(),
            entity.getEnabled(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public PaymentMethod toEntity(CreatePaymentMethodRequest request) {
        PaymentMethod entity = new PaymentMethod();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setMethodName(request.methodName());
        entity.setMethodCode(request.methodCode());
        entity.setEnabled(request.enabled());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(PaymentMethod entity, UpdatePaymentMethodRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.methodName() != null) entity.setMethodName(request.methodName());
        if (request.methodCode() != null) entity.setMethodCode(request.methodCode());
        if (request.enabled() != null) entity.setEnabled(request.enabled());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
