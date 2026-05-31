package com.gogidix.ecommerce.orchestrator.application.dto;

public record FulfillmentOrchestratorResponse(String id, String name, boolean active) {
    public static FulfillmentOrchestratorResponse from(FulfillmentOrchestratorDto dto) {
        return new FulfillmentOrchestratorResponse(dto.id(), dto.name(), dto.active());
    }
}
