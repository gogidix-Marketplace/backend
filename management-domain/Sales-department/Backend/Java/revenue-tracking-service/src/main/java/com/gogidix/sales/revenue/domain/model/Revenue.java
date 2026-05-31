package com.gogidix.sales.revenue.domain.model;

import com.gogidix.sales.revenue.domain.event.RevenueRecognizedEvent;
import com.gogidix.sales.revenue.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Revenue Domain Entity
 * Multi-tenant revenue tracking with recognition schedules
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "revenues")
public class Revenue extends BaseEntity {

    private String revenueId;

    private String tenantId;

    private String contractId;

    private String customerId;

    private String customerName;

    private String productId;

    private String productName;

    private String territory;

    private String region;

    private RevenueType revenueType;

    private BigDecimal totalAmount;

    private String currency;

    private BigDecimal recognizedAmount;

    private BigDecimal remainingAmount;

    private RevenueRecognitionType recognitionType;

    private Integer recognitionPeriodMonths;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate recognitionDate;

    private RevenueStatus status;

    private String invoiceId;

    private String invoiceNumber;

    private LocalDate invoiceDate;

    private String salesOrderId;

    private String opportunityId;

    private String salespersonId;

    private String salespersonName;

    private String department;

    private String costCenter;

    private String projectId;

    private List<String> tags;

    private String description;

    private String notes;

    private LocalDate bookingDate;

    private BigDecimal deferredRevenue;

    private LocalDate lastRecognitionDate;

    private Integer recognitionCount;

    private String parentRevenueId;

    private Boolean isSplitRevenue;

    private List<Revenue> splitRevenues;

    @Builder.Default
    private List<RevenueRecognizedEvent> domainEvents = new ArrayList<>();

    private Boolean reconciled;

    private String reconciliationId;

    private LocalDate reconciliationDate;

    public enum RevenueType {
        NEW_BUSINESS,
        RENEWAL,
        EXPANSION,
        UPSILON,
        CONTRACTION,
        CHURN,
        RECURRING,
        NON_RECURRING,
        ONE_TIME,
        USAGE_BASED,
        PROFESSIONAL_SERVICES,
        MAINTENANCE,
        SUPPORT,
        OTHER
    }

    public enum RevenueRecognitionType {
        POINT_IN_TIME,
        OVER_TIME,
        RATABLE,
        MILESTONE,
        DELIVERABLE
    }

    public enum RevenueStatus {
        PENDING,
        BOOKED,
        RECOGNIZING,
        RECOGNIZED,
        PARTIALLY_RECOGNIZED,
        DEFERRED,
        CANCELLED,
        REVERSED
    }

    /**
     * Creates a new revenue record
     */
    public static Revenue create(String tenantId, String contractId, String customerId,
                                  String productId, RevenueType revenueType, BigDecimal totalAmount,
                                  String currency, RevenueRecognitionType recognitionType,
                                  LocalDate startDate, LocalDate endDate) {
        Revenue revenue = Revenue.builder()
                .tenantId(tenantId)
                .contractId(contractId)
                .customerId(customerId)
                .productId(productId)
                .revenueType(revenueType)
                .totalAmount(totalAmount)
                .currency(currency)
                .recognizedAmount(BigDecimal.ZERO)
                .remainingAmount(totalAmount)
                .recognitionType(recognitionType)
                .startDate(startDate)
                .endDate(endDate)
                .status(RevenueStatus.PENDING)
                .recognitionCount(0)
                .tags(new ArrayList<>())
                .build();

        if (recognitionType == RevenueRecognitionType.POINT_IN_TIME) {
            revenue.recognitionDate = startDate;
            revenue.deferredRevenue = totalAmount;
        } else {
            revenue.deferredRevenue = totalAmount;
        }

        revenue.addDomainEvent(RevenueRecognizedEvent.builder()
                .revenueId(revenue.getRevenueId())
                .tenantId(tenantId)
                .customerId(customerId)
                .productId(productId)
                .amount(totalAmount)
                .currency(currency)
                .revenueType(revenueType.name())
                .timestamp(Instant.now())
                .eventType("REVENUE_CREATED")
                .build());

        return revenue;
    }

    /**
     * Books the revenue
     */
    public void book(String userId) {
        if (this.status != RevenueStatus.PENDING) {
            throw new IllegalStateException("Can only book pending revenue");
        }

        this.status = RevenueStatus.BOOKED;
        this.bookingDate = LocalDate.now();
        this.updatedBy = userId;

        addDomainEvent(RevenueRecognizedEvent.builder()
                .revenueId(this.revenueId)
                .tenantId(this.tenantId)
                .customerId(this.customerId)
                .productId(this.productId)
                .amount(this.totalAmount)
                .currency(this.currency)
                .revenueType(this.revenueType.name())
                .timestamp(Instant.now())
                .eventType("REVENUE_BOOKED")
                .build());
    }

    /**
     * Recognizes revenue
     */
    public void recognize(BigDecimal amount, LocalDate recognitionDate) {
        if (this.status != RevenueStatus.BOOKED && this.status != RevenueStatus.RECOGNIZING
                && this.status != RevenueStatus.PARTIALLY_RECOGNIZED) {
            throw new IllegalStateException("Cannot recognize revenue in current status: " + this.status);
        }

        if (amount.compareTo(this.remainingAmount) > 0) {
            throw new IllegalArgumentException("Recognition amount exceeds remaining amount");
        }

        this.recognizedAmount = this.recognizedAmount.add(amount);
        this.remainingAmount = this.totalAmount.subtract(this.recognizedAmount);
        this.lastRecognitionDate = recognitionDate;
        this.recognitionCount++;

        if (this.remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.status = RevenueStatus.RECOGNIZED;
            this.deferredRevenue = BigDecimal.ZERO;
        } else {
            this.status = RevenueStatus.PARTIALLY_RECOGNIZED;
            this.deferredRevenue = this.remainingAmount;
        }

        addDomainEvent(RevenueRecognizedEvent.builder()
                .revenueId(this.revenueId)
                .tenantId(this.tenantId)
                .customerId(this.customerId)
                .productId(this.productId)
                .amount(amount)
                .currency(this.currency)
                .revenueType(this.revenueType.name())
                .recognizedAmount(this.recognizedAmount)
                .remainingAmount(this.remainingAmount)
                .timestamp(Instant.now())
                .eventType("REVENUE_RECOGNIZED")
                .build());
    }

    /**
     * Marks as deferred
     */
    public void defer(String reason) {
        if (this.status != RevenueStatus.BOOKED) {
            throw new IllegalStateException("Can only defer booked revenue");
        }

        this.status = RevenueStatus.DEFERRED;
        this.notes = reason;
    }

    /**
     * Cancels the revenue
     */
    public void cancel(String reason) {
        if (this.status == RevenueStatus.RECOGNIZED || this.status == RevenueStatus.REVERSED) {
            throw new IllegalStateException("Cannot cancel recognized or reversed revenue");
        }

        this.status = RevenueStatus.CANCELLED;
        this.notes = reason;

        addDomainEvent(RevenueRecognizedEvent.builder()
                .revenueId(this.revenueId)
                .tenantId(this.tenantId)
                .customerId(this.customerId)
                .productId(this.productId)
                .amount(this.recognizedAmount)
                .currency(this.currency)
                .revenueType(this.revenueType.name())
                .timestamp(Instant.now())
                .eventType("REVENUE_CANCELLED")
                .build());
    }

    /**
     * Reverses the revenue
     */
    public void reverse(BigDecimal amount, String reason) {
        if (amount.compareTo(this.recognizedAmount) > 0) {
            throw new IllegalArgumentException("Reverse amount exceeds recognized amount");
        }

        this.recognizedAmount = this.recognizedAmount.subtract(amount);
        this.remainingAmount = this.totalAmount.subtract(this.recognizedAmount);
        this.deferredRevenue = this.remainingAmount;

        if (this.recognizedAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.status = RevenueStatus.REVERSED;
        } else {
            this.status = RevenueStatus.PARTIALLY_RECOGNIZED;
        }

        this.notes = reason;

        addDomainEvent(RevenueRecognizedEvent.builder()
                .revenueId(this.revenueId)
                .tenantId(this.tenantId)
                .customerId(this.customerId)
                .productId(this.productId)
                .amount(amount.negate())
                .currency(this.currency)
                .revenueType(this.revenueType.name())
                .recognizedAmount(this.recognizedAmount)
                .remainingAmount(this.remainingAmount)
                .timestamp(Instant.now())
                .eventType("REVENUE_REVERSED")
                .build());
    }

    /**
     * Marks as reconciled
     */
    public void markAsReconciled(String reconciliationId) {
        this.reconciled = true;
        this.reconciliationId = reconciliationId;
        this.reconciliationDate = LocalDate.now();
    }

    /**
     * Adds a tag to the revenue
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the revenue
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Calculates recognition percentage
     */
    public BigDecimal getRecognitionPercentage() {
        if (this.totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return this.recognizedAmount
                .divide(this.totalAmount, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    public void addDomainEvent(RevenueRecognizedEvent event) {
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
     * Checks if revenue is recurring
     */
    public boolean isRecurring() {
        return this.revenueType == RevenueType.RECURRING
                || this.revenueType == RevenueType.NEW_BUSINESS
                || this.revenueType == RevenueType.RENEWAL
                || this.revenueType == RevenueType.EXPANSION;
    }

    /**
     * Checks if revenue is recognized
     */
    public boolean isFullyRecognized() {
        return this.status == RevenueStatus.RECOGNIZED
                || this.remainingAmount.compareTo(BigDecimal.ZERO) == 0;
    }
}
