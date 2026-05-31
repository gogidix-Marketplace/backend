package com.gogidix.sales.revenue.domain.model;

import com.gogidix.sales.revenue.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Revenue Recognition Schedule Domain Entity
 * Defines how revenue should be recognized over time
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "revenue_recognition_schedules")
public class RevenueRecognitionSchedule extends BaseEntity {

    private String scheduleId;

    private String tenantId;

    private String revenueId;

    private String contractId;

    private String customerId;

    private String productId;

    private Revenue.RevenueRecognitionType recognitionType;

    private BigDecimal totalAmount;

    private String currency;

    private LocalDate startDate;

    private LocalDate endDate;

    private ScheduleStatus status;

    private Integer totalPeriods;

    private Integer completedPeriods;

    @Builder.Default
    private List<ScheduleEntry> scheduleEntries = new ArrayList<>();

    private String description;

    private String notes;

    private LocalDate createdDate;

    private String createdBy;

    private LocalDate lastProcessedDate;

    private String lastProcessedBy;

    public enum ScheduleStatus {
        DRAFT,
        ACTIVE,
        PAUSED,
        COMPLETED,
        CANCELLED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleEntry {
        private String entryId;
        private Integer periodNumber;
        private LocalDate recognitionDate;
        private BigDecimal amount;
        private BigDecimal cumulativeAmount;
        private EntryStatus status;
        private LocalDate recognizedDate;
        private String recognizedBy;
        private String notes;

        public enum EntryStatus {
            PENDING,
            RECOGNIZED,
            SKIPPED,
            DEFERRED
        }
    }

    /**
     * Creates a new recognition schedule
     */
    public static RevenueRecognitionSchedule create(String tenantId, String revenueId,
                                                      String contractId, String customerId,
                                                      String productId, Revenue.RevenueRecognitionType recognitionType,
                                                      BigDecimal totalAmount, String currency,
                                                      LocalDate startDate, LocalDate endDate,
                                                      Integer totalPeriods) {
        RevenueRecognitionSchedule schedule = RevenueRecognitionSchedule.builder()
                .tenantId(tenantId)
                .revenueId(revenueId)
                .contractId(contractId)
                .customerId(customerId)
                .productId(productId)
                .recognitionType(recognitionType)
                .totalAmount(totalAmount)
                .currency(currency)
                .startDate(startDate)
                .endDate(endDate)
                .totalPeriods(totalPeriods)
                .completedPeriods(0)
                .status(ScheduleStatus.DRAFT)
                .scheduleEntries(new ArrayList<>())
                .createdDate(LocalDate.now())
                .build();

        schedule.generateScheduleEntries();
        return schedule;
    }

    /**
     * Generates schedule entries based on recognition type
     */
    public void generateScheduleEntries() {
        if (this.recognitionType == Revenue.RevenueRecognitionType.OVER_TIME
                || this.recognitionType == Revenue.RevenueRecognitionType.RATABLE) {
            generateRatableSchedule();
        } else if (this.recognitionType == Revenue.RevenueRecognitionType.MILESTONE) {
            generateMilestoneSchedule();
        }
    }

    /**
     * Generates ratable recognition schedule
     */
    private void generateRatableSchedule() {
        this.scheduleEntries.clear();
        BigDecimal periodAmount = this.totalAmount.divide(
                BigDecimal.valueOf(this.totalPeriods),
                2,
                java.math.RoundingMode.HALF_UP);

        // Distribute rounding error to last period
        BigDecimal cumulativeAmount = BigDecimal.ZERO;
        for (int i = 1; i <= this.totalPeriods; i++) {
            BigDecimal entryAmount = periodAmount;
            if (i == this.totalPeriods) {
                entryAmount = this.totalAmount.subtract(cumulativeAmount);
            }

            LocalDate recognitionDate = calculateRecognitionDate(i);
            cumulativeAmount = cumulativeAmount.add(entryAmount);

            ScheduleEntry entry = ScheduleEntry.builder()
                    .entryId(java.util.UUID.randomUUID().toString())
                    .periodNumber(i)
                    .recognitionDate(recognitionDate)
                    .amount(entryAmount)
                    .cumulativeAmount(cumulativeAmount)
                    .status(ScheduleEntry.EntryStatus.PENDING)
                    .build();

            this.scheduleEntries.add(entry);
        }
    }

    /**
     * Generates milestone-based schedule
     */
    private void generateMilestoneSchedule() {
        this.scheduleEntries.clear();

        // Create placeholder entries for milestones
        ScheduleEntry entry = ScheduleEntry.builder()
                .entryId(java.util.UUID.randomUUID().toString())
                .periodNumber(1)
                .recognitionDate(this.endDate)
                .amount(this.totalAmount)
                .cumulativeAmount(this.totalAmount)
                .status(ScheduleEntry.EntryStatus.PENDING)
                .build();

        this.scheduleEntries.add(entry);
    }

    /**
     * Calculates recognition date for a period
     */
    private LocalDate calculateRecognitionDate(int periodNumber) {
        if (this.totalPeriods == 1) {
            return this.startDate;
        }

        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(this.startDate, this.endDate);
        long periodDays = daysBetween / this.totalPeriods;

        return this.startDate.plusDays(periodDays * periodNumber);
    }

    /**
     * Activates the schedule
     */
    public void activate() {
        if (this.status != ScheduleStatus.DRAFT) {
            throw new IllegalStateException("Can only activate draft schedules");
        }
        this.status = ScheduleStatus.ACTIVE;
    }

    /**
     * Pauses the schedule
     */
    public void pause(String reason) {
        if (this.status != ScheduleStatus.ACTIVE) {
            throw new IllegalStateException("Can only pause active schedules");
        }
        this.status = ScheduleStatus.PAUSED;
        this.notes = reason;
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
    public void cancel(String reason) {
        if (this.status == ScheduleStatus.COMPLETED || this.status == ScheduleStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel completed or cancelled schedules");
        }
        this.status = ScheduleStatus.CANCELLED;
        this.notes = reason;
    }

    /**
     * Marks a schedule entry as recognized
     */
    public void recognizeEntry(String entryId, String recognizedBy) {
        ScheduleEntry entry = findEntryById(entryId);
        if (entry == null) {
            throw new IllegalArgumentException("Entry not found: " + entryId);
        }

        if (entry.getStatus() != ScheduleEntry.EntryStatus.PENDING) {
            throw new IllegalStateException("Entry already processed: " + entryId);
        }

        entry.setStatus(ScheduleEntry.EntryStatus.RECOGNIZED);
        entry.setRecognizedDate(LocalDate.now());
        entry.setRecognizedBy(recognizedBy);

        this.completedPeriods++;
        this.lastProcessedDate = LocalDate.now();
        this.lastProcessedBy = recognizedBy;

        if (this.completedPeriods >= this.totalPeriods) {
            this.status = ScheduleStatus.COMPLETED;
        }
    }

    /**
     * Skips a schedule entry
     */
    public void skipEntry(String entryId, String reason) {
        ScheduleEntry entry = findEntryById(entryId);
        if (entry == null) {
            throw new IllegalArgumentException("Entry not found: " + entryId);
        }

        if (entry.getStatus() != ScheduleEntry.EntryStatus.PENDING) {
            throw new IllegalStateException("Entry already processed: " + entryId);
        }

        entry.setStatus(ScheduleEntry.EntryStatus.SKIPPED);
        entry.setNotes(reason);

        this.completedPeriods++;
        this.lastProcessedDate = LocalDate.now();

        if (this.completedPeriods >= this.totalPeriods) {
            this.status = ScheduleStatus.COMPLETED;
        }
    }

    /**
     * Gets pending entries
     */
    public List<ScheduleEntry> getPendingEntries() {
        return this.scheduleEntries.stream()
                .filter(e -> e.getStatus() == ScheduleEntry.EntryStatus.PENDING)
                .toList();
    }

    /**
     * Gets entries due on or before a date
     */
    public List<ScheduleEntry> getEntriesDueBy(LocalDate date) {
        return this.scheduleEntries.stream()
                .filter(e -> e.getRecognitionDate().isBefore(date.plusDays(1))
                        && e.getStatus() == ScheduleEntry.EntryStatus.PENDING)
                .toList();
    }

    /**
     * Calculates remaining amount
     */
    public BigDecimal getRemainingAmount() {
        return this.scheduleEntries.stream()
                .filter(e -> e.getStatus() == ScheduleEntry.EntryStatus.PENDING)
                .map(ScheduleEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates recognized amount
     */
    public BigDecimal getRecognizedAmount() {
        return this.scheduleEntries.stream()
                .filter(e -> e.getStatus() == ScheduleEntry.EntryStatus.RECOGNIZED)
                .map(ScheduleEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Finds entry by ID
     */
    private ScheduleEntry findEntryById(String entryId) {
        return this.scheduleEntries.stream()
                .filter(e -> e.getEntryId().equals(entryId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets completion percentage
     */
    public BigDecimal getCompletionPercentage() {
        if (this.totalPeriods == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(this.completedPeriods)
                .divide(BigDecimal.valueOf(this.totalPeriods), 2, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    /**
     * Checks if schedule is complete
     */
    public boolean isComplete() {
        return this.status == ScheduleStatus.COMPLETED
                || this.completedPeriods >= this.totalPeriods;
    }
}
