package com.gogidix.shared.courier.pricing.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Price Quote Entity
 * Temporary quotes with validity period
 * Generated for customers before booking confirmation
 */
@Document(collection = "price_quotes")
@CompoundIndex(def = "{'tenantId': 1, 'quoteId': 1}", unique = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceQuote {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String quoteId;

    private String customerId;

    private String serviceType;

    private String vehicleType;

    private BigDecimal totalAmount;

    private String currency;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @Indexed
    private Boolean expired;

    @Indexed
    private Boolean accepted;
}
