package com.gogidix.ecommerce.procurement.budget.application.dto;
public record BudgetResponse(String id, String name, boolean active) {
    public static BudgetResponse from(BudgetDto dto) {
        return new BudgetResponse(dto.id(), dto.name(), dto.active());
    }
}
