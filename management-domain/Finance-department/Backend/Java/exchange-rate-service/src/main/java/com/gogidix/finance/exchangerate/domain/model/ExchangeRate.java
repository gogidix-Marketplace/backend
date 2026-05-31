package com.gogidix.finance.exchangerate.domain.model;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;

@Document(collection = "exchange_rates")
@CompoundIndex(name = "tenant_currency_pair_idx", def = "{'tenantId': 1, 'baseCurrency': 1, 'quoteCurrency': 1}", unique = true)
public class ExchangeRate extends BaseEntity {

    @Indexed
    @Field("base_currency")
    private String baseCurrency;

    @Indexed
    @Field("quote_currency")
    private String quoteCurrency;

    @Field("rate")
    private BigDecimal rate;

    @Field("inverse_rate")
    private BigDecimal inverseRate;

    @Field("valid_from")
    private Instant validFrom;

    @Field("valid_to")
    private Instant validTo;

    @Field("source")
    private String source;

    @Field("quality")
    private RateQuality quality;

    @Field("bid_price")
    private BigDecimal bidPrice;

    @Field("ask_price")
    private BigDecimal askPrice;

    @Field("mid_price")
    private BigDecimal midPrice;

    @Field("spread")
    private BigDecimal spread;

    public enum RateQuality {
        REAL_TIME,
        DAILY,
        ESTIMATED,
        HISTORICAL,
        CACHED
    }

    protected ExchangeRate() {
    }

    private ExchangeRate(Builder builder) {
        super(builder.tenantId);
        this.baseCurrency = validateCurrencyCode(builder.baseCurrency, "baseCurrency");
        this.quoteCurrency = validateCurrencyCode(builder.quoteCurrency, "quoteCurrency");
        if (baseCurrency.equals(quoteCurrency)) {
            throw new IllegalArgumentException("Base and quote currencies cannot be the same");
        }
        setRate(builder.rate);
        this.validFrom = builder.validFrom != null ? builder.validFrom : Instant.now();
        this.validTo = builder.validTo;
        this.source = builder.source;
        this.quality = builder.quality != null ? builder.quality : RateQuality.DAILY;
        this.bidPrice = builder.bidPrice;
        this.askPrice = builder.askPrice;
        this.midPrice = builder.midPrice;
        this.spread = builder.spread;
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

    public void setRate(BigDecimal newRate) {
        if (newRate == null || newRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rate must be positive");
        }
        this.rate = newRate.setScale(10, RoundingMode.HALF_UP);
        this.inverseRate = BigDecimal.ONE.divide(this.rate, 10, RoundingMode.HALF_UP);
        this.updateTimestamp();
    }

    public void updateRate(BigDecimal newRate, String source, RateQuality quality) {
        BigDecimal oldRate = this.rate;
        setRate(newRate);
        this.source = source;
        this.quality = quality;
        this.updateTimestamp();
    }

    public BigDecimal convert(BigDecimal amount, String fromCurrency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        if (fromCurrency.equals(baseCurrency)) {
            return amount.multiply(rate).setScale(10, RoundingMode.HALF_UP);
        } else if (fromCurrency.equals(quoteCurrency)) {
            return amount.multiply(inverseRate).setScale(10, RoundingMode.HALF_UP);
        } else {
            throw new IllegalArgumentException("Currency mismatch: " + fromCurrency);
        }
    }

    public boolean isValid() {
        Instant now = Instant.now();
        return (validFrom == null || !validFrom.isAfter(now)) &&
               (validTo == null || !validTo.isBefore(now));
    }

    public boolean isExpired() {
        return validTo != null && validTo.isBefore(Instant.now());
    }

    public void invalidate() {
        this.validTo = Instant.now();
        this.quality = RateQuality.HISTORICAL;
        this.updateTimestamp();
    }

    public void extendValidity(Instant newValidTo) {
        if (newValidTo == null || newValidTo.isBefore(Instant.now())) {
            throw new IllegalArgumentException("New validTo must be in the future");
        }
        this.validTo = newValidTo;
        this.updateTimestamp();
    }

    public void setSpread(BigDecimal bid, BigDecimal ask) {
        if (bid != null && ask != null) {
            this.bidPrice = bid.setScale(10, RoundingMode.HALF_UP);
            this.askPrice = ask.setScale(10, RoundingMode.HALF_UP);
            this.spread = ask.subtract(bid).setScale(10, RoundingMode.HALF_UP);
            this.midPrice = bid.add(ask).divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
            this.updateTimestamp();
        }
    }

    public String getCurrencyPair() {
        return baseCurrency + quoteCurrency;
    }

    public String getCurrencyPairWithSlash() {
        return baseCurrency + "/" + quoteCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getInverseRate() {
        return inverseRate;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public Instant getValidTo() {
        return validTo;
    }

    public String getSource() {
        return source;
    }

    public RateQuality getQuality() {
        return quality;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public BigDecimal getMidPrice() {
        return midPrice;
    }

    public BigDecimal getSpread() {
        return spread;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tenantId;
        private String baseCurrency;
        private String quoteCurrency;
        private BigDecimal rate;
        private Instant validFrom;
        private Instant validTo;
        private String source;
        private RateQuality quality;
        private BigDecimal bidPrice;
        private BigDecimal askPrice;
        private BigDecimal midPrice;
        private BigDecimal spread;

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

        public Builder validFrom(Instant validFrom) {
            this.validFrom = validFrom;
            return this;
        }

        public Builder validTo(Instant validTo) {
            this.validTo = validTo;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder quality(RateQuality quality) {
            this.quality = quality;
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

        public ExchangeRate build() {
            return new ExchangeRate(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(baseCurrency, that.baseCurrency) &&
               Objects.equals(quoteCurrency, that.quoteCurrency) &&
               Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseCurrency, quoteCurrency, tenantId);
    }
}
