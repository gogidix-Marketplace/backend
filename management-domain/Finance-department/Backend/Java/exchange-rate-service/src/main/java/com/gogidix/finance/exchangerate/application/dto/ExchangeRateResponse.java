package com.gogidix.finance.exchangerate.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    private String id;
    private String baseCurrency;
    private String quoteCurrency;
    private BigDecimal rate;
    private String source;
    private LocalDateTime timestamp;
}
