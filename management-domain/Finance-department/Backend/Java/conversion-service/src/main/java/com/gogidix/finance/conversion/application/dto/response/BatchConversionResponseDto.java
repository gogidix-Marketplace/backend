package com.gogidix.finance.conversion.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Batch Conversion Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchConversionResponseDto {

    private String batchId;

    private String correlationId;

    private int totalCount;

    private int successCount;

    private int failureCount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant processedAt;

    private List<ConversionResponseDto> conversions;

    private List<BatchError> errors;

    private BigDecimal totalOriginalAmount;

    private BigDecimal totalConvertedAmount;

    private String status;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchError {
        private String reference;

        private BigDecimal amount;

        private String fromCurrency;

        private String toCurrency;

        private String errorMessage;
    }

    /**
     * Creates a successful batch response
     */
    public static BatchConversionResponseDto success(String batchId, String correlationId,
                                                      List<ConversionResponseDto> conversions) {
        int successCount = (int) conversions.stream()
                .filter(c -> c.getStatus() == ConversionResponseDto.ConversionStatusDto.COMPLETED)
                .count();

        BigDecimal totalOriginal = conversions.stream()
                .map(ConversionResponseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalConverted = conversions.stream()
                .filter(c -> c.getConvertedAmount() != null)
                .map(ConversionResponseDto::getConvertedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BatchConversionResponseDto.builder()
                .batchId(batchId)
                .correlationId(correlationId)
                .totalCount(conversions.size())
                .successCount(successCount)
                .failureCount(conversions.size() - successCount)
                .processedAt(Instant.now())
                .conversions(conversions)
                .totalOriginalAmount(totalOriginal)
                .totalConvertedAmount(totalConverted)
                .status(successCount == conversions.size() ? "COMPLETED" : "PARTIAL")
                .build();
    }

    /**
     * Creates a batch response with errors
     */
    public static BatchConversionResponseDto withErrors(String batchId, String correlationId,
                                                        List<ConversionResponseDto> conversions,
                                                        List<BatchError> errors) {
        int successCount = conversions.size();

        return BatchConversionResponseDto.builder()
                .batchId(batchId)
                .correlationId(correlationId)
                .totalCount(conversions.size() + errors.size())
                .successCount(successCount)
                .failureCount(errors.size())
                .processedAt(Instant.now())
                .conversions(conversions)
                .errors(errors)
                .status("PARTIAL")
                .build();
    }
}
