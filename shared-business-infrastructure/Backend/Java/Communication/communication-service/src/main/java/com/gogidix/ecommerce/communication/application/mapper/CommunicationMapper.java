package com.gogidix.ecommerce.communication.application.mapper;

import com.gogidix.ecommerce.communication.application.dto.*;
import com.gogidix.ecommerce.communication.domain.model.Communication;
import org.springframework.stereotype.Component;

@Component
public class CommunicationMapper {

    public CommunicationResponse toResponse(Communication entity) {
        if (entity == null) return null;
        return new CommunicationResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getChannel(),
            entity.getMessageType(),
            entity.getContent(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Communication toEntity(CreateCommunicationRequest request) {
        Communication entity = new Communication();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setChannel(request.channel());
        entity.setMessageType(request.messageType());
        entity.setContent(request.content());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Communication entity, UpdateCommunicationRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.channel() != null) entity.setChannel(request.channel());
        if (request.messageType() != null) entity.setMessageType(request.messageType());
        if (request.content() != null) entity.setContent(request.content());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
