package com.gogidix.finance.conversion.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.finance.conversion.domain.model.CurrencyConversion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Conversion Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResponseDto {

    private String id;

    private String conversionId;

    private String tenantId;

    private String requestedBy;

    private BigDecimal amount;

    private String fromCurrency;

    private String toCurrency;

    private BigDecimal rate;

    private BigDecimal convertedAmount;

    private ConversionStatusDto status;

    private String provider;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant conversionDate;

    private String reference;

    private String correlationId;

    private String failureReason;

    private BigDecimal fee;

    private BigDecimal totalAmount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    private Boolean reversible;

    public enum ConversionStatusDto {
        PENDING,
        COMPLETED,
        FAILED,
        REVERSED
    }

    /**
     * Converts domain entity to DTO
     */
    public static ConversionResponseDto fromEntity(CurrencyConversion conversion) {
        return ConversionResponseDto.builder()
                .id(conversion.getId())
                .conversionId(conversion.getConversionId())
                .tenantId(conversion.getTenantId())
                .requestedBy(conversion.getRequestedBy())
                .amount(conversion.getAmount())
                .fromCurrency(conversion.getFromCurrency())
                .toCurrency(conversion.getToCurrency())
                .rate(conversion.getRate())
                .convertedAmount(conversion.getConvertedAmount())
                .status(mapStatus(conversion.getStatus()))
                .provider(conversion.getProvider())
                .conversionDate(conversion.getConversionDate())
                .reference(conversion.getReference())
                .correlationId(conversion.getCorrelationId())
                .failureReason(conversion.getFailureReason())
                .fee(conversion.getFee())
                .totalAmount(conversion.getTotalAmount())
                .createdAt(conversion.getCreatedAt())
                .updatedAt(conversion.getUpdatedAt())
                .reversible(conversion.isReversible())
                .build();
    }

    private static ConversionStatusDto mapStatus(CurrencyConversion.ConversionStatus status) {
        return status != null ? ConversionStatusDto.valueOf(status.name()) : null;
    }
}
