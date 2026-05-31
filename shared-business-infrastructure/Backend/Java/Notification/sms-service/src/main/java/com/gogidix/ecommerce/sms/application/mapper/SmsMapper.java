package com.gogidix.ecommerce.sms.application.mapper;

import com.gogidix.ecommerce.sms.application.dto.*;
import com.gogidix.ecommerce.sms.domain.model.Sms;
import org.springframework.stereotype.Component;

@Component
public class SmsMapper {

    public SmsResponse toResponse(Sms entity) {
        if (entity == null) return null;
        return new SmsResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getPhoneNumber(),
            entity.getMessage(),
            entity.getDeliveryStatus(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Sms toEntity(CreateSmsRequest request) {
        Sms entity = new Sms();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setPhoneNumber(request.phoneNumber());
        entity.setMessage(request.message());
        entity.setDeliveryStatus(request.deliveryStatus());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Sms entity, UpdateSmsRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.phoneNumber() != null) entity.setPhoneNumber(request.phoneNumber());
        if (request.message() != null) entity.setMessage(request.message());
        if (request.deliveryStatus() != null) entity.setDeliveryStatus(request.deliveryStatus());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
