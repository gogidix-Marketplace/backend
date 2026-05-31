package com.gogidix.finance.conversion.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.gogidix.finance.conversion.domain.event.ConversionCompletedEvent;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Currency Conversion Domain Entity
 * Represents a currency conversion with amount, source/destination currencies, rate, and converted amount
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "currency_conversions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversion extends TenantAwareEntity {

    private String conversionId;
    private String requestedBy;
    private BigDecimal amount;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;
    private BigDecimal convertedAmount;
    private ConversionStatus status;
    private String provider;
    private Instant conversionDate;
    private String reference;
    private String correlationId;
    private String failureReason;
    private BigDecimal fee;
    private BigDecimal totalAmount;
    private List<ConversionCompletedEvent> domainEvents = new ArrayList<>();

    public enum ConversionStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REVERSED
    }

    public static CurrencyConversionBuilder builder() {
        return new CurrencyConversionBuilder();
    }

    /**
     * Creates a new currency conversion request
     */
    public static CurrencyConversion create(String tenantId, String requestedBy,
                                            BigDecimal amount, String fromCurrency,
                                            String toCurrency, String correlationId) {
        String conversionId = "CONV-" + Instant.now().toEpochMilli() + "-" +
                java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        CurrencyConversion conversion = CurrencyConversion.builder()
                .tenantId(tenantId)
                .conversionId(conversionId)
                .requestedBy(requestedBy)
                .amount(amount)
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .status(ConversionStatus.PENDING)
                .conversionDate(Instant.now())
                .correlationId(correlationId)
                .build();

        conversion.generateReference();

        return conversion;
    }

    /**
     * Completes the conversion with the given rate
     */
    public void complete(BigDecimal rate, String provider) {
        if (this.status != ConversionStatus.PENDING) {
            throw new IllegalStateException("Can only complete pending conversions");
        }

        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rate must be positive");
        }

        this.rate = rate;
        this.provider = provider;
        this.convertedAmount = this.amount.multiply(rate)
                .setScale(2, java.math.RoundingMode.HALF_UP);
        this.status = ConversionStatus.COMPLETED;
        this.updatedAt = Instant.now();

        addDomainEvent(ConversionCompletedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .conversionId(this.conversionId)
                .tenantId(this.tenantId)
                .requestedBy(this.requestedBy)
                .fromCurrency(this.fromCurrency)
                .toCurrency(this.toCurrency)
                .amount(this.amount)
                .rate(this.rate)
                .convertedAmount(this.convertedAmount)
                .provider(this.provider)
                .timestamp(Instant.now())
                .eventType("CONVERSION_COMPLETED")
                .build());
    }

    /**
     * Marks the conversion as failed
     */
    public void fail(String reason) {
        if (this.status != ConversionStatus.PENDING) {
            throw new IllegalStateException("Can only fail pending conversions");
        }

        this.status = ConversionStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = Instant.now();
    }

    /**
     * Reverses the conversion
     */
    public void reverse(String reason) {
        if (this.status != ConversionStatus.COMPLETED) {
            throw new IllegalStateException("Can only reverse completed conversions");
        }

        this.status = ConversionStatus.REVERSED;
        this.failureReason = reason;
        this.updatedAt = Instant.now();
    }

    /**
     * Sets the conversion fee
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
        if (this.convertedAmount != null) {
            this.totalAmount = this.convertedAmount.add(fee != null ? fee : BigDecimal.ZERO);
        }
    }

    /**
     * Generates a unique reference for the conversion
     */
    private void generateReference() {
        this.reference = "REF-" + this.conversionId;
    }

    /**
     * Validates if the conversion can be processed
     */
    public void validateForProcessing() {
        if (this.amount == null || this.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.fromCurrency == null || this.fromCurrency.isBlank()) {
            throw new IllegalArgumentException("Source currency is required");
        }
        if (this.toCurrency == null || this.toCurrency.isBlank()) {
            throw new IllegalArgumentException("Target currency is required");
        }
        if (this.fromCurrency.equals(this.toCurrency)) {
            throw new IllegalArgumentException("Source and target currencies must be different");
        }
        if (this.amount.compareTo(new BigDecimal("10000000")) > 0) {
            throw new IllegalArgumentException("Amount exceeds maximum limit");
        }
    }

    public void addDomainEvent(ConversionCompletedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Checks if the conversion is reversible within the allowed timeframe
     */
    public boolean isReversible() {
        if (this.status != ConversionStatus.COMPLETED) {
            return false;
        }

        Instant reversalDeadline = this.conversionDate.plusSeconds(300); // 5 minutes
        return Instant.now().isBefore(reversalDeadline);
    }

    /**
     * Custom builder for CurrencyConversion
     */
    public static class CurrencyConversionBuilder {
        private String tenantId;
        private String conversionId;
        private String requestedBy;
        private BigDecimal amount;
        private String fromCurrency;
        private String toCurrency;
        private BigDecimal rate;
        private BigDecimal convertedAmount;
        private ConversionStatus status;
        private String provider;
        private Instant conversionDate;
        private String reference;
        private String correlationId;
        private String failureReason;
        private BigDecimal fee;
        private BigDecimal totalAmount;
        private List<ConversionCompletedEvent> domainEvents = new ArrayList<>();

        public CurrencyConversionBuilder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public CurrencyConversionBuilder conversionId(String conversionId) {
            this.conversionId = conversionId;
            return this;
        }

        public CurrencyConversionBuilder requestedBy(String requestedBy) {
            this.requestedBy = requestedBy;
            return this;
        }

        public CurrencyConversionBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public CurrencyConversionBuilder fromCurrency(String fromCurrency) {
            this.fromCurrency = fromCurrency;
            return this;
        }

        public CurrencyConversionBuilder toCurrency(String toCurrency) {
            this.toCurrency = toCurrency;
            return this;
        }

        public CurrencyConversionBuilder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public CurrencyConversionBuilder convertedAmount(BigDecimal convertedAmount) {
            this.convertedAmount = convertedAmount;
            return this;
        }

        public CurrencyConversionBuilder status(ConversionStatus status) {
            this.status = status;
            return this;
        }

        public CurrencyConversionBuilder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public CurrencyConversionBuilder conversionDate(Instant conversionDate) {
            this.conversionDate = conversionDate;
            return this;
        }

        public CurrencyConversionBuilder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public CurrencyConversionBuilder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public CurrencyConversionBuilder failureReason(String failureReason) {
            this.failureReason = failureReason;
            return this;
        }

        public CurrencyConversionBuilder fee(BigDecimal fee) {
            this.fee = fee;
            return this;
        }

        public CurrencyConversionBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public CurrencyConversionBuilder domainEvents(List<ConversionCompletedEvent> domainEvents) {
            this.domainEvents = domainEvents;
            return this;
        }

        public CurrencyConversion build() {
            CurrencyConversion conversion = new CurrencyConversion();
            conversion.tenantId = this.tenantId;
            conversion.conversionId = this.conversionId;
            conversion.requestedBy = this.requestedBy;
            conversion.amount = this.amount;
            conversion.fromCurrency = this.fromCurrency;
            conversion.toCurrency = this.toCurrency;
            conversion.rate = this.rate;
            conversion.convertedAmount = this.convertedAmount;
            conversion.status = this.status;
            conversion.provider = this.provider;
            conversion.conversionDate = this.conversionDate;
            conversion.reference = this.reference;
            conversion.correlationId = this.correlationId;
            conversion.failureReason = this.failureReason;
            conversion.fee = this.fee;
            conversion.totalAmount = this.totalAmount;
            conversion.domainEvents = this.domainEvents;
            return conversion;
        }
    }
}
