package com.gogidix.ecommerce.airfreight.application.command;

public record UpdateAirFreightCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
