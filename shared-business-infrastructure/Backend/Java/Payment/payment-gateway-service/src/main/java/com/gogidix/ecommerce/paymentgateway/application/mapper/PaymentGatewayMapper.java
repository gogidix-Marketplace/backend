package com.gogidix.ecommerce.paymentgateway.application.mapper;

import com.gogidix.ecommerce.paymentgateway.application.dto.*;
import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayMapper {

    public PaymentGatewayResponse toResponse(PaymentGateway entity) {
        if (entity == null) return null;
        return new PaymentGatewayResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getGatewayName(),
            entity.getGatewayCode(),
            entity.getSupportedCurrencies(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public PaymentGateway toEntity(CreatePaymentGatewayRequest request) {
        PaymentGateway entity = new PaymentGateway();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setGatewayName(request.gatewayName());
        entity.setGatewayCode(request.gatewayCode());
        entity.setSupportedCurrencies(request.supportedCurrencies());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(PaymentGateway entity, UpdatePaymentGatewayRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.gatewayName() != null) entity.setGatewayName(request.gatewayName());
        if (request.gatewayCode() != null) entity.setGatewayCode(request.gatewayCode());
        if (request.supportedCurrencies() != null) entity.setSupportedCurrencies(request.supportedCurrencies());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
