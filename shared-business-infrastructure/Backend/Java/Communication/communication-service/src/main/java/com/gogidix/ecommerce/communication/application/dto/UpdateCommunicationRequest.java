package com.gogidix.ecommerce.communication.application.dto;

public record UpdateCommunicationRequest(
    String name,
    String description,
    String type,
    String channel,
    String messageType,
    String content,
    Boolean isActive
) {}
