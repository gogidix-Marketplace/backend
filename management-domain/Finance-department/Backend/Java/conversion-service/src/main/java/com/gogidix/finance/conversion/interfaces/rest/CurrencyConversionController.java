package com.gogidix.finance.conversion.interfaces.rest;

import com.gogidix.finance.conversion.application.dto.response.BatchConversionResponseDto;
import com.gogidix.finance.conversion.application.dto.response.ConversionResponseDto;
import com.gogidix.finance.conversion.application.dto.response.ErrorResponseDto;
import com.gogidix.finance.conversion.application.service.ConversionQueryService;
import com.gogidix.finance.conversion.application.service.CurrencyConversionService;
import com.gogidix.finance.conversion.domain.model.ConversionRate;
import com.gogidix.finance.conversion.domain.model.CurrencyConversion;
import com.gogidix.finance.conversion.domain.port.in.CurrencyConversionCommand;
import com.gogidix.finance.conversion.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Currency Conversion REST Controller
 * Handles HTTP requests for currency conversion operations
 */
@RestController
@RequestMapping("/conversions")
@RequiredArgsConstructor
@Tag(name = "Currency Conversions", description = "Currency conversion endpoints")
public class CurrencyConversionController {

    private final CurrencyConversionService conversionService;
    private final ConversionQueryService queryService;

    @PostMapping
    @Operation(summary = "Convert amount between currencies")
    public ResponseEntity<ConversionResponseDto> convertAmount(
            @Valid @RequestBody ConvertAmountRequestDto request) {

        CurrencyConversionCommand.ConvertAmountCommand command =
                new CurrencyConversionCommand.ConvertAmountCommand(
                        RequestContextHolder.getTenantId(),
                        RequestContextHolder.getUserId(),
                        request.getAmount(),
                        request.getFromCurrency(),
                        request.getToCurrency(),
                        UUID.randomUUID().toString(),
                        request.getProvider(),
                        request.getApplyFee(),
                        request.getFeePercentage()
                );

        CurrencyConversion conversion = conversionService.convertAmount(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(conversion));
    }

    @PostMapping("/batch")
    @Operation(summary = "Batch convert amounts between currencies")
    public ResponseEntity<BatchConversionResponseDto> batchConvert(
            @Valid @RequestBody BatchConvertRequestDto request) {

        String correlationId = UUID.randomUUID().toString();
        String batchId = "BATCH-" + Instant.now().toEpochMilli();

        // Convert DTOs to command items
        List<CurrencyConversionCommand.BatchConvertCommand.ConversionItem> conversionItems =
                request.getConversions().stream()
                        .map(dto -> new CurrencyConversionCommand.BatchConvertCommand.ConversionItem(
                                dto.getAmount(),
                                dto.getFromCurrency(),
                                dto.getToCurrency(),
                                dto.getReference()
                        ))
                        .toList();

        CurrencyConversionCommand.BatchConvertCommand command =
                new CurrencyConversionCommand.BatchConvertCommand(
                        RequestContextHolder.getTenantId(),
                        RequestContextHolder.getUserId(),
                        conversionItems,
                        correlationId,
                        request.getProvider(),
                        request.getApplyFee(),
                        request.getFeePercentage()
                );

        CurrencyConversionService.BatchConversionResult result =
                conversionService.batchConvert(command);

        List<ConversionResponseDto> conversionDtos = result.successfulConversions().stream()
                .map(this::toDto)
                .toList();

        List<BatchConversionResponseDto.BatchError> errors = result.failedConversions().stream()
                .map(f -> BatchConversionResponseDto.BatchError.builder()
                        .amount(f.getAmount())
                        .fromCurrency(f.getFromCurrency())
                        .toCurrency(f.getToCurrency())
                        .errorMessage(f.getFailureReason())
                        .build())
                .toList();

        BatchConversionResponseDto response = result.hasFailures()
                ? BatchConversionResponseDto.withErrors(batchId, correlationId, conversionDtos, errors)
                : BatchConversionResponseDto.success(batchId, correlationId, conversionDtos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{conversionId}")
    @Operation(summary = "Get conversion by ID")
    public ResponseEntity<ConversionResponseDto> getConversion(
            @Parameter(description = "Conversion ID") @PathVariable String conversionId) {

        CurrencyConversion conversion = queryService.getConversionById(conversionId);
        return ResponseEntity.ok(toDto(conversion));
    }

    @GetMapping
    @Operation(summary = "Get all conversions for current user")
    public ResponseEntity<List<ConversionResponseDto>> getMyConversions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "conversionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<CurrencyConversion> conversions = queryService.getConversionsByUser(
                RequestContextHolder.getUserId(), page, size, sortBy, sortDirection);

        return ResponseEntity.ok(conversions.stream()
                .map(this::toDto)
                .toList());
    }

    @GetMapping("/pair/{fromCurrency}/{toCurrency}")
    @Operation(summary = "Get conversions by currency pair")
    public ResponseEntity<List<ConversionResponseDto>> getConversionsByPair(
            @Parameter(description = "Source currency") @PathVariable String fromCurrency,
            @Parameter(description = "Target currency") @PathVariable String toCurrency,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<CurrencyConversion> conversions = queryService.getConversionsByCurrencies(
                fromCurrency, toCurrency, page, size);

        return ResponseEntity.ok(conversions.stream()
                .map(this::toDto)
                .toList());
    }

    @PostMapping("/{conversionId}/reverse")
    @Operation(summary = "Reverse a conversion")
    public ResponseEntity<Void> reverseConversion(
            @Parameter(description = "Conversion ID") @PathVariable String conversionId,
            @RequestBody ReverseConversionRequestDto request) {

        CurrencyConversionCommand.ReverseConversionCommand command =
                new CurrencyConversionCommand.ReverseConversionCommand(
                        RequestContextHolder.getTenantId(),
                        conversionId,
                        RequestContextHolder.getUserId(),
                        request.getReason()
                );

        conversionService.reverseConversion(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rates/refresh")
    @Operation(summary = "Refresh exchange rate cache")
    public ResponseEntity<ConversionRate> refreshRate(
            @RequestBody RefreshRateRequestDto request) {

        CurrencyConversionCommand.RefreshRateCommand command =
                new CurrencyConversionCommand.RefreshRateCommand(
                        RequestContextHolder.getTenantId(),
                        request.getFromCurrency(),
                        request.getToCurrency(),
                        request.getProvider()
                );

        ConversionRate rate = conversionService.refreshRate(command);
        return ResponseEntity.ok(rate);
    }

    @DeleteMapping("/rates/cache")
    @Operation(summary = "Invalidate cached rate")
    public ResponseEntity<Void> invalidateRateCache(
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {

        CurrencyConversionCommand.InvalidateRateCacheCommand command =
                new CurrencyConversionCommand.InvalidateRateCacheCommand(
                        RequestContextHolder.getTenantId(),
                        fromCurrency,
                        toCurrency
                );

        conversionService.invalidateRateCache(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rates")
    @Operation(summary = "Get current exchange rate")
    public ResponseEntity<ConversionRate> getRate(
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency,
            @RequestParam(defaultValue = "false") boolean forceRefresh) {

        ConversionRate rate = queryService.getConversionRate(fromCurrency, toCurrency, forceRefresh);
        return ResponseEntity.ok(rate);
    }

    @GetMapping("/calculate")
    @Operation(summary = "Calculate conversion without saving")
    public ResponseEntity<ConversionQueryService.CalculationResult> calculateConversion(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {

        ConversionQueryService.CalculationResult result =
                queryService.calculateConversion(amount, fromCurrency, toCurrency);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get conversion statistics")
    public ResponseEntity<ConversionQueryService.ConversionStatistics> getStatistics(
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate,
            @RequestParam(required = false) String fromCurrency,
            @RequestParam(required = false) String toCurrency) {

        Instant start = startDate != null ? Instant.ofEpochMilli(startDate) : Instant.now().minusSeconds(86400);
        Instant end = endDate != null ? Instant.ofEpochMilli(endDate) : Instant.now();

        ConversionQueryService.ConversionStatistics statistics =
                queryService.getStatistics(start, end, fromCurrency, toCurrency);

        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/currencies")
    @Operation(summary = "Get supported currencies")
    public ResponseEntity<List<String>> getSupportedCurrencies() {
        List<String> currencies = queryService.getSupportedCurrencies();
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/rates/batch")
    @Operation(summary = "Get multiple exchange rates")
    public ResponseEntity<List<ConversionRate>> getBatchRates(
            @RequestParam String baseCurrency,
            @RequestParam List<String> targetCurrencies) {

        List<ConversionRate> rates = queryService.getBatchRates(baseCurrency, targetCurrencies);
        return ResponseEntity.ok(rates);
    }

    private ConversionResponseDto toDto(CurrencyConversion conversion) {
        return ConversionResponseDto.fromEntity(conversion);
    }

    // Request DTOs
    @lombok.Data
    public static class ConvertAmountRequestDto {
        @jakarta.validation.constraints.NotNull(message = "Amount is required")
        @jakarta.validation.constraints.Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @jakarta.validation.constraints.NotBlank(message = "Source currency is required")
        private String fromCurrency;

        @jakarta.validation.constraints.NotBlank(message = "Target currency is required")
        private String toCurrency;

        private String provider;
        private Boolean applyFee;
        private BigDecimal feePercentage;
    }

    @lombok.Data
    public static class BatchConvertRequestDto {
        @jakarta.validation.constraints.NotEmpty(message = "Conversions list cannot be empty")
        private List<ConversionItemDto> conversions;
        private String provider;
        private Boolean applyFee;
        private BigDecimal feePercentage;

        @lombok.Data
        public static class ConversionItemDto {
            @jakarta.validation.constraints.NotNull(message = "Amount is required")
            @jakarta.validation.constraints.Positive(message = "Amount must be positive")
            private BigDecimal amount;

            @jakarta.validation.constraints.NotBlank(message = "Source currency is required")
            private String fromCurrency;

            @jakarta.validation.constraints.NotBlank(message = "Target currency is required")
            private String toCurrency;

            private String reference;
        }
    }

    @lombok.Data
    public static class ReverseConversionRequestDto {
        private String reason;
    }

    @lombok.Data
    public static class RefreshRateRequestDto {
        @jakarta.validation.constraints.NotBlank(message = "Source currency is required")
        private String fromCurrency;

        @jakarta.validation.constraints.NotBlank(message = "Target currency is required")
        private String toCurrency;

        private String provider;
    }
}
