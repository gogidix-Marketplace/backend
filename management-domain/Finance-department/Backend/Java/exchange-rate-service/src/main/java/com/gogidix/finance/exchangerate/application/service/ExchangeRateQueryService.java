package com.gogidix.finance.exchangerate.application.service;

import com.gogidix.finance.exchangerate.application.dto.ExchangeRateResponse;
import com.gogidix.finance.exchangerate.application.mapper.ExchangeRateMapper;
import com.gogidix.finance.exchangerate.domain.model.ExchangeRate;
import com.gogidix.finance.exchangerate.domain.port.out.RateCachePort;
import com.gogidix.finance.exchangerate.domain.repository.ExchangeRateRepository;
import com.gogidix.finance.exchangerate.shared.exception.ExchangeRateNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateQueryService {

    private final ExchangeRateRepository repository;
    private final ExchangeRateMapper mapper;
    private final RateCachePort cachePort;

    public ExchangeRateResponse getRate(String baseCurrency, String quoteCurrency, String tenantId) {
        String pair = baseCurrency + "/" + quoteCurrency;

        return cachePort.getCachedRate(pair)
                .map(cached -> buildResponse(pair, cached))
                .orElseGet(() -> {
                    ExchangeRate rate = repository.findByBaseCurrencyAndQuoteCurrencyAndTenantId(
                            baseCurrency, quoteCurrency, tenantId)
                            .orElseThrow(() -> new ExchangeRateNotFoundException(
                                    "No rate found for " + pair));

                    cachePort.cacheRate(pair, rate.getRate(), 300);
                    return mapper.toResponse(rate);
                });
    }

    public ExchangeRateResponse getRate(String baseCurrency, String quoteCurrency) {
        return getRate(baseCurrency, quoteCurrency, "default");
    }

    public ExchangeRateResponse getRateById(String id) {
        ExchangeRate rate = repository.findById(id)
                .orElseThrow(() -> new ExchangeRateNotFoundException("Rate not found: " + id));
        return mapper.toResponse(rate);
    }

    public List<ExchangeRateResponse> getHistoricalRates(String baseCurrency, String quoteCurrency,
                                                         Instant start, Instant end) {
        List<ExchangeRate> rates = repository.findAll().stream()
                .filter(r -> r.getBaseCurrency().equals(baseCurrency))
                .filter(r -> r.getQuoteCurrency().equals(quoteCurrency))
                .filter(r -> r.getUpdatedAt().isAfter(start) && r.getUpdatedAt().isBefore(end))
                .toList();
        return rates.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public BigDecimal convertAmount(BigDecimal amount, String fromCurrency, String toCurrency) {
        ExchangeRateResponse rate = getRate(fromCurrency, toCurrency);
        return amount.multiply(rate.getRate());
    }

    private ExchangeRateResponse buildResponse(String pair, BigDecimal rate) {
        String[] currencies = pair.split("/");
        return ExchangeRateResponse.builder()
                .id(UUID.randomUUID().toString())
                .baseCurrency(currencies[0])
                .quoteCurrency(currencies[1])
                .rate(rate)
                .source("CACHE")
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}
