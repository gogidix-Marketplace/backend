package com.gogidix.finance.currency.interfaces.rest;

import com.gogidix.finance.currency.application.dto.request.CreateExchangeRateRequestDto;
import com.gogidix.finance.currency.application.dto.response.ExchangeRateResponseDto;
import com.gogidix.finance.currency.application.mapper.CurrencyMapper;
import com.gogidix.finance.currency.application.service.ExchangeRateCommandService;
import com.gogidix.finance.currency.application.service.ExchangeRateQueryService;
import com.gogidix.finance.currency.domain.model.ExchangeRate;
import com.gogidix.finance.currency.domain.port.in.ExchangeRateCommand;
import com.gogidix.finance.currency.domain.port.in.ExchangeRateQuery;
import com.gogidix.finance.currency.shared.exception.NotFoundException;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller - Exchange Rate Management
 * Handles HTTP requests for exchange rate operations
 */
@RestController
@RequestMapping("/exchange-rates")
@Tag(name = "Exchange Rate Management", description = "CRUD operations for exchange rates")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateController {

    private final ExchangeRateCommandService exchangeRateCommandService;
    private final ExchangeRateQueryService exchangeRateQueryService;
    private final CurrencyMapper currencyMapper;

    @GetMapping
    @Operation(summary = "List all exchange rates", description = "Returns all exchange rates for the current tenant")
    public ResponseEntity<List<ExchangeRateResponseDto>> listExchangeRates() {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Listing exchange rates for tenant: {}", tenantId);

        List<ExchangeRate> rates = exchangeRateQueryService.getAllForTenant(
            new ExchangeRateQuery.GetAllRatesQuery(tenantId));

        List<ExchangeRateResponseDto> response = rates.stream()
            .map(currencyMapper::toResponseDto)
            .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest exchange rates", description = "Returns the latest rate for each currency pair")
    public ResponseEntity<List<ExchangeRateResponseDto>> getLatestRates() {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Getting latest exchange rates for tenant: {}", tenantId);

        List<ExchangeRate> rates = exchangeRateQueryService.getLatestRates(
            new ExchangeRateQuery.GetLatestRatesQuery(tenantId));

        List<ExchangeRateResponseDto> response = rates.stream()
            .map(currencyMapper::toResponseDto)
            .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{base}/{quote}")
    @Operation(summary = "Get exchange rate for pair", description = "Returns the current rate for a currency pair")
    public ResponseEntity<ExchangeRateResponseDto> getExchangeRate(
        @Parameter(description = "Base currency") @PathVariable String base,
        @Parameter(description = "Quote currency") @PathVariable String quote) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Getting exchange rate: {}/{} for tenant: {}", base, quote, tenantId);

        ExchangeRate rate = exchangeRateQueryService.getCurrentRate(
            new ExchangeRateQuery.GetCurrentRateQuery(tenantId, base, quote))
            .orElseThrow(() -> new NotFoundException("Exchange rate not found"));

        return ResponseEntity.ok(currencyMapper.toResponseDto(rate));
    }

    @PostMapping
    @Operation(summary = "Create or update exchange rate", description = "Creates or updates an exchange rate")
    public ResponseEntity<ExchangeRateResponseDto> createOrUpdateRate(
        @Valid @RequestBody CreateExchangeRateRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Creating/updating exchange rate: {}/{} for tenant: {}",
            request.baseCurrency(), request.quoteCurrency(), tenantId);

        ExchangeRate rate = exchangeRateCommandService.createOrUpdate(
            new ExchangeRateCommand.CreateOrUpdateRateCommand(
                tenantId,
                request.baseCurrency(),
                request.quoteCurrency(),
                request.rate(),
                request.source(),
                request.quality() != null ? request.quality() : ExchangeRate.RateQuality.DAILY
            ));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(currencyMapper.toResponseDto(rate));
    }

    @DeleteMapping("/{base}/{quote}")
    @Operation(summary = "Invalidate exchange rate", description = "Invalidates the current exchange rate for a pair")
    public ResponseEntity<Void> invalidateRate(
        @Parameter(description = "Base currency") @PathVariable String base,
        @Parameter(description = "Quote currency") @PathVariable String quote) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Invalidating exchange rate: {}/{} for tenant: {}", base, quote, tenantId);

        exchangeRateCommandService.invalidate(
            new ExchangeRateCommand.InvalidateRateCommand(tenantId, base, quote));

        return ResponseEntity.noContent().build();
    }
}
