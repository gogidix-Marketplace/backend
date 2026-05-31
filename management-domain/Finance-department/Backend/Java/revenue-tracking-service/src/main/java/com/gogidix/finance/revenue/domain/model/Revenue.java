package com.gogidix.finance.revenue.domain.model;

import com.gogidix.finance.revenue.domain.event.RevenueRecognizedEvent;
import com.gogidix.finance.revenue.shared.base.BaseEntity;
import com.gogidix.finance.revenue.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
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
 * Multi-tenant revenue tracking and recognition
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "revenues")
public class Revenue extends BaseEntity {

    private String revenueId;

    private String tenantId;

    private String customerId;

    private String customerName;

    private String contractId;

    private String projectId;

    private RevenueType type;

    private BigDecimal totalAmount;

    private String currency;

    private BigDecimal recognizedAmount;

    private BigDecimal deferredAmount;

    private RevenueStatus status;

    private RecognitionMethod recognitionMethod;

    private LocalDate recognitionStartDate;

    private LocalDate recognitionEndDate;

    private Integer recognitionPeriods;

    private Integer currentPeriod;

    private LocalDate transactionDate;

    private String description;

    private String category;

    private String productCode;

    private String productSku;

    private String department;

    private String costCenter;

    private String salespersonId;

    private String region;

    private String territory;

    private PaymentTerms paymentTerms;

    private LocalDate invoiceDate;

    private String invoiceNumber;

    private LocalDate dueDate;

    private LocalDate paidDate;

    private Boolean fullyPaid;

    private List<RevenueMilestone> milestones;

    private List<String> tags;

    private String notes;

    private String parentRevenueId;

    private Boolean isRecurring;

    private String recurringSchedule;

    private LocalDate nextRecognitionDate;

    private BigDecimal recognizedThisPeriod;

    private List<RevenueRecognizedEvent> domainEvents = new ArrayList<>();

    public enum RevenueType {
        RECURRING,
        ONE_TIME,
        USAGE_BASED,
        SUBSCRIPTION,
        PROJECT,
        SERVICE,
        PRODUCT,
        LICENSE,
        MAINTENANCE,
        SUPPORT
    }

    public enum RevenueStatus {
        PENDING,
        RECOGNIZED,
        DEFERRED,
        PARTIALLY_RECOGNIZED,
        CANCELLED,
        REFUNDED,
        ON_HOLD
    }

    public enum RecognitionMethod {
        POINT_IN_TIME,
        OVER_TIME,
        DEFERRED,
        MILESTONE,
        USAGE_BASED
    }

    public enum PaymentTerms {
        NET_15,
        NET_30,
        NET_45,
        NET_60,
        COD,
        PREPAID,
        INSTALLMENTS
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueMilestone {
        private String milestoneId;
        private String name;
        private String description;
        private BigDecimal amount;
        private LocalDate targetDate;
        private LocalDate completedDate;
        private MilestoneStatus status;

        public enum MilestoneStatus {
            PENDING,
            IN_PROGRESS,
            COMPLETED,
            CANCELLED
        }
    }

    /**
     * Creates a new revenue entry
     */
    public static Revenue create(String tenantId, String customerId, RevenueType type,
                                  BigDecimal totalAmount, String currency,
                                  RecognitionMethod recognitionMethod, LocalDate transactionDate) {
        Revenue revenue = new Revenue();
        revenue.tenantId = tenantId;
        revenue.customerId = customerId;
        revenue.type = type;
        revenue.totalAmount = totalAmount;
        revenue.currency = currency;
        revenue.recognizedAmount = BigDecimal.ZERO;
        revenue.deferredAmount = totalAmount;
        revenue.status = RevenueStatus.PENDING;
        revenue.recognitionMethod = recognitionMethod;
        revenue.transactionDate = transactionDate;
        revenue.fullyPaid = false;
        revenue.isRecurring = false;
        revenue.currentPeriod = 0;
        revenue.recognizedThisPeriod = BigDecimal.ZERO;
        revenue.tags = new ArrayList<>();
        revenue.milestones = new ArrayList<>();

        // Initialize status based on recognition method
        if (recognitionMethod == RecognitionMethod.DEFERRED) {
            revenue.status = RevenueStatus.DEFERRED;
        } else if (recognitionMethod == RecognitionMethod.POINT_IN_TIME) {
            revenue.status = RevenueStatus.PENDING;
        }

        revenue.addDomainEvent(RevenueRecognizedEvent.builder()
            .revenueId(revenue.getRevenueId())
            .tenantId(tenantId)
            .customerId(customerId)
            .type(type.name())
            .amount(totalAmount)
            .currency(currency)
            .timestamp(Instant.now())
            .eventType("REVENUE_CREATED")
            .build());

        return revenue;
    }

    /**
     * Recognizes revenue (full or partial)
     */
    public void recognize(BigDecimal amount, String recognizedBy) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount", "Recognition amount must be positive");
        }

        BigDecimal totalRecognized = this.recognizedAmount.add(amount);
        if (totalRecognized.compareTo(this.totalAmount) > 0) {
            throw new ValidationException("amount", "Recognition amount exceeds total revenue");
        }

        this.recognizedAmount = totalRecognized;
        this.recognizedThisPeriod = amount;
        this.deferredAmount = this.totalAmount.subtract(this.recognizedAmount);
        this.currentPeriod++;

        // Update status
        if (this.recognizedAmount.compareTo(this.totalAmount) == 0) {
            this.status = RevenueStatus.RECOGNIZED;
        } else {
            this.status = RevenueStatus.PARTIALLY_RECOGNIZED;
        }

        // Calculate next recognition date for deferred revenue
        if (this.recognitionMethod == RecognitionMethod.OVER_TIME && this.status != RevenueStatus.RECOGNIZED) {
            this.nextRecognitionDate = calculateNextRecognitionDate();
        }

        addDomainEvent(RevenueRecognizedEvent.builder()
            .revenueId(this.revenueId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .type(this.type.name())
            .amount(amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("REVENUE_RECOGNIZED")
            .recognizedBy(recognizedBy)
            .totalRecognized(this.recognizedAmount)
            .build());
    }

    /**
     * Defers revenue recognition
     */
    public void defer(Integer periods, LocalDate startDate) {
        if (this.status == RevenueStatus.RECOGNIZED) {
            throw new ValidationException("Cannot defer already recognized revenue");
        }

        this.recognitionMethod = RecognitionMethod.OVER_TIME;
        this.recognitionPeriods = periods;
        this.recognitionStartDate = startDate;
        this.recognitionEndDate = startDate.plusMonths(periods);
        this.status = RevenueStatus.DEFERRED;
        this.nextRecognitionDate = calculateNextRecognitionDate();
        this.currentPeriod = 0;

        BigDecimal periodAmount = this.totalAmount.divide(BigDecimal.valueOf(periods), 2, BigDecimal.ROUND_HALF_UP);
        this.recognizedThisPeriod = periodAmount;

        addDomainEvent(RevenueRecognizedEvent.builder()
            .revenueId(this.revenueId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .type(this.type.name())
            .amount(this.totalAmount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("REVENUE_DEFERRED")
            .periods(periods)
            .build());
    }

    /**
     * Processes a milestone completion
     */
    public void completeMilestone(String milestoneId, LocalDate completedDate) {
        if (this.milestones == null || this.milestones.isEmpty()) {
            throw new ValidationException("milestoneId", "No milestones defined");
        }

        RevenueMilestone milestone = this.milestones.stream()
            .filter(m -> m.getMilestoneId().equals(milestoneId))
            .findFirst()
            .orElseThrow(() -> new ValidationException("milestoneId", "Milestone not found"));

        milestone.setStatus(RevenueMilestone.MilestoneStatus.COMPLETED);
        milestone.setCompletedDate(completedDate);

        // Recognize the milestone amount
        recognize(milestone.getAmount(), "system");

        addDomainEvent(RevenueRecognizedEvent.builder()
            .revenueId(this.revenueId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .type(this.type.name())
            .amount(milestone.getAmount())
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("MILESTONE_COMPLETED")
            .milestoneId(milestoneId)
            .build());
    }

    /**
     * Cancels the revenue
     */
    public void cancel(String reason) {
        if (this.status == RevenueStatus.RECOGNIZED) {
            throw new ValidationException("Cannot cancel recognized revenue");
        }

        this.status = RevenueStatus.CANCELLED;
        this.notes = (this.notes != null ? this.notes + "\n" : "") + "Cancelled: " + reason;

        addDomainEvent(RevenueRecognizedEvent.builder()
            .revenueId(this.revenueId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .type(this.type.name())
            .amount(this.recognizedAmount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("REVENUE_CANCELLED")
            .reason(reason)
            .build());
    }

    /**
     * Processes a refund
     */
    public void refund(BigDecimal refundAmount, String reason) {
        if (refundAmount.compareTo(this.recognizedAmount) > 0) {
            throw new ValidationException("refundAmount", "Refund cannot exceed recognized amount");
        }

        this.recognizedAmount = this.recognizedAmount.subtract(refundAmount);
        this.deferredAmount = this.totalAmount.subtract(this.recognizedAmount);
        this.status = RevenueStatus.REFUNDED;

        addDomainEvent(RevenueRecognizedEvent.builder()
            .revenueId(this.revenueId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .type(this.type.name())
            .amount(refundAmount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("REVENUE_REFUNDED")
            .reason(reason)
            .build());
    }

    /**
     * Marks revenue as paid
     */
    public void markAsPaid(LocalDate paidDate) {
        this.paidDate = paidDate;
        this.fullyPaid = true;

        addDomainEvent(RevenueRecognizedEvent.builder()
            .revenueId(this.revenueId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .type(this.type.name())
            .amount(this.totalAmount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("REVENUE_PAID")
            .build());
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
     * Adds a milestone
     */
    public void addMilestone(RevenueMilestone milestone) {
        if (this.milestones == null) {
            this.milestones = new ArrayList<>();
        }
        this.milestones.add(milestone);
    }

    /**
     * Calculates recognition progress percentage
     */
    public BigDecimal getRecognitionProgress() {
        if (this.totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return this.recognizedAmount
            .divide(this.totalAmount, 4, BigDecimal.ROUND_HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Calculates remaining amount to be recognized
     */
    public BigDecimal getRemainingAmount() {
        return this.totalAmount.subtract(this.recognizedAmount);
    }

    private LocalDate calculateNextRecognitionDate() {
        if (recognitionStartDate == null || recognitionPeriods == null) {
            return null;
        }

        int monthsPerPeriod = recognitionPeriods > 0
            ? 12 / recognitionPeriods
            : 1;

        return recognitionStartDate.plusMonths((long) (currentPeriod + 1) * monthsPerPeriod);
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
}
