package com.gogidix.globalbusinessmanagement.currencyconversion.application.mapper;

import com.gogidix.globalbusinessmanagement.currencyconversion.domain.model.CurrencyConversion;
import com.gogidix.globalbusinessmanagement.currencyconversion.application.dto.CurrencyConversionRequestDto;
import com.gogidix.globalbusinessmanagement.currencyconversion.application.dto.CurrencyConversionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConversionMapper {

    public CurrencyConversion toEntity(CurrencyConversionRequestDto dto) {
        return CurrencyConversion.builder()
            .tenantId(dto.getTenantId())
            .fromCurrency(dto.getFromCurrency())
            .toCurrency(dto.getToCurrency())
            .rate(dto.getRate())
            .source(dto.getSource())
            .effectiveDate(dto.getEffectiveDate())
            .status(dto.getStatus())
            .build();
    }

    public CurrencyConversionResponseDto toResponseDto(CurrencyConversion entity) {
        return CurrencyConversionResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .fromCurrency(entity.getFromCurrency())
            .toCurrency(entity.getToCurrency())
            .rate(entity.getRate())
            .source(entity.getSource())
            .effectiveDate(entity.getEffectiveDate())
            .status(entity.getStatus())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
