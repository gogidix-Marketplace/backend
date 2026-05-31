package com.gogidix.finance.exchangerate.application.service;

import com.gogidix.finance.exchangerate.application.dto.ExchangeRateRequest;
import com.gogidix.finance.exchangerate.application.dto.ExchangeRateResponse;
import com.gogidix.finance.exchangerate.application.mapper.ExchangeRateMapper;
import com.gogidix.finance.exchangerate.domain.event.ExchangeRateEvent;
import com.gogidix.finance.exchangerate.domain.model.ExchangeRate;
import com.gogidix.finance.exchangerate.domain.port.in.ExchangeRateCommandPort;
import com.gogidix.finance.exchangerate.domain.port.out.EventPublisher;
import com.gogidix.finance.exchangerate.domain.port.out.RateCachePort;
import com.gogidix.finance.exchangerate.domain.repository.ExchangeRateRepository;
import com.gogidix.finance.exchangerate.shared.exception.ExchangeRateNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateCommandService implements ExchangeRateCommandPort {

    private final ExchangeRateRepository repository;
    private final ExchangeRateMapper mapper;
    private final EventPublisher eventPublisher;
    private final RateCachePort cachePort;

    @Override
    public ExchangeRateResponse createRate(ExchangeRateRequest request) {
        ExchangeRate rate = mapper.toDomain(request);
        rate = repository.save(rate);

        evictCache(rate);

        publishEvent("RATE_CREATED", rate);
        log.info("Created exchange rate: {}", rate.getId());

        return mapper.toResponse(rate);
    }

    @Override
    public ExchangeRateResponse updateRate(String id, ExchangeRateRequest request) {
        ExchangeRate existing = repository.findById(id)
                .orElseThrow(() -> new ExchangeRateNotFoundException("Rate not found: " + id));

        // Update the existing entity
        existing.updateRate(request.getRate(), request.getSource(), existing.getQuality());

        evictCache(existing);

        publishEvent("RATE_UPDATED", existing);
        log.info("Updated exchange rate: {}", id);

        return mapper.toResponse(existing);
    }

    @Override
    public void deleteRate(String id) {
        ExchangeRate rate = repository.findById(id)
                .orElseThrow(() -> new ExchangeRateNotFoundException("Rate not found: " + id));

        repository.delete(rate);
        evictCache(rate);

        publishEvent("RATE_DELETED", rate);
        log.info("Deleted exchange rate: {}", id);
    }

    @Override
    public List<ExchangeRateResponse> importRates(List<ExchangeRateRequest> requests) {
        List<ExchangeRate> rates = requests.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        List<ExchangeRate> saved = repository.saveAll(rates);
        saved.forEach(this::evictCache);

        log.info("Imported {} exchange rates", saved.size());
        return saved.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    private void evictCache(ExchangeRate rate) {
        String cacheKey = rate.getBaseCurrency() + "/" + rate.getQuoteCurrency();
        // In-memory cache eviction would happen here
    }

    private void publishEvent(String eventType, ExchangeRate rate) {
        ExchangeRateEvent event = new ExchangeRateEvent(
                UUID.randomUUID().toString(),
                eventType,
                rate.getCurrencyPair(),
                rate.getRate(),
                rate.getSource(),
                LocalDateTime.ofInstant(rate.getUpdatedAt(), ZoneId.systemDefault()),
                UUID.randomUUID().toString()
        );
        eventPublisher.publish(event);
    }
}
