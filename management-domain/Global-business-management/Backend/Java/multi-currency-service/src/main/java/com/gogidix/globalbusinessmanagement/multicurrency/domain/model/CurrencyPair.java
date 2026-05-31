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
import java.util.Map;

/**
 * Domain model representing a currency pair configuration.
 * Defines trading relationships between two currencies.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "currency_pairs")
@CompoundIndex(name = "base_quote_idx", def = "{'baseCurrency': 1, 'quoteCurrency': 1}", unique = true)
public class CurrencyPair {

    @Id
    private String id;

    @NotBlank(message = "Base currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid ISO 4217 code")
    @Indexed
    private String baseCurrency;

    @NotBlank(message = "Quote currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid ISO 4217 code")
    @Indexed
    private String quoteCurrency;

    @NotBlank(message = "Pair symbol is required")
    @Indexed(unique = true)
    private String symbol;

    private String displayName;

    @NotNull(message = "Pair status is required")
    @Builder.Default
    private PairStatus status = PairStatus.ACTIVE;

    @Indexed
    private PairType type;

    @Indexed
    private String category;

    @DecimalMin(value = "0.001", message = "Minimum trade amount must be positive")
    private BigDecimal minTradeAmount;

    @DecimalMin(value = "0.001", message = "Maximum trade amount must be positive")
    private BigDecimal maxTradeAmount;

    private BigDecimal tickSize;

    private Integer decimalPlaces;

    @Indexed
    private BigDecimal currentRate;

    @Indexed
    private BigDecimal previousRate;

    @Indexed
    private BigDecimal dayOpenRate;

    @Indexed
    private BigDecimal dayHighRate;

    @Indexed
    private BigDecimal dayLowRate;

    private BigDecimal weekHighRate;

    private BigDecimal weekLowRate;

    private BigDecimal monthHighRate;

    private BigDecimal monthLowRate;

    private BigDecimal yearHighRate;

    private BigDecimal yearLowRate;

    @Indexed
    private BigDecimal volatility;

    @Indexed
    private BigDecimal liquidityScore;

    @Indexed
    private BigDecimal volume24h;

    private BigDecimal volume7d;

    private BigDecimal volume30d;

    private BigDecimal change24h;

    private BigDecimal changePercent24h;

    private BigDecimal change7d;

    private BigDecimal changePercent7d;

    private BigDecimal change30d;

    private BigDecimal changePercent30d;

    @Indexed
    private Integer popularityRank;

    @Indexed
    private BigDecimal marketCap;

    private Boolean isTradable;

    private Boolean isFramed;

    private Integer tradingHoursStart;

    private Integer tradingHoursEnd;

    private String tradingTimeZone;

    private Map<String, Object> properties;

    private String dataSource;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private Instant lastRateUpdate;

    public enum PairStatus {
        ACTIVE,
        INACTIVE,
        RESTRICTED,
        DELISTED,
        PENDING
    }

    public enum PairType {
        FIAT_FIAT,
        FIAT_CRYPTO,
        CRYPTO_CRYPTO,
        COMMODITY,
        INDEX
    }

    public boolean isActive() {
        return PairStatus.ACTIVE.equals(status);
    }

    public boolean isTradable() {
        return isActive() && Boolean.TRUE.equals(isTradable);
    }

    public String getPairKey() {
        return baseCurrency + "/" + quoteCurrency;
    }

    public String getInverseKey() {
        return quoteCurrency + "/" + baseCurrency;
    }

    public BigDecimal getInverseRate() {
        if (currentRate == null || currentRate.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.ONE.divide(currentRate, 10, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateSpread(BigDecimal bidRate, BigDecimal askRate) {
        if (bidRate != null && askRate != null) {
            return askRate.subtract(bidRate);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calculatePipValue(BigDecimal lotSize) {
        if (lotSize == null || tickSize == null || tickSize.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return lotSize.multiply(tickSize);
    }

    public boolean isWithinTradingHours() {
        if (tradingHoursStart == null || tradingHoursEnd == null) {
            return true; // 24/7 trading
        }
        int currentHour = LocalDateTime.now().getHour();
        return currentHour >= tradingHoursStart && currentHour < tradingHoursEnd;
    }

    public BigDecimal calculateRateChange() {
        if (currentRate != null && previousRate != null && previousRate.compareTo(BigDecimal.ZERO) != 0) {
            return currentRate.subtract(previousRate)
                .divide(previousRate, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    public boolean isCryptoPair() {
        return PairType.FIAT_CRYPTO.equals(type) || PairType.CRYPTO_CRYPTO.equals(type);
    }

    public boolean isFiatPair() {
        return PairType.FIAT_FIAT.equals(type);
    }
}
