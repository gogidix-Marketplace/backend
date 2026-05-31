package com.gogidix.finance.accountspayable.infrastructure.persistence.mongodb;

import com.gogidix.finance.accountspayable.domain.model.PaymentSchedule;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "payment_schedules")
public class PaymentScheduleEntity {

    @Id
    private String id;

    @Indexed
    @Field("schedule_id")
    private String scheduleId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("vendor_id")
    private String vendorId;

    @Field("vendor_name")
    private String vendorName;

    @Field("invoice_id")
    private String invoiceId;

    @Field("invoice_number")
    private String invoiceNumber;

    @Field("schedule_type")
    private String scheduleType;

    @Field("total_amount")
    private BigDecimal totalAmount;

    @Field("currency")
    private String currency;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    @Field("frequency")
    private String frequency;

    @Field("installments")
    private Integer installments;

    @Field("installment_amount")
    private BigDecimal installmentAmount;

    @Field("scheduled_payments")
    private List<ScheduledPaymentEmbed> scheduledPayments;

    @Indexed
    @Field("status")
    private String status;

    @Field("auto_payment_method")
    private String autoPaymentMethod;

    @Field("bank_account_id")
    private String bankAccountId;

    @Field("description")
    private String description;

    @Field("notes")
    private String notes;

    @Field("next_payment_date")
    private LocalDate nextPaymentDate;

    @Field("remaining_installments")
    private Integer remainingInstallments;

    @Field("paid_amount")
    private BigDecimal paidAmount;

    @Field("remaining_amount")
    private BigDecimal remainingAmount;

    @Field("created_by")
    private String createdBy;

    @Field("approved_by")
    private String approvedBy;

    @Field("last_payment_date")
    private LocalDate lastPaymentDate;

    public PaymentScheduleEntity() {
    }

    public PaymentScheduleEntity(PaymentSchedule schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.tenantId = schedule.getTenantId();
        this.vendorId = schedule.getVendorId();
        this.vendorName = schedule.getVendorName();
        this.invoiceId = schedule.getInvoiceId();
        this.invoiceNumber = schedule.getInvoiceNumber();
        this.scheduleType = schedule.getScheduleType() != null ? schedule.getScheduleType().name() : null;
        this.totalAmount = schedule.getTotalAmount();
        this.currency = schedule.getCurrency();
        this.startDate = schedule.getStartDate();
        this.endDate = schedule.getEndDate();
        this.frequency = schedule.getFrequency() != null ? schedule.getFrequency().name() : null;
        this.installments = schedule.getInstallments();
        this.installmentAmount = schedule.getInstallmentAmount();
        this.status = schedule.getStatus() != null ? schedule.getStatus().name() : null;
        this.autoPaymentMethod = schedule.getAutoPaymentMethod();
        this.bankAccountId = schedule.getBankAccountId();
        this.description = schedule.getDescription();
        this.notes = schedule.getNotes();
        this.nextPaymentDate = schedule.getNextPaymentDate();
        this.remainingInstallments = schedule.getRemainingInstallments();
        this.paidAmount = schedule.getPaidAmount();
        this.remainingAmount = schedule.getRemainingAmount();
        this.createdBy = schedule.getCreatedBy();
        this.approvedBy = schedule.getApprovedBy();
        this.lastPaymentDate = schedule.getLastPaymentDate();

        if (schedule.getScheduledPayments() != null) {
            this.scheduledPayments = new ArrayList<>();
            for (PaymentSchedule.ScheduledPayment sp : schedule.getScheduledPayments()) {
                this.scheduledPayments.add(new ScheduledPaymentEmbed(sp));
            }
        }
    }

    public PaymentSchedule toDomainModel() {
        List<PaymentSchedule.ScheduledPayment> paymentList = new ArrayList<>();
        if (this.scheduledPayments != null) {
            for (ScheduledPaymentEmbed embed : this.scheduledPayments) {
                paymentList.add(embed.toDomainModel());
            }
        }

        return PaymentSchedule.builder()
                .scheduleId(this.scheduleId)
                .tenantId(this.tenantId)
                .vendorId(this.vendorId)
                .vendorName(this.vendorName)
                .invoiceId(this.invoiceId)
                .invoiceNumber(this.invoiceNumber)
                .scheduleType(this.scheduleType != null ? PaymentSchedule.ScheduleType.valueOf(this.scheduleType) : null)
                .totalAmount(this.totalAmount)
                .currency(this.currency)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .frequency(this.frequency != null ? PaymentSchedule.ScheduleFrequency.valueOf(this.frequency) : null)
                .installments(this.installments)
                .installmentAmount(this.installmentAmount)
                .scheduledPayments(paymentList)
                .status(this.status != null ? PaymentSchedule.ScheduleStatus.valueOf(this.status) : null)
                .autoPaymentMethod(this.autoPaymentMethod)
                .bankAccountId(this.bankAccountId)
                .description(this.description)
                .notes(this.notes)
                .nextPaymentDate(this.nextPaymentDate)
                .remainingInstallments(this.remainingInstallments)
                .paidAmount(this.paidAmount)
                .remainingAmount(this.remainingAmount)
                .createdBy(this.createdBy)
                .approvedBy(this.approvedBy)
                .lastPaymentDate(this.lastPaymentDate)
                .build();
    }

    public static class ScheduledPaymentEmbed {
        private String paymentId;
        private LocalDate scheduledDate;
        private BigDecimal amount;
        private String status;
        private String paymentReference;
        private Instant processedAt;
        private String failureReason;

        public ScheduledPaymentEmbed() {
        }

        public ScheduledPaymentEmbed(PaymentSchedule.ScheduledPayment sp) {
            this.paymentId = sp.getPaymentId();
            this.scheduledDate = sp.getScheduledDate();
            this.amount = sp.getAmount();
            this.status = sp.getStatus() != null ? sp.getStatus().name() : null;
            this.paymentReference = sp.getPaymentReference();
            this.processedAt = sp.getProcessedAt();
            this.failureReason = sp.getFailureReason();
        }

        public PaymentSchedule.ScheduledPayment toDomainModel() {
            return PaymentSchedule.ScheduledPayment.builder()
                    .paymentId(this.paymentId)
                    .scheduledDate(this.scheduledDate)
                    .amount(this.amount)
                    .status(this.status != null ? PaymentSchedule.ScheduledPayment.PaymentStatus.valueOf(this.status) : null)
                    .paymentReference(this.paymentReference)
                    .processedAt(this.processedAt)
                    .failureReason(this.failureReason)
                    .build();
        }

        public String getPaymentId() { return paymentId; }
        public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
        public LocalDate getScheduledDate() { return scheduledDate; }
        public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getPaymentReference() { return paymentReference; }
        public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }
        public Instant getProcessedAt() { return processedAt; }
        public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }
        public String getFailureReason() { return failureReason; }
        public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public String getScheduleType() { return scheduleType; }
    public void setScheduleType(String scheduleType) { this.scheduleType = scheduleType; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public Integer getInstallments() { return installments; }
    public void setInstallments(Integer installments) { this.installments = installments; }
    public BigDecimal getInstallmentAmount() { return installmentAmount; }
    public void setInstallmentAmount(BigDecimal installmentAmount) { this.installmentAmount = installmentAmount; }
    public List<ScheduledPaymentEmbed> getScheduledPayments() { return scheduledPayments; }
    public void setScheduledPayments(List<ScheduledPaymentEmbed> scheduledPayments) { this.scheduledPayments = scheduledPayments; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAutoPaymentMethod() { return autoPaymentMethod; }
    public void setAutoPaymentMethod(String autoPaymentMethod) { this.autoPaymentMethod = autoPaymentMethod; }
    public String getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(String bankAccountId) { this.bankAccountId = bankAccountId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDate getNextPaymentDate() { return nextPaymentDate; }
    public void setNextPaymentDate(LocalDate nextPaymentDate) { this.nextPaymentDate = nextPaymentDate; }
    public Integer getRemainingInstallments() { return remainingInstallments; }
    public void setRemainingInstallments(Integer remainingInstallments) { this.remainingInstallments = remainingInstallments; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public LocalDate getLastPaymentDate() { return lastPaymentDate; }
    public void setLastPaymentDate(LocalDate lastPaymentDate) { this.lastPaymentDate = lastPaymentDate; }
}
