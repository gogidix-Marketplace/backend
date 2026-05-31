package com.gogidix.globalbusinessmanagement.multicurrency.interfaces.rest;

import com.gogidix.globalbusinessmanagement.multicurrency.application.service.CurrencyConversionService;
import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.Currency;
import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/multi-currency")
@RequiredArgsConstructor
public class MultiCurrencyController {

    private final CurrencyConversionService currencyConversionService;

    @GetMapping("/currencies")
    public ResponseEntity<List<Currency>> getSupportedCurrencies() {
        return ResponseEntity.ok(currencyConversionService.getSupportedCurrencies());
    }

    @GetMapping("/currencies/{code}/supported")
    public ResponseEntity<Map<String, Boolean>> isCurrencySupported(@PathVariable String code) {
        return ResponseEntity.ok(Map.of("supported", currencyConversionService.isCurrencySupported(code)));
    }

    @GetMapping("/convert")
    public ResponseEntity<Map<String, Object>> convert(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestParam String to) {
        BigDecimal result = currencyConversionService.convert(amount, from, to);
        return ResponseEntity.ok(Map.of("from", from, "to", to, "amount", amount, "result", result));
    }

    @GetMapping("/convert/as-of-date")
    public ResponseEntity<Map<String, Object>> convertAsOfDate(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam Instant date) {
        BigDecimal result = currencyConversionService.convertAsOfDate(amount, from, to, date);
        return ResponseEntity.ok(Map.of("from", from, "to", to, "amount", amount, "result", result, "asOfDate", date));
    }

    @GetMapping("/rates")
    public ResponseEntity<BigDecimal> getExchangeRate(
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(currencyConversionService.getExchangeRate(from, to));
    }

    @GetMapping("/rates/all")
    public ResponseEntity<Map<String, BigDecimal>> getAllExchangeRates(@RequestParam String base) {
        return ResponseEntity.ok(currencyConversionService.getAllExchangeRates(base));
    }

    @GetMapping("/rates/historical")
    public ResponseEntity<List<ExchangeRate>> getHistoricalRates(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = "30") int limit) {
        return ResponseEntity.ok(currencyConversionService.getHistoricalRates(from, to, limit));
    }

    @PostMapping("/rates")
    public ResponseEntity<ExchangeRate> createExchangeRate(@RequestBody ExchangeRate exchangeRate) {
        return ResponseEntity.ok(currencyConversionService.createOrUpdateExchangeRate(exchangeRate));
    }

    @PostMapping("/rates/batch")
    public ResponseEntity<Map<String, BigDecimal>> getBatchRates(@RequestBody List<String> currencyPairs) {
        return ResponseEntity.ok(currencyConversionService.getBatchExchangeRates(currencyPairs));
    }

    @PostMapping("/convert/multiple")
    public ResponseEntity<Map<String, BigDecimal>> convertToMultiple(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestBody List<String> toCurrencies) {
        return ResponseEntity.ok(currencyConversionService.convertToMultiple(amount, from, toCurrencies));
    }

    @GetMapping("/cross-rate")
    public ResponseEntity<Map<String, Object>> calculateCrossRate(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String base) {
        BigDecimal rate = currencyConversionService.calculateCrossRate(from, to, base);
        return ResponseEntity.ok(Map.of("from", from, "to", to, "viaBase", base, "rate", rate));
    }

    @DeleteMapping("/cache")
    public ResponseEntity<Void> invalidateCache() {
        currencyConversionService.invalidateAllCache();
        return ResponseEntity.noContent().build();
    }
}
