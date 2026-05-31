package com.gogidix.ecommerce.email.application.mapper;

import com.gogidix.ecommerce.email.application.dto.CreateEmailRequest;
import com.gogidix.ecommerce.email.application.dto.UpdateEmailRequest;
import com.gogidix.ecommerce.email.application.dto.EmailResponse;
import com.gogidix.ecommerce.email.domain.model.Email;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailMapper {

    public EmailResponse toResponse(Email entity) {
        return new EmailResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getRecipient(),
            entity.getSubject(),
            entity.getBodyTemplate(),
            entity.getStatus()
        );
    }

    public Email toEntity(CreateEmailRequest request) {
        Email entity = new Email();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public void updateFromRequest(Email entity, UpdateEmailRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getType() != null) entity.setType(request.getType());
        if (request.getIsActive() != null) entity.setActive(request.getIsActive());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}