package com.gogidix.finance.exchangerate.application.mapper;

import com.gogidix.finance.exchangerate.application.dto.ExchangeRateRequest;
import com.gogidix.finance.exchangerate.application.dto.ExchangeRateResponse;
import com.gogidix.finance.exchangerate.domain.model.ExchangeRate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class ExchangeRateMapper {

    public ExchangeRate toDomain(ExchangeRateRequest request) {
        return ExchangeRate.builder()
                .baseCurrency(request.getBaseCurrency())
                .quoteCurrency(request.getQuoteCurrency())
                .rate(request.getRate())
                .source(request.getSource())
                .build();
    }

    public ExchangeRateResponse toResponse(ExchangeRate rate) {
        return ExchangeRateResponse.builder()
                .id(rate.getId())
                .baseCurrency(rate.getBaseCurrency())
                .quoteCurrency(rate.getQuoteCurrency())
                .rate(rate.getRate())
                .source(rate.getSource())
                .timestamp(LocalDateTime.ofInstant(rate.getUpdatedAt(), ZoneId.systemDefault()))
                .build();
    }
}
