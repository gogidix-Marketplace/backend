package com.gogidix.ecommerce.communication.application.command;

public record CreateCommunicationCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
