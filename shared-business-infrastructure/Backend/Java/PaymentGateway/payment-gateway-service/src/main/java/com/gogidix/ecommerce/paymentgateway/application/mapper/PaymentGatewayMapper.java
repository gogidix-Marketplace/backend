package com.gogidix.ecommerce.paymentgateway.application.mapper;

import com.gogidix.ecommerce.paymentgateway.application.dto.CreatePaymentGatewayRequest;
import com.gogidix.ecommerce.paymentgateway.application.dto.UpdatePaymentGatewayRequest;
import com.gogidix.ecommerce.paymentgateway.application.dto.PaymentGatewayResponse;
import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentGatewayMapper {

    public PaymentGatewayResponse toResponse(PaymentGateway entity) {
        return new PaymentGatewayResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getGatewayName(),
            entity.getGatewayCode(),
            entity.getSupportedCurrencies()
        );
    }

    public PaymentGateway toEntity(CreatePaymentGatewayRequest request) {
        PaymentGateway entity = new PaymentGateway();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public void updateFromRequest(PaymentGateway entity, UpdatePaymentGatewayRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getType() != null) entity.setType(request.getType());
        if (request.getIsActive() != null) entity.setActive(request.getIsActive());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}