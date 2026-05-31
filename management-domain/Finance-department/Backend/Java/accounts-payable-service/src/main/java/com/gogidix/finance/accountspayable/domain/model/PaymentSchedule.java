package com.gogidix.finance.accountspayable.domain.model;

import com.gogidix.finance.accountspayable.shared.base.BaseEntity;
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
 * Manages scheduled payments for invoices and vendors
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "payment_schedules")
public class PaymentSchedule extends BaseEntity {

    private String scheduleId;

    private String tenantId;

    private String vendorId;

    private String vendorName;

    private String invoiceId;

    private String invoiceNumber;

    private ScheduleType scheduleType;

    private BigDecimal totalAmount;

    private String currency;

    private LocalDate startDate;

    private LocalDate endDate;

    private ScheduleFrequency frequency;

    private Integer installments;

    private BigDecimal installmentAmount;

    private List<ScheduledPayment> scheduledPayments;

    private ScheduleStatus status;

    private String autoPaymentMethod;

    private String bankAccountId;

    private String description;

    private String notes;

    private LocalDate nextPaymentDate;

    private Integer remainingInstallments;

    private BigDecimal paidAmount;

    private BigDecimal remainingAmount;

    private String createdBy;

    private String approvedBy;

    private LocalDate lastPaymentDate;

    public enum ScheduleType {
        INVOICE_BASED,
        RECURRING,
        INSTALLMENT_PLAN,
        MILESTONE_BASED
    }

    public enum ScheduleFrequency {
        DAILY,
        WEEKLY,
        BI_WEEKLY,
        MONTHLY,
        QUARTERLY,
        SEMI_ANNUALLY,
        ANNUALLY,
        CUSTOM
    }

    public enum ScheduleStatus {
        PENDING,
        ACTIVE,
        PAUSED,
        COMPLETED,
        CANCELLED,
        FAILED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduledPayment {
        private String paymentId;
        private LocalDate scheduledDate;
        private BigDecimal amount;
        private PaymentStatus status;
        private String paymentReference;
        private Instant processedAt;
        private String failureReason;

        public enum PaymentStatus {
            PENDING,
            PROCESSED,
            FAILED,
            SKIPPED,
            CANCELLED
        }
    }

    /**
     * Creates a new payment schedule
     */
    public static PaymentSchedule create(String tenantId, String vendorId, String vendorName,
                                          String invoiceId, String invoiceNumber,
                                          ScheduleType scheduleType, BigDecimal totalAmount,
                                          String currency, LocalDate startDate,
                                          ScheduleFrequency frequency, Integer installments,
                                          String createdBy) {
        BigDecimal installmentAmount = totalAmount.divide(new BigDecimal(installments), 2,
            java.math.RoundingMode.HALF_UP);

        List<ScheduledPayment> scheduledPayments = new ArrayList<>();
        LocalDate currentDate = startDate;

        for (int i = 0; i < installments; i++) {
            ScheduledPayment payment = ScheduledPayment.builder()
                .paymentId(java.util.UUID.randomUUID().toString())
                .scheduledDate(currentDate)
                .amount(installmentAmount)
                .status(ScheduledPayment.PaymentStatus.PENDING)
                .build();

            scheduledPayments.add(payment);

            // Calculate next date based on frequency
            currentDate = calculateNextDate(currentDate, frequency);
        }

        return PaymentSchedule.builder()
            .tenantId(tenantId)
            .vendorId(vendorId)
            .vendorName(vendorName)
            .invoiceId(invoiceId)
            .invoiceNumber(invoiceNumber)
            .scheduleType(scheduleType)
            .totalAmount(totalAmount)
            .currency(currency)
            .startDate(startDate)
            .frequency(frequency)
            .installments(installments)
            .installmentAmount(installmentAmount)
            .scheduledPayments(scheduledPayments)
            .status(ScheduleStatus.PENDING)
            .remainingInstallments(installments)
            .paidAmount(BigDecimal.ZERO)
            .remainingAmount(totalAmount)
            .nextPaymentDate(startDate)
            .createdBy(createdBy)
            .build();
    }

    /**
     * Activates the schedule
     */
    public void activate(String approvedBy) {
        if (this.status != ScheduleStatus.PENDING) {
            throw new IllegalStateException("Can only activate pending schedules");
        }

        this.status = ScheduleStatus.ACTIVE;
        this.approvedBy = approvedBy;
    }

    /**
     * Pauses the schedule
     */
    public void pause() {
        if (this.status != ScheduleStatus.ACTIVE) {
            throw new IllegalStateException("Can only pause active schedules");
        }

        this.status = ScheduleStatus.PAUSED;
    }

    /**
     * Resumes the schedule
     */
    public void resume() {
        if (this.status != ScheduleStatus.PAUSED) {
            throw new IllegalStateException("Can only resume paused schedules");
        }

        this.status = ScheduleStatus.ACTIVE;
    }

    /**
     * Cancels the schedule
     */
    public void cancel() {
        if (this.status == ScheduleStatus.COMPLETED || this.status == ScheduleStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel completed or already cancelled schedules");
        }

        this.status = ScheduleStatus.CANCELLED;

        // Cancel all pending payments
        for (ScheduledPayment payment : this.scheduledPayments) {
            if (payment.getStatus() == ScheduledPayment.PaymentStatus.PENDING) {
                payment.setStatus(ScheduledPayment.PaymentStatus.CANCELLED);
            }
        }
    }

    /**
     * Processes a scheduled payment
     */
    public void processPayment(String paymentId, String paymentReference) {
        ScheduledPayment payment = findScheduledPayment(paymentId);
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found in schedule");
        }

        if (payment.getStatus() != ScheduledPayment.PaymentStatus.PENDING) {
            throw new IllegalStateException("Can only process pending payments");
        }

        payment.setStatus(ScheduledPayment.PaymentStatus.PROCESSED);
        payment.setPaymentReference(paymentReference);
        payment.setProcessedAt(Instant.now());

        this.paidAmount = this.paidAmount.add(payment.getAmount());
        this.remainingAmount = this.totalAmount.subtract(this.paidAmount);
        this.remainingInstallments--;
        this.lastPaymentDate = LocalDate.now();

        updateNextPaymentDate();
        checkCompletion();
    }

    /**
     * Marks a payment as failed
     */
    public void markPaymentFailed(String paymentId, String reason) {
        ScheduledPayment payment = findScheduledPayment(paymentId);
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found in schedule");
        }

        payment.setStatus(ScheduledPayment.PaymentStatus.FAILED);
        payment.setFailureReason(reason);

        // If too many failures, pause the schedule
        long failureCount = this.scheduledPayments.stream()
            .filter(p -> p.getStatus() == ScheduledPayment.PaymentStatus.FAILED)
            .count();

        if (failureCount >= 3) {
            this.status = ScheduleStatus.FAILED;
        }
    }

    /**
     * Skips a scheduled payment
     */
    public void skipPayment(String paymentId) {
        ScheduledPayment payment = findScheduledPayment(paymentId);
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found in schedule");
        }

        if (payment.getStatus() != ScheduledPayment.PaymentStatus.PENDING) {
            throw new IllegalStateException("Can only skip pending payments");
        }

        payment.setStatus(ScheduledPayment.PaymentStatus.SKIPPED);
        updateNextPaymentDate();
    }

    /**
     * Updates auto payment method
     */
    public void setAutoPayment(String paymentMethod, String bankAccountId) {
        this.autoPaymentMethod = paymentMethod;
        this.bankAccountId = bankAccountId;
    }

    /**
     * Gets pending payments
     */
    public List<ScheduledPayment> getPendingPayments() {
        return this.scheduledPayments.stream()
            .filter(p -> p.getStatus() == ScheduledPayment.PaymentStatus.PENDING)
            .toList();
    }

    /**
     * Gets overdue payments
     */
    public List<ScheduledPayment> getOverduePayments() {
        LocalDate today = LocalDate.now();
        return this.scheduledPayments.stream()
            .filter(p -> p.getStatus() == ScheduledPayment.PaymentStatus.PENDING &&
                       p.getScheduledDate().isBefore(today))
            .toList();
    }

    /**
     * Checks if schedule is complete
     */
    public boolean isComplete() {
        return this.status == ScheduleStatus.COMPLETED;
    }

    /**
     * Gets payment progress percentage
     */
    public int getProgressPercentage() {
        if (this.totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        return this.paidAmount.multiply(new BigDecimal("100"))
            .divide(this.totalAmount, 0, java.math.RoundingMode.HALF_UP)
            .intValue();
    }

    private ScheduledPayment findScheduledPayment(String paymentId) {
        return this.scheduledPayments.stream()
            .filter(p -> p.getPaymentId().equals(paymentId))
            .findFirst()
            .orElse(null);
    }

    private void updateNextPaymentDate() {
        this.nextPaymentDate = this.scheduledPayments.stream()
            .filter(p -> p.getStatus() == ScheduledPayment.PaymentStatus.PENDING)
            .map(ScheduledPayment::getScheduledDate)
            .sorted()
            .findFirst()
            .orElse(null);
    }

    private void checkCompletion() {
        if (this.remainingInstallments <= 0 ||
            this.remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            this.status = ScheduleStatus.COMPLETED;
        }
    }

    private static LocalDate calculateNextDate(LocalDate currentDate, ScheduleFrequency frequency) {
        return switch (frequency) {
            case DAILY -> currentDate.plusDays(1);
            case WEEKLY -> currentDate.plusWeeks(1);
            case BI_WEEKLY -> currentDate.plusWeeks(2);
            case MONTHLY -> currentDate.plusMonths(1);
            case QUARTERLY -> currentDate.plusMonths(3);
            case SEMI_ANNUALLY -> currentDate.plusMonths(6);
            case ANNUALLY -> currentDate.plusYears(1);
            case CUSTOM -> currentDate; // Caller must set custom dates
        };
    }
}
