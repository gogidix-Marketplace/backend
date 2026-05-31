package com.gogidix.digitalmarketing.budgetmanagement.application.mapper;

import com.gogidix.digitalmarketing.budgetmanagement.domain.model.Budget;
import com.gogidix.digitalmarketing.budgetmanagement.application.dto.BudgetRequestDto;
import com.gogidix.digitalmarketing.budgetmanagement.application.dto.BudgetResponseDto;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {

    public Budget toEntity(BudgetRequestDto dto) {
        return Budget.builder()
            .tenantId(dto.getTenantId())
            .name(dto.getName())
            .fiscalYear(dto.getFiscalYear())
            .status(dto.getStatus())
            .currency(dto.getCurrency())
            .budgetCategory(dto.getBudgetCategory())
            .country(dto.getCountry())
            .department(dto.getDepartment())
            .build();
    }

    public BudgetResponseDto toResponseDto(Budget entity) {
        return BudgetResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .name(entity.getName())
            .fiscalYear(entity.getFiscalYear())
            .totalAmount(entity.getTotalAmount() != null ? entity.getTotalAmount().toString() : null)
            .allocatedAmount(entity.getAllocatedAmount() != null ? entity.getAllocatedAmount().toString() : null)
            .status(entity.getStatus())
            .currency(entity.getCurrency())
            .budgetCategory(entity.getBudgetCategory())
            .country(entity.getCountry())
            .department(entity.getDepartment())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
