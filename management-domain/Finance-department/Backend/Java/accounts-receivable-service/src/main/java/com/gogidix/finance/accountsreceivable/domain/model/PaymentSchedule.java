package com.gogidix.finance.accountsreceivable.domain.model;

import com.gogidix.finance.accountsreceivable.shared.base.BaseEntity;
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
 * Payment Schedule Domain Entity
 * Represents scheduled payment plans for invoices
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ar_payment_schedules")
public class PaymentSchedule extends BaseEntity {

    private String scheduleId;

    private String tenantId;

    private String scheduleNumber;

    private String customerId;

    private String customerName;

    private String invoiceId;

    private String invoiceNumber;

    private BigDecimal totalAmount;

    private BigDecimal amountPaid;

    private BigDecimal balanceRemaining;

    private String currency;

    private ScheduleStatus status;

    private ScheduleType scheduleType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer installmentCount;

    private String frequency;

    private String paymentMethod;

    private Boolean autoCharge;

    private String description;

    private String notes;

    @Builder.Default
    private List<ScheduledPayment> scheduledPayments = new ArrayList<>();

    private Instant lastPaymentDate;

    private String nextPaymentDate;

    private Integer totalInstallments;

    private Integer completedInstallments;

    private BigDecimal installmentAmount;

    private String templateId;

    private String originalScheduleId;

    private Boolean isRescheduled;

    private String rescheduleReason;

    private LocalDate rescheduleDate;

    private List<String> tags;

    private String projectId;

    private String departmentId;

    private String bankAccountId;

    private String paymentGatewayCustomerId;

    private String paymentGatewaySubscriptionId;

    private Boolean sendReminder;

    private Integer reminderDaysBefore;

    private Boolean prorateFirstInstallment;

    private BigDecimal prorationAmount;

    private String agreedBy;

    private Instant agreedAt;

    private String agreementReference;

    private String contractId;

    public enum ScheduleStatus {
        PENDING,
        ACTIVE,
        PAUSED,
        COMPLETED,
        CANCELLED,
        DEFAULTED,
        SUSPENDED
    }

    public enum ScheduleType {
        FIXED_INSTALLMENTS,
        PERCENTAGE_BASED,
        CUSTOM,
        SEASONAL,
        STEP_UP,
        STEP_DOWN,
        BALLOON_PAYMENT
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduledPayment {
        private String scheduledPaymentId;
        private Integer installmentNumber;
        private BigDecimal amount;
        private LocalDate dueDate;
        private PaymentStatus status;
        private Instant paidAt;
        private String transactionId;
        private String paymentMethod;
        private BigDecimal paidAmount;
        private BigDecimal remainingAmount;
        private LocalDate paidDate;
        private String failureReason;
        private Integer attemptCount;
        private LocalDate nextAttemptDate;
        private String notes;

        public enum PaymentStatus {
            PENDING,
            PROCESSING,
            PAID,
            PARTIALLY_PAID,
            FAILED,
            SKIPPED,
            CANCELLED
        }
    }

    /**
     * Creates a new payment schedule
     */
    public static PaymentSchedule create(String tenantId, String customerId, String customerName,
                                          String invoiceId, String invoiceNumber,
                                          BigDecimal totalAmount, String currency,
                                          LocalDate startDate, Integer installmentCount,
                                          String frequency, ScheduleType scheduleType) {
        PaymentSchedule schedule = PaymentSchedule.builder()
            .tenantId(tenantId)
            .customerId(customerId)
            .customerName(customerName)
            .invoiceId(invoiceId)
            .invoiceNumber(invoiceNumber)
            .totalAmount(totalAmount)
            .currency(currency)
            .startDate(startDate)
            .installmentCount(installmentCount)
            .totalInstallments(installmentCount)
            .completedInstallments(0)
            .frequency(frequency)
            .scheduleType(scheduleType)
            .status(ScheduleStatus.PENDING)
            .amountPaid(BigDecimal.ZERO)
            .balanceRemaining(totalAmount)
            .autoCharge(false)
            .sendReminder(true)
            .reminderDaysBefore(3)
            .isRescheduled(false)
            .scheduledPayments(new ArrayList<>())
            .tags(new ArrayList<>())
            .build();

        schedule.generateInstallments();

        return schedule;
    }

    /**
     * Activates the payment schedule
     */
    public void activate() {
        if (this.status != ScheduleStatus.PENDING) {
            throw new IllegalStateException("Can only activate pending schedules");
        }

        if (this.scheduledPayments == null || this.scheduledPayments.isEmpty()) {
            throw new IllegalStateException("Cannot activate schedule without installments");
        }

        this.status = ScheduleStatus.ACTIVE;
    }

    /**
     * Processes a scheduled payment
     */
    public void processPayment(String scheduledPaymentId, BigDecimal amount, String transactionId) {
        if (this.status != ScheduleStatus.ACTIVE) {
            throw new IllegalStateException("Can only process payments for active schedules");
        }

        ScheduledPayment payment = this.scheduledPayments.stream()
            .filter(p -> p.getScheduledPaymentId().equals(scheduledPaymentId))
            .findFirst()
            .orElseThrow(() -> new com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException(
                "Scheduled payment not found"));

        if (payment.getStatus() != ScheduledPayment.PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment has already been processed");
        }

        payment.setStatus(ScheduledPayment.PaymentStatus.PAID);
        payment.setPaidAmount(amount);
        payment.setPaidAt(Instant.now());
        payment.setPaidDate(LocalDate.now());
        payment.setTransactionId(transactionId);

        this.amountPaid = this.amountPaid.add(amount);
        this.balanceRemaining = this.totalAmount.subtract(this.amountPaid);
        this.lastPaymentDate = Instant.now();
        this.completedInstallments++;

        if (this.balanceRemaining.compareTo(BigDecimal.ZERO) <= 0) {
            this.status = ScheduleStatus.COMPLETED;
        }

        updateNextPaymentDate();
    }

    /**
     * Fails a scheduled payment
     */
    public void failPayment(String scheduledPaymentId, String reason) {
        ScheduledPayment payment = this.scheduledPayments.stream()
            .filter(p -> p.getScheduledPaymentId().equals(scheduledPaymentId))
            .findFirst()
            .orElseThrow(() -> new com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException(
                "Scheduled payment not found"));

        payment.setStatus(ScheduledPayment.PaymentStatus.FAILED);
        payment.setFailureReason(reason);
        payment.setAttemptCount(payment.getAttemptCount() != null ? payment.getAttemptCount() + 1 : 1);

        // Check if too many failures
        long failedCount = this.scheduledPayments.stream()
            .filter(p -> p.getStatus() == ScheduledPayment.PaymentStatus.FAILED)
            .count();

        if (failedCount >= 3) {
            this.status = ScheduleStatus.DEFAULTED;
        }
    }

    /**
     * Pauses the payment schedule
     */
    public void pause() {
        if (this.status != ScheduleStatus.ACTIVE) {
            throw new IllegalStateException("Can only pause active schedules");
        }

        this.status = ScheduleStatus.PAUSED;
    }

    /**
     * Resumes the payment schedule
     */
    public void resume() {
        if (this.status != ScheduleStatus.PAUSED) {
            throw new IllegalStateException("Can only resume paused schedules");
        }

        this.status = ScheduleStatus.ACTIVE;
    }

    /**
     * Cancels the payment schedule
     */
    public void cancel(String reason) {
        if (this.status == ScheduleStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed schedules");
        }

        this.status = ScheduleStatus.CANCELLED;
        this.notes = reason;
    }

    /**
     * Reschedules the payment schedule
     */
    public void reschedule(LocalDate newStartDate, Integer newInstallmentCount, String reason) {
        if (this.status == ScheduleStatus.COMPLETED || this.status == ScheduleStatus.CANCELLED) {
            throw new IllegalStateException("Cannot reschedule completed or cancelled schedules");
        }

        this.originalScheduleId = this.scheduleId;
        this.isRescheduled = true;
        this.rescheduleReason = reason;
        this.rescheduleDate = LocalDate.now();
        this.startDate = newStartDate;
        this.installmentCount = newInstallmentCount;
        this.totalInstallments = newInstallmentCount;

        // Regenerate installments (only for pending ones)
        this.scheduledPayments.removeIf(p -> p.getStatus() != ScheduledPayment.PaymentStatus.PENDING);
        generateInstallments();
    }

    /**
     * Skips a scheduled payment
     */
    public void skipPayment(String scheduledPaymentId, String reason) {
        if (this.status != ScheduleStatus.ACTIVE) {
            throw new IllegalStateException("Can only skip payments for active schedules");
        }

        ScheduledPayment payment = this.scheduledPayments.stream()
            .filter(p -> p.getScheduledPaymentId().equals(scheduledPaymentId))
            .findFirst()
            .orElseThrow(() -> new com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException(
                "Scheduled payment not found"));

        if (payment.getStatus() != ScheduledPayment.PaymentStatus.PENDING) {
            throw new IllegalStateException("Can only skip pending payments");
        }

        payment.setStatus(ScheduledPayment.PaymentStatus.SKIPPED);
        payment.setNotes(reason);
    }

    /**
     * Adds a tag to the payment schedule
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
     * Removes a tag from the payment schedule
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Gets completion percentage
     */
    public double getCompletionPercentage() {
        if (this.totalInstallments == null || this.totalInstallments == 0) {
            return 0;
        }
        return (this.completedInstallments * 100.0) / this.totalInstallments;
    }

    private void generateInstallments() {
        if (this.scheduledPayments == null) {
            this.scheduledPayments = new ArrayList<>();
        }
        this.scheduledPayments.clear();

        if (this.installmentCount == null || this.installmentCount <= 0) {
            return;
        }

        // Calculate installment amount
        this.installmentAmount = this.totalAmount.divide(
            new BigDecimal(this.installmentCount), 2, java.math.RoundingMode.HALF_UP);

        LocalDate currentDate = this.startDate;

        for (int i = 1; i <= this.installmentCount; i++) {
            ScheduledPayment payment = ScheduledPayment.builder()
                .scheduledPaymentId(java.util.UUID.randomUUID().toString())
                .installmentNumber(i)
                .amount(this.installmentAmount)
                .dueDate(currentDate)
                .status(ScheduledPayment.PaymentStatus.PENDING)
                .build();

            this.scheduledPayments.add(payment);

            // Calculate next due date based on frequency
            currentDate = calculateNextDate(currentDate);
        }

        updateNextPaymentDate();
    }

    private LocalDate calculateNextDate(LocalDate currentDate) {
        return switch (this.frequency != null ? this.frequency.toLowerCase() : "monthly") {
            case "weekly", "1w" -> currentDate.plusWeeks(1);
            case "bi-weekly", "2w" -> currentDate.plusWeeks(2);
            case "monthly", "1m" -> currentDate.plusMonths(1);
            case "quarterly", "3m" -> currentDate.plusMonths(3);
            case "semi-annual", "6m" -> currentDate.plusMonths(6);
            case "annual", "yearly", "1y" -> currentDate.plusYears(1);
            default -> currentDate.plusMonths(1);
        };
    }

    private void updateNextPaymentDate() {
        this.nextPaymentDate = this.scheduledPayments.stream()
            .filter(p -> p.getStatus() == ScheduledPayment.PaymentStatus.PENDING)
            .map(p -> p.getDueDate().toString())
            .findFirst()
            .orElse(null);
    }

    public void clearDomainEvents() {
        // No domain events in PaymentSchedule currently
    }
}
