package com.gogidix.sales.revenue.application.mapper;

import com.gogidix.sales.revenue.domain.model.Revenue;
import com.gogidix.sales.revenue.domain.model.Revenue.RevenueType;
import com.gogidix.sales.revenue.application.dto.RevenueRequestDto;
import com.gogidix.sales.revenue.application.dto.RevenueResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RevenueMapper {

    public Revenue toEntity(RevenueRequestDto dto) {
        return Revenue.builder()
            .tenantId(dto.getTenantId())
            .contractId(dto.getContractId())
            .customerId(dto.getCustomerId())
            .customerName(dto.getCustomerName())
            .productId(dto.getProductId())
            .productName(dto.getProductName())
            .territory(dto.getTerritory())
            .region(dto.getRegion())
            .revenueType(dto.getRevenueType() != null ? RevenueType.valueOf(dto.getRevenueType()) : null)
            .totalAmount(dto.getTotalAmount())
            .build();
    }

    public RevenueResponseDto toResponseDto(Revenue entity) {
        return RevenueResponseDto.builder()
            .id(entity.getId())
            .revenueId(entity.getRevenueId())
            .tenantId(entity.getTenantId())
            .contractId(entity.getContractId())
            .customerId(entity.getCustomerId())
            .customerName(entity.getCustomerName())
            .productId(entity.getProductId())
            .productName(entity.getProductName())
            .territory(entity.getTerritory())
            .region(entity.getRegion())
            .revenueType(entity.getRevenueType() != null ? entity.getRevenueType().name() : null)
            .totalAmount(entity.getTotalAmount())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}