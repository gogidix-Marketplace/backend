package com.gogidix.finance.exchangerate.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity - Historical Rate
 * Tracks historical exchange rate data for analysis and reporting
 * Multi-tenant SaaS entity with mandatory tenantId field
 */
@Data
@Builder
@AllArgsConstructor
@Document(collection = "historical_rates")
@CompoundIndex(name = "tenant_currency_pair_timestamp_idx",
    def = "{'tenantId': 1, 'baseCurrency': 1, 'quoteCurrency': 1, 'timestamp': -1}")
public class HistoricalRate extends BaseEntity {

    @Id
    private String historicalRateId;

    @Indexed
    private String tenantId;

    @Indexed
    private String baseCurrency;

    @Indexed
    private String quoteCurrency;

    private BigDecimal rate;

    private BigDecimal inverseRate;

    private BigDecimal openRate;

    private BigDecimal closeRate;

    private BigDecimal highRate;

    private BigDecimal lowRate;

    private BigDecimal bidPrice;

    private BigDecimal askPrice;

    private BigDecimal midPrice;

    private BigDecimal spread;

    private String source;

    private ExchangeRate.RateQuality quality;

    @Indexed
    private Instant timestamp;

    private Long volume;

    private String dataProvider;

    private Instant recordedAt;

    private Long version;

    private String changeReason;

    private String changedBy;

    private HistoricalRate() {
    }

    private HistoricalRate(Builder builder) {
        this.historicalRateId = builder.historicalRateId != null
            ? builder.historicalRateId
            : java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(builder.tenantId, "tenantId is required");
        this.baseCurrency = validateCurrencyCode(builder.baseCurrency, "baseCurrency");
        this.quoteCurrency = validateCurrencyCode(builder.quoteCurrency, "quoteCurrency");
        this.rate = builder.rate;
        this.inverseRate = builder.rate != null
            ? BigDecimal.ONE.divide(builder.rate, 10, java.math.RoundingMode.HALF_UP)
            : null;
        this.openRate = builder.openRate;
        this.closeRate = builder.closeRate;
        this.highRate = builder.highRate;
        this.lowRate = builder.lowRate;
        this.bidPrice = builder.bidPrice;
        this.askPrice = builder.askPrice;
        this.midPrice = builder.midPrice;
        this.spread = builder.spread;
        this.source = builder.source;
        this.quality = builder.quality;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.volume = builder.volume;
        this.dataProvider = builder.dataProvider;
        this.recordedAt = Instant.now();
        this.version = builder.version != null ? builder.version : 1L;
        this.changeReason = builder.changeReason;
        this.changedBy = builder.changedBy;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    private String validateCurrencyCode(String code, String fieldName) {
        if (code == null) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        if (code.length() != 3) {
            throw new IllegalArgumentException(fieldName + " must be a valid ISO 4217 code (3 characters)");
        }
        if (!code.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException(fieldName + " must contain only uppercase letters");
        }
        return code;
    }

    public BigDecimal calculateChange(HistoricalRate previous) {
        if (previous == null || previous.getRate() == null || this.rate == null) {
            return BigDecimal.ZERO;
        }
        return this.rate.subtract(previous.getRate())
            .divide(previous.getRate(), 6, java.math.RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal calculateChangePercent(HistoricalRate previous) {
        if (previous == null || previous.getRate() == null || this.rate == null) {
            return BigDecimal.ZERO;
        }
        return this.rate.subtract(previous.getRate())
            .multiply(BigDecimal.valueOf(100))
            .divide(previous.getRate(), 4, java.math.RoundingMode.HALF_UP);
    }

    public void setDailyOHLC(BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
        this.openRate = open;
        this.highRate = high;
        this.lowRate = low;
        this.closeRate = close;
        this.rate = close;
    }

    public boolean isValid() {
        return rate != null && rate.compareTo(BigDecimal.ZERO) > 0;
    }

    public String getCurrencyPair() {
        return baseCurrency + quoteCurrency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String historicalRateId;
        private String tenantId;
        private String baseCurrency;
        private String quoteCurrency;
        private BigDecimal rate;
        private BigDecimal openRate;
        private BigDecimal closeRate;
        private BigDecimal highRate;
        private BigDecimal lowRate;
        private BigDecimal bidPrice;
        private BigDecimal askPrice;
        private BigDecimal midPrice;
        private BigDecimal spread;
        private String source;
        private ExchangeRate.RateQuality quality;
        private Instant timestamp;
        private Long volume;
        private String dataProvider;
        private Long version;
        private String changeReason;
        private String changedBy;

        public Builder historicalRateId(String historicalRateId) {
            this.historicalRateId = historicalRateId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder baseCurrency(String baseCurrency) {
            this.baseCurrency = baseCurrency;
            return this;
        }

        public Builder quoteCurrency(String quoteCurrency) {
            this.quoteCurrency = quoteCurrency;
            return this;
        }

        public Builder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public Builder openRate(BigDecimal openRate) {
            this.openRate = openRate;
            return this;
        }

        public Builder closeRate(BigDecimal closeRate) {
            this.closeRate = closeRate;
            return this;
        }

        public Builder highRate(BigDecimal highRate) {
            this.highRate = highRate;
            return this;
        }

        public Builder lowRate(BigDecimal lowRate) {
            this.lowRate = lowRate;
            return this;
        }

        public Builder bidPrice(BigDecimal bidPrice) {
            this.bidPrice = bidPrice;
            return this;
        }

        public Builder askPrice(BigDecimal askPrice) {
            this.askPrice = askPrice;
            return this;
        }

        public Builder midPrice(BigDecimal midPrice) {
            this.midPrice = midPrice;
            return this;
        }

        public Builder spread(BigDecimal spread) {
            this.spread = spread;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder quality(ExchangeRate.RateQuality quality) {
            this.quality = quality;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder volume(Long volume) {
            this.volume = volume;
            return this;
        }

        public Builder dataProvider(String dataProvider) {
            this.dataProvider = dataProvider;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public Builder changeReason(String changeReason) {
            this.changeReason = changeReason;
            return this;
        }

        public Builder changedBy(String changedBy) {
            this.changedBy = changedBy;
            return this;
        }

        public HistoricalRate build() {
            return new HistoricalRate(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricalRate that = (HistoricalRate) o;
        return Objects.equals(historicalRateId, that.historicalRateId) &&
               Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historicalRateId, tenantId);
    }
}
