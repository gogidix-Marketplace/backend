package com.gogidix.finance.exchangerate.interfaces.rest;

import com.gogidix.finance.exchangerate.application.dto.ExchangeRateRequest;
import com.gogidix.finance.exchangerate.application.dto.ExchangeRateResponse;
import com.gogidix.finance.exchangerate.application.service.ExchangeRateCommandService;
import com.gogidix.finance.exchangerate.application.service.ExchangeRateQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
@Tag(name = "Exchange Rates", description = "Exchange rate management API")
public class ExchangeRateController {

    private final ExchangeRateQueryService queryService;
    private final ExchangeRateCommandService commandService;

    @GetMapping("/current")
    @Operation(summary = "Get current exchange rate")
    public ResponseEntity<ExchangeRateResponse> getCurrentRate(
            @Parameter(description = "Base currency") @RequestParam String baseCurrency,
            @Parameter(description = "Quote currency") @RequestParam String quoteCurrency) {
        return ResponseEntity.ok(queryService.getRate(baseCurrency, quoteCurrency));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get rate by ID")
    public ResponseEntity<ExchangeRateResponse> getRateById(
            @Parameter(description = "Rate ID") @PathVariable String id) {
        return ResponseEntity.ok(queryService.getRateById(id));
    }

    @GetMapping("/historical")
    @Operation(summary = "Get historical rates")
    public ResponseEntity<List<ExchangeRateResponse>> getHistoricalRates(
            @Parameter(description = "Base currency") @RequestParam String baseCurrency,
            @Parameter(description = "Quote currency") @RequestParam String quoteCurrency,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        Instant startInstant = start.atZone(ZoneId.systemDefault()).toInstant();
        Instant endInstant = end.atZone(ZoneId.systemDefault()).toInstant();
        return ResponseEntity.ok(queryService.getHistoricalRates(baseCurrency, quoteCurrency, startInstant, endInstant));
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert amount between currencies")
    public ResponseEntity<BigDecimal> convertAmount(
            @Parameter(description = "Amount to convert") @RequestParam BigDecimal amount,
            @Parameter(description = "From currency") @RequestParam String fromCurrency,
            @Parameter(description = "To currency") @RequestParam String toCurrency) {
        return ResponseEntity.ok(queryService.convertAmount(amount, fromCurrency, toCurrency));
    }

    @PostMapping
    @Operation(summary = "Create new exchange rate")
    public ResponseEntity<ExchangeRateResponse> createRate(
            @RequestBody ExchangeRateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandService.createRate(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update exchange rate")
    public ResponseEntity<ExchangeRateResponse> updateRate(
            @Parameter(description = "Rate ID") @PathVariable String id,
            @RequestBody ExchangeRateRequest request) {
        return ResponseEntity.ok(commandService.updateRate(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete exchange rate")
    public ResponseEntity<Void> deleteRate(
            @Parameter(description = "Rate ID") @PathVariable String id) {
        commandService.deleteRate(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    @Operation(summary = "Import exchange rates")
    public ResponseEntity<List<ExchangeRateResponse>> importRates(
            @RequestBody List<ExchangeRateRequest> requests) {
        return ResponseEntity.ok(commandService.importRates(requests));
    }
}
