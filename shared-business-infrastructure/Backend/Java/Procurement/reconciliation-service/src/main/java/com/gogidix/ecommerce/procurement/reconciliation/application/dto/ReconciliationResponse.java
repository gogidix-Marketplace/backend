package com.gogidix.ecommerce.procurement.reconciliation.application.dto;
public record ReconciliationResponse(String id, String name, boolean active) {
    public static ReconciliationResponse from(ReconciliationDto dto) {
        return new ReconciliationResponse(dto.id(), dto.name(), dto.active());
    }
}
