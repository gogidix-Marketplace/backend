package com.gogidix.ecommerce.communication.application.command;

public record DeleteCommunicationCommand(
    String tenantId,
    String id
) {}
