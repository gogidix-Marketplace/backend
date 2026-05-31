package com.gogidix.ecommerce.communication.application.dto;

import java.time.Instant;

public record CommunicationResponse(
    String id,
    String name,
    String description,
    String type,
    String channel,
    String messageType,
    String content,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
