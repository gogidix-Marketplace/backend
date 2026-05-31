package com.gogidix.globalbusinessmanagement.currencyconversion.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "currency_conversions")
public class CurrencyConversion {

    @Id
    private String id;
    private String tenantId;
    private String fromCurrency;
    private String toCurrency;
    private String rate;
    private String source;
    private String effectiveDate;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    public CurrencyConversion(String tenantId) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
