package com.gogidix.finance.conversion.infrastructure.persistence.mongodb;

import com.gogidix.finance.conversion.domain.event.ConversionCompletedEvent;
import com.gogidix.finance.conversion.domain.model.CurrencyConversion;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MongoDB document entity for storing CurrencyConversion domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "currency_conversions")
public class CurrencyConversionEntity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("conversion_id")
    private String conversionId;

    @Field("requested_by")
    private String requestedBy;

    private BigDecimal amount;

    @Field("from_currency")
    private String fromCurrency;

    @Field("to_currency")
    private String toCurrency;

    private BigDecimal rate;

    @Field("converted_amount")
    private BigDecimal convertedAmount;

    @Indexed
    @Field("status")
    private String status;

    private String provider;

    @Field("conversion_date")
    private Instant conversionDate;

    private String reference;

    @Field("correlation_id")
    private String correlationId;

    @Field("failure_reason")
    private String failureReason;

    private BigDecimal fee;

    @Field("total_amount")
    private BigDecimal totalAmount;

    @Field("domain_events")
    private List<ConversionCompletedEvent> domainEvents;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public CurrencyConversionEntity() {
    }

    // Constructor from domain model
    public CurrencyConversionEntity(CurrencyConversion domain) {
        this.id = domain.getId();
        this.tenantId = domain.getTenantId();
        this.conversionId = domain.getConversionId();
        this.requestedBy = domain.getRequestedBy();
        this.amount = domain.getAmount();
        this.fromCurrency = domain.getFromCurrency();
        this.toCurrency = domain.getToCurrency();
        this.rate = domain.getRate();
        this.convertedAmount = domain.getConvertedAmount();
        this.status = domain.getStatus() != null ? domain.getStatus().name() : null;
        this.provider = domain.getProvider();
        this.conversionDate = domain.getConversionDate();
        this.reference = domain.getReference();
        this.correlationId = domain.getCorrelationId();
        this.failureReason = domain.getFailureReason();
        this.fee = domain.getFee();
        this.totalAmount = domain.getTotalAmount();
        this.domainEvents = domain.getDomainEvents() != null ? new ArrayList<>(domain.getDomainEvents()) : new ArrayList<>();
        this.createdAt = domain.getCreatedAt();
        this.updatedAt = domain.getUpdatedAt();
    }

    // Convert to domain model
    public CurrencyConversion toDomainModel() {
        CurrencyConversion.CurrencyConversionBuilder builder = CurrencyConversion.builder()
                .tenantId(this.tenantId)
                .conversionId(this.conversionId)
                .requestedBy(this.requestedBy)
                .amount(this.amount)
                .fromCurrency(this.fromCurrency)
                .toCurrency(this.toCurrency)
                .rate(this.rate)
                .convertedAmount(this.convertedAmount)
                .provider(this.provider)
                .conversionDate(this.conversionDate)
                .reference(this.reference)
                .correlationId(this.correlationId)
                .failureReason(this.failureReason)
                .fee(this.fee)
                .totalAmount(this.totalAmount)
                .domainEvents(this.domainEvents);

        if (this.status != null) {
            builder.status(CurrencyConversion.ConversionStatus.valueOf(this.status));
        }

        CurrencyConversion conversion = builder.build();
        // Set ID, created and updated via reflection since builder doesn't support them
        try {
            java.lang.reflect.Field idField = CurrencyConversion.class.getSuperclass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(conversion, this.id);

            java.lang.reflect.Field createdAtField = CurrencyConversion.class.getSuperclass().getSuperclass().getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(conversion, this.createdAt);

            java.lang.reflect.Field updatedAtField = CurrencyConversion.class.getSuperclass().getSuperclass().getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(conversion, this.updatedAt);
        } catch (Exception e) {
            // Ignore reflection errors
        }

        return conversion;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getConversionId() { return conversionId; }
    public void setConversionId(String conversionId) { this.conversionId = conversionId; }

    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getFromCurrency() { return fromCurrency; }
    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }

    public String getToCurrency() { return toCurrency; }
    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }

    public BigDecimal getConvertedAmount() { return convertedAmount; }
    public void setConvertedAmount(BigDecimal convertedAmount) { this.convertedAmount = convertedAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public Instant getConversionDate() { return conversionDate; }
    public void setConversionDate(Instant conversionDate) { this.conversionDate = conversionDate; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public List<ConversionCompletedEvent> getDomainEvents() { return domainEvents; }
    public void setDomainEvents(List<ConversionCompletedEvent> domainEvents) { this.domainEvents = domainEvents; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyConversionEntity that = (CurrencyConversionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
