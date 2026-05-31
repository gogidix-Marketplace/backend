package com.gogidix.ecommerce.procurement.requisition.application.dto;
public record RequisitionResponse(String id, String name, boolean active) {
    public static RequisitionResponse from(RequisitionDto dto) {
        return new RequisitionResponse(dto.id(), dto.name(), dto.active());
    }
}
