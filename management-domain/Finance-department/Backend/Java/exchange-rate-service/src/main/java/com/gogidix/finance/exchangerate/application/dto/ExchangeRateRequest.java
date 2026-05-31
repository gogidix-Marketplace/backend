package com.gogidix.finance.exchangerate.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequest {
    private String baseCurrency;
    private String quoteCurrency;
    private BigDecimal rate;
    private String source;
}
