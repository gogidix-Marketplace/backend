package com.gogidix.ecommerce.airfreight.application.command;

public record DeleteAirFreightCommand(
    String tenantId,
    String id
) {}
