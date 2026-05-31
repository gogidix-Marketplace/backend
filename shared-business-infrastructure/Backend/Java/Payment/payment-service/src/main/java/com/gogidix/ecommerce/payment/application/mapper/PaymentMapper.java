package com.gogidix.ecommerce.payment.application.mapper;

import com.gogidix.ecommerce.payment.application.dto.*;
import com.gogidix.ecommerce.payment.domain.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment entity) {
        if (entity == null) return null;
        return new PaymentResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getOrderId(),
            entity.getAmount(),
            entity.getPaymentStatus(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Payment toEntity(CreatePaymentRequest request) {
        Payment entity = new Payment();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setOrderId(request.orderId());
        entity.setAmount(request.amount());
        entity.setPaymentStatus(request.paymentStatus());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Payment entity, UpdatePaymentRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.orderId() != null) entity.setOrderId(request.orderId());
        if (request.amount() != null) entity.setAmount(request.amount());
        if (request.paymentStatus() != null) entity.setPaymentStatus(request.paymentStatus());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
