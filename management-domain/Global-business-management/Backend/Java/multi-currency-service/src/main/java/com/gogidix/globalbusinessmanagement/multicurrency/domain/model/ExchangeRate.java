package com.gogidix.globalbusinessmanagement.multicurrency.domain.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Domain model representing an exchange rate between two currencies.
 * Stores current and historical exchange rate data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "exchange_rates")
@CompoundIndex(name = "from_to_date_idx", def = "{'fromCurrency': 1, 'toCurrency': 1, 'effectiveDate': -1}", unique = false)
public class ExchangeRate {

    @Id
    private String id;

    @NotBlank(message = "Source currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid ISO 4217 code")
    @Indexed
    private String fromCurrency;

    @NotBlank(message = "Target currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid ISO 4217 code")
    @Indexed
    private String toCurrency;

    @NotNull(message = "Exchange rate is required")
    @DecimalMin(value = "0.000001", message = "Exchange rate must be positive")
    private BigDecimal rate;

    @NotNull(message = "Effective date is required")
    @Indexed
    private Instant effectiveDate;

    @NotNull(message = "Rate source is required")
    private RateSource source;

    private String sourceDetail;

    @Indexed
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @NotNull(message = "Rate status is required")
    @Builder.Default
    private RateStatus status = RateStatus.ACTIVE;

    @Indexed
    private String currencyPair;

    private BigDecimal bidRate;

    private BigDecimal askRate;

    private BigDecimal midRate;

    private BigDecimal spread;

    @Indexed
    private BigDecimal volatility;

    @Indexed
    private BigDecimal volume24h;

    private BigDecimal high24h;

    private BigDecimal low24h;

    private BigDecimal change24h;

    private BigDecimal changePercent24h;

    private Integer decimalPlaces;

    private BigDecimal inverseRate;

    @Builder.Default
    private Boolean isCrossRate = false;

    private String baseCurrency;

    private String providerRateId;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public enum RateSource {
        ECB,
        FED,
        BANK_OF_ENGLAND,
        BLOOMBERG,
        REUTERS,
        XE,
        OANDA,
        CRYPTO_COMPARE,
        COINBASE,
        BINANCE,
        MANUAL,
        CALCULATED
    }

    public enum RateStatus {
        ACTIVE,
        INACTIVE,
        PENDING,
        EXPIRED
    }

    public boolean isActive() {
        return RateStatus.ACTIVE.equals(status);
    }

    public boolean isSamePair(String from, String to) {
        return fromCurrency.equals(from) && toCurrency.equals(to);
    }

    public boolean isInversePair(String from, String to) {
        return fromCurrency.equals(to) && toCurrency.equals(from);
    }

    public BigDecimal convert(BigDecimal amount) {
        if (amount == null || rate == null) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(rate).setScale(decimalPlaces != null ? decimalPlaces : 6, RoundingMode.HALF_UP);
    }

    public BigDecimal invertRate() {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.ONE.divide(rate, 10, RoundingMode.HALF_UP);
    }

    public String getCurrencyPair() {
        return fromCurrency + toCurrency;
    }

    public String getInversePair() {
        return toCurrency + fromCurrency;
    }

    public BigDecimal calculateSpread() {
        if (bidRate != null && askRate != null) {
            spread = askRate.subtract(bidRate);
        }
        return spread;
    }

    public BigDecimal calculateMidRate() {
        if (bidRate != null && askRate != null) {
            midRate = bidRate.add(askRate).divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
        }
        return midRate;
    }

    public boolean isExpired(Instant asOfDate) {
        return endDate != null && asOfDate != null &&
            LocalDateTime.from(asOfDate).isAfter(endDate);
    }

    public boolean isValidForDate(Instant asOfDate) {
        return isActive() &&
            (effectiveDate == null || !effectiveDate.isAfter(asOfDate)) &&
            !isExpired(asOfDate);
    }
}
