package com.gogidix.ecommerce.email.application.mapper;

import com.gogidix.ecommerce.email.application.dto.*;
import com.gogidix.ecommerce.email.domain.model.Email;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public EmailResponse toResponse(Email entity) {
        if (entity == null) return null;
        return new EmailResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getRecipient(),
            entity.getSubject(),
            entity.getStatus(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Email toEntity(CreateEmailRequest request) {
        Email entity = new Email();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setRecipient(request.recipient());
        entity.setSubject(request.subject());
        entity.setStatus(request.status());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Email entity, UpdateEmailRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.recipient() != null) entity.setRecipient(request.recipient());
        if (request.subject() != null) entity.setSubject(request.subject());
        if (request.status() != null) entity.setStatus(request.status());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
