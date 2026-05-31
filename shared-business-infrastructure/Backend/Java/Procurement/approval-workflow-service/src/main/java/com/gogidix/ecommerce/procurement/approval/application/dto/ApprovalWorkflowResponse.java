package com.gogidix.ecommerce.procurement.approval.application.dto;
public record ApprovalWorkflowResponse(String id, String name, boolean active) {
    public static ApprovalWorkflowResponse from(ApprovalWorkflowDto dto) {
        return new ApprovalWorkflowResponse(dto.id(), dto.name(), dto.active());
    }
}
