package com.gogidix.shared.courier.pricing.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceQuote {

    private String quoteId;
    private String tenantId;
    private String serviceType;
    private Double totalPrice;
    private String currency;
    private Map<String, Object> breakdown;
    private LocalDateTime validUntil;
}
