package com.gogidix.ecommerce.sms.application.mapper;

import com.gogidix.ecommerce.sms.application.dto.CreateSmsRequest;
import com.gogidix.ecommerce.sms.application.dto.UpdateSmsRequest;
import com.gogidix.ecommerce.sms.application.dto.SmsResponse;
import com.gogidix.ecommerce.sms.domain.model.Sms;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SmsMapper {

    public SmsResponse toResponse(Sms entity) {
        return new SmsResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getPhoneNumber(),
            entity.getMessage(),
            entity.getDeliveryStatus()
        );
    }

    public Sms toEntity(CreateSmsRequest request) {
        Sms entity = new Sms();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public void updateFromRequest(Sms entity, UpdateSmsRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getType() != null) entity.setType(request.getType());
        if (request.getIsActive() != null) entity.setActive(request.getIsActive());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}