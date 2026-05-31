package com.gogidix.finance.currency.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class CurrencyConvertedEvent extends DomainEvent {

    private String conversionId;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private BigDecimal exchangeRate;
    private String referenceId;

    public CurrencyConvertedEvent() {}

    public CurrencyConvertedEvent(String tenantId, String correlationId, String conversionId,
                                   String fromCurrency, String toCurrency, BigDecimal fromAmount,
                                   BigDecimal toAmount, BigDecimal exchangeRate, String referenceId) {
        super(correlationId, "CURRENCY_CONVERTED", tenantId, java.time.Instant.now());
        this.conversionId = conversionId;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.exchangeRate = exchangeRate;
        this.referenceId = referenceId;
    }
}
