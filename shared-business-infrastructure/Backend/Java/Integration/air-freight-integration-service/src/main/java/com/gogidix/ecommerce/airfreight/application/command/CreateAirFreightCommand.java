package com.gogidix.ecommerce.airfreight.application.command;

public record CreateAirFreightCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
