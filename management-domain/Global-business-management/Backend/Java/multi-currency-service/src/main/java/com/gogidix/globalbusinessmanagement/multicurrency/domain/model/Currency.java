package com.gogidix.globalbusinessmanagement.multicurrency.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing a currency with its properties.
 * Stores currency information including codes, symbols, and metadata.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "currencies")
public class Currency {

    @Id
    private String id;

    @NotBlank(message = "Currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid ISO 4217 code")
    @Indexed(unique = true)
    private String code;

    @NotBlank(message = "Currency name is required")
    private String name;

    @NotBlank(message = "Currency symbol is required")
    private String symbol;

    @NotNull(message = "Number of decimal places is required")
    @Positive(message = "Decimal places must be positive")
    private Integer decimalPlaces;

    @NotNull(message = "Currency status is required")
    @Builder.Default
    private CurrencyStatus status = CurrencyStatus.ACTIVE;

    private String numericCode;

    @Indexed
    private String region;

    private List<String> countries;

    private Boolean isCrypto;

    private String blockchainNetwork;

    @Builder.Default
    private Integer minorUnits = 2;

    private String centralBank;

    private String description;

    @Indexed
    private Boolean isDefault;

    @Indexed
    private Integer sortOrder;

    private Instant lastRateUpdate;

    private BigDecimal currentRateToUSD;

    private CurrencyMetadata metadata;

    @LastModifiedDate
    private Instant updatedAt;

    private Instant createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrencyMetadata {
        private String issuer;
        private String issuerWebsite;
        private Map<String, String> localizedNames;
        private List<String> aliases;
        private String currencyFamily;
        private Boolean isFiat;
        private Integer marketCapRank;
        private BigDecimal circulatingSupply;
        private BigDecimal maxSupply;
    }

    public enum CurrencyStatus {
        ACTIVE,
        INACTIVE,
        DELISTED,
        PENDING
    }

    public boolean isActive() {
        return CurrencyStatus.ACTIVE.equals(status);
    }

    public boolean isFiat() {
        return Boolean.FALSE.equals(isCrypto);
    }

    public boolean isFiatCurrency() {
        return Boolean.FALSE.equals(isCrypto) && isActive();
    }

    public String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return symbol + "0.00";
        }
        return symbol + String.format("%." + decimalPlaces + "f", amount);
    }

    public BigDecimal roundToDecimalPlaces(BigDecimal amount) {
        if (amount == null || decimalPlaces == null) {
            return amount;
        }
        return amount.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
    }
}
