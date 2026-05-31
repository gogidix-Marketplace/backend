package com.gogidix.ecommerce.communication.application.command;

public record UpdateCommunicationCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
