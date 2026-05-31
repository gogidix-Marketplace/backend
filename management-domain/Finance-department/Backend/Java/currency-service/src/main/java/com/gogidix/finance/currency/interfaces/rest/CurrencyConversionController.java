package com.gogidix.finance.currency.interfaces.rest;

import com.gogidix.finance.currency.application.dto.request.CalculateConversionRequestDto;
import com.gogidix.finance.currency.application.dto.request.ConvertCurrencyRequestDto;
import com.gogidix.finance.currency.application.dto.response.CurrencyConversionResponseDto;
import com.gogidix.finance.currency.application.mapper.CurrencyMapper;
import com.gogidix.finance.currency.application.service.CurrencyConversionCommandService;
import com.gogidix.finance.currency.application.service.CurrencyConversionQueryService;
import com.gogidix.finance.currency.domain.model.CurrencyConversion;
import com.gogidix.finance.currency.domain.model.Money;
import com.gogidix.finance.currency.domain.port.in.CurrencyConversionCommand;
import com.gogidix.finance.currency.domain.port.in.CurrencyConversionQuery;
import com.gogidix.finance.currency.shared.exception.NotFoundException;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller - Currency Conversion Management
 * Handles HTTP requests for currency conversion operations
 */
@RestController
@RequestMapping("/conversions")
@Tag(name = "Currency Conversion", description = "Currency conversion operations")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Slf4j
public class CurrencyConversionController {

    private final CurrencyConversionCommandService conversionCommandService;
    private final CurrencyConversionQueryService conversionQueryService;
    private final CurrencyMapper currencyMapper;

    @PostMapping
    @Operation(summary = "Convert currency", description = "Performs a currency conversion and saves the record")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @ApiResponse(responseCode = "201", description = "Conversion completed successfully"),
        @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public ResponseEntity<CurrencyConversionResponseDto> convert(
        @Valid @RequestBody ConvertCurrencyRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        String correlationId = RequestContextHolder.getCorrelationId();

        log.info("Converting currency: {} {} to {} for tenant: {}",
            request.fromCurrency(), request.amount(), request.toCurrency(), tenantId);

        CurrencyConversion conversion = conversionCommandService.convert(
            new CurrencyConversionCommand.ConvertCommand(
                tenantId,
                request.fromCurrency(),
                request.toCurrency(),
                request.amount(),
                request.referenceId(),
                correlationId
            ));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(currencyMapper.toResponseDto(conversion));
    }

    @PostMapping("/calculate")
    @Operation(summary = "Calculate conversion", description = "Calculates conversion without saving")
    public ResponseEntity<CalculationResponseDto> calculate(
        @Valid @RequestBody CalculateConversionRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Calculating conversion: {} {} to {} for tenant: {}",
            request.fromCurrency(), request.amount(), request.toCurrency(), tenantId);

        Money result = conversionCommandService.calculate(
            new CurrencyConversionCommand.CalculateConversionCommand(
                tenantId,
                request.fromCurrency(),
                request.toCurrency(),
                request.amount()
            ));

        return ResponseEntity.ok(new CalculationResponseDto(
            request.fromCurrency(),
            request.toCurrency(),
            request.amount(),
            result.getAmount(),
            result.getCurrency()
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get conversion by ID", description = "Returns a conversion record by its ID")
    public ResponseEntity<CurrencyConversionResponseDto> getConversion(
        @Parameter(description = "Conversion ID") @PathVariable String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Getting conversion: {} for tenant: {}", id, tenantId);

        CurrencyConversion conversion = conversionQueryService.getByConversionId(
            new CurrencyConversionQuery.GetByConversionIdQuery(tenantId, id))
            .orElseThrow(() -> new NotFoundException("Conversion not found"));

        return ResponseEntity.ok(currencyMapper.toResponseDto(conversion));
    }

    @GetMapping
    @Operation(summary = "List conversions", description = "Returns all conversions for the current tenant")
    public ResponseEntity<List<CurrencyConversionResponseDto>> listConversions(
        @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Listing conversions for tenant: {}", tenantId);

        List<CurrencyConversion> conversions = conversionQueryService.getPaginated(
            new CurrencyConversionQuery.GetPaginatedConversionsQuery(tenantId, page, size));

        List<CurrencyConversionResponseDto> response = conversions.stream()
            .map(currencyMapper::toResponseDto)
            .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reverse")
    @Operation(summary = "Reverse conversion", description = "Reverses a previous conversion")
    public ResponseEntity<CurrencyConversionResponseDto> reverseConversion(
        @Parameter(description = "Conversion ID") @PathVariable String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Reversing conversion: {} for tenant: {}", id, tenantId);

        CurrencyConversion conversion = conversionCommandService.reverse(
            new CurrencyConversionCommand.ReverseConversionCommand(tenantId, id));

        return ResponseEntity.ok(currencyMapper.toResponseDto(conversion));
    }

    /**
     * Response DTO for calculation endpoint
     */
    public record CalculationResponseDto(
        String fromCurrency,
        String toCurrency,
        BigDecimal fromAmount,
        BigDecimal toAmount,
        String resultCurrency
    ) {}
}
