package com.gogidix.ecommerce.communication.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCommunicationRequest(
    @NotBlank String name,
    String description,
    String type,
    String channel,
    String messageType,
    String content
) {}
