package com.gogidix.finance.currency.interfaces.rest;

import com.gogidix.finance.currency.application.dto.request.*;
import com.gogidix.finance.currency.application.dto.response.*;
import com.gogidix.finance.currency.application.mapper.CurrencyMapper;
import com.gogidix.finance.currency.application.service.*;
import com.gogidix.finance.currency.domain.model.Currency;
import com.gogidix.finance.currency.domain.port.in.CurrencyCommand;
import com.gogidix.finance.currency.domain.port.in.CurrencyQuery;
import com.gogidix.finance.currency.shared.exception.NotFoundException;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * REST Controller - Currency Management
 * Handles HTTP requests for currency operations
 * Following hexagonal architecture principles
 */
@RestController
@RequestMapping("/currencies")
@Tag(name = "Currency Management", description = "CRUD operations for currencies")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Slf4j
public class CurrencyController {

    private final CurrencyCommandService currencyCommandService;
    private final CurrencyQueryService currencyQueryService;
    private final CurrencyMapper currencyMapper;

    @GetMapping
    @Operation(summary = "List all currencies", description = "Returns all currencies for the current tenant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved currencies"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<CurrencyResponseDto>> listCurrencies() {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Listing currencies for tenant: {}", tenantId);

        List<Currency> currencies = currencyQueryService.getAllForTenant(
            new CurrencyQuery.GetAllCurrenciesQuery(tenantId));

        List<CurrencyResponseDto> response = currencies.stream()
            .map(currencyMapper::toResponseDto)
            .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @Operation(summary = "List active currencies", description = "Returns only active currencies")
    public ResponseEntity<List<CurrencyResponseDto>> listActiveCurrencies() {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Listing active currencies for tenant: {}", tenantId);

        List<Currency> currencies = currencyQueryService.getActiveForTenant(
            new CurrencyQuery.GetActiveCurrenciesQuery(tenantId));

        List<CurrencyResponseDto> response = currencies.stream()
            .map(currencyMapper::toResponseDto)
            .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get currency by ID", description = "Returns a single currency by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved currency"),
        @ApiResponse(responseCode = "404", description = "Currency not found")
    })
    public ResponseEntity<CurrencyResponseDto> getCurrency(
        @Parameter(description = "Currency ID") @PathVariable String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Getting currency: {} for tenant: {}", id, tenantId);

        Currency currency = currencyQueryService.getById(
            new CurrencyQuery.GetCurrencyByIdQuery(tenantId, id))
            .orElseThrow(() -> new NotFoundException("Currency not found"));

        return ResponseEntity.ok(currencyMapper.toResponseDto(currency));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get currency by code", description = "Returns a single currency by its ISO code")
    public ResponseEntity<CurrencyResponseDto> getCurrencyByCode(
        @Parameter(description = "Currency ISO code (3 letters)") @PathVariable String code) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Getting currency by code: {} for tenant: {}", code, tenantId);

        Currency currency = currencyQueryService.getByCode(
            new CurrencyQuery.GetCurrencyByCodeQuery(tenantId, code))
            .orElseThrow(() -> new NotFoundException("Currency not found"));

        return ResponseEntity.ok(currencyMapper.toResponseDto(currency));
    }

    @PostMapping
    @Operation(summary = "Create new currency", description = "Creates a new currency for the current tenant")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Currency created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public ResponseEntity<CurrencyResponseDto> createCurrency(
        @Valid @RequestBody CreateCurrencyRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Creating currency: {} for tenant: {}", request.currencyCode(), tenantId);

        Currency currency = currencyCommandService.create(
            new CurrencyCommand.CreateCurrencyCommand(
                tenantId,
                request.currencyCode(),
                request.name(),
                request.symbol(),
                request.decimalPlaces(),
                request.isoNumericCode(),
                request.countryCodes() != null ? request.countryCodes() : new String[0]
            ));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(currencyMapper.toResponseDto(currency));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update currency", description = "Updates an existing currency")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Currency updated successfully"),
        @ApiResponse(responseCode = "404", description = "Currency not found")
    })
    public ResponseEntity<CurrencyResponseDto> updateCurrency(
        @Parameter(description = "Currency ID") @PathVariable String id,
        @Valid @RequestBody UpdateCurrencyRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Updating currency: {} for tenant: {}", id, tenantId);

        Currency currency = currencyCommandService.update(
            new CurrencyCommand.UpdateCurrencyCommand(
                tenantId,
                id,
                request.name(),
                request.symbol(),
                request.decimalPlaces(),
                request.isoNumericCode()
            ));

        return ResponseEntity.ok(currencyMapper.toResponseDto(currency));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate currency", description = "Activates a deactivated currency")
    public ResponseEntity<Void> activateCurrency(
        @Parameter(description = "Currency ID") @PathVariable String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Activating currency: {} for tenant: {}", id, tenantId);

        currencyCommandService.activate(
            new CurrencyCommand.ActivateCurrencyCommand(tenantId, id));

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate currency", description = "Deactivates a currency")
    public ResponseEntity<Void> deactivateCurrency(
        @Parameter(description = "Currency ID") @PathVariable String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Deactivating currency: {} for tenant: {}", id, tenantId);

        currencyCommandService.deactivate(
            new CurrencyCommand.DeactivateCurrencyCommand(tenantId, id));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete currency", description = "Deletes a currency")
    @ApiResponse(responseCode = "204", description = "Currency deleted successfully")
    public ResponseEntity<Void> deleteCurrency(
        @Parameter(description = "Currency ID") @PathVariable String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Deleting currency: {} for tenant: {}", id, tenantId);

        currencyCommandService.delete(
            new CurrencyCommand.DeleteCurrencyCommand(tenantId, id));

        return ResponseEntity.noContent().build();
    }
}
