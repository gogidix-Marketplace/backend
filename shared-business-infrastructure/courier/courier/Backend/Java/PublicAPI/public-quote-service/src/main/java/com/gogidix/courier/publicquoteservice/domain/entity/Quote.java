package com.gogidix.courier.publicquoteservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "quotes")
public class Quote {

    @Id
    private String id;

    @Indexed
    @Field("quote_number")
    private String quoteNumber;

    @Indexed
    @Field("customer_email")
    private String customerEmail;

    @Field("pickup_address")
    private String pickupAddress;

    @Field("delivery_address")
    private String deliveryAddress;

    @Field("items")
    private List<QuoteItem> items;

    @Field("base_amount")
    private BigDecimal baseAmount;

    @Field("tax_amount")
    private BigDecimal taxAmount;

    @Field("surcharge_amount")
    private BigDecimal surchargeAmount;

    @Field("discount_amount")
    private BigDecimal discountAmount;

    @Field("total_amount")
    private BigDecimal totalAmount;

    @Field("currency")
    private String currency;

    @Field("status")
    private QuoteStatus status;

    @Field("valid_until")
    private Instant validUntil;

    @Field("service_type")
    private String serviceType;

    @Field("estimated_delivery_days")
    private Integer estimatedDeliveryDays;

    @Field("created_at")
    private Instant createdAt;

    protected Quote() {
    }

    public Quote(String customerEmail, String serviceType) {
        this.id = java.util.UUID.randomUUID().toString();
        this.quoteNumber = "QT-" + System.currentTimeMillis();
        this.customerEmail = Objects.requireNonNull(customerEmail);
        this.serviceType = serviceType;
        this.items = new ArrayList<>();
        this.currency = "USD";
        this.status = QuoteStatus.PENDING;
        this.validUntil = Instant.now().plusSeconds(1800); // 30 minutes
        this.createdAt = Instant.now();
    }

    public boolean isValid() {
        return status == QuoteStatus.PENDING && Instant.now().isBefore(validUntil);
    }

    public void accept() {
        this.status = QuoteStatus.ACCEPTED;
    }

    public void expire() {
        this.status = QuoteStatus.EXPIRED;
    }

    public void reject() {
        this.status = QuoteStatus.REJECTED;
    }

    public void calculateTotal() {
        BigDecimal subtotal = baseAmount;
        if (taxAmount != null) subtotal = subtotal.add(taxAmount);
        if (surchargeAmount != null) subtotal = subtotal.add(surchargeAmount);
        if (discountAmount != null) subtotal = subtotal.subtract(discountAmount);
        this.totalAmount = subtotal;
    }

    public enum QuoteStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        EXPIRED
    }

    // Getters
    public String getId() { return id; }
    public String getQuoteNumber() { return quoteNumber; }
    public String getCustomerEmail() { return customerEmail; }
    public String getPickupAddress() { return pickupAddress; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public List<QuoteItem> getItems() { return items; }
    public BigDecimal getBaseAmount() { return baseAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public BigDecimal getSurchargeAmount() { return surchargeAmount; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public QuoteStatus getStatus() { return status; }
    public Instant getValidUntil() { return validUntil; }
    public String getServiceType() { return serviceType; }
    public Integer getEstimatedDeliveryDays() { return estimatedDeliveryDays; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    public void setBaseAmount(BigDecimal baseAmount) { this.baseAmount = baseAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public void setSurchargeAmount(BigDecimal surchargeAmount) { this.surchargeAmount = surchargeAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public void setEstimatedDeliveryDays(Integer estimatedDeliveryDays) { this.estimatedDeliveryDays = estimatedDeliveryDays; }

    protected void setId(String id) { this.id = id; }
    protected void setQuoteNumber(String quoteNumber) { this.quoteNumber = quoteNumber; }
    protected void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    protected void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    protected void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    protected void setItems(List<QuoteItem> items) { this.items = items; }
    protected void setCurrency(String currency) { this.currency = currency; }
    protected void setStatus(QuoteStatus status) { this.status = status; }
    protected void setValidUntil(Instant validUntil) { this.validUntil = validUntil; }
    protected void setServiceType(String serviceType) { this.serviceType = serviceType; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
