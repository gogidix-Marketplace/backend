package com.gogidix.sales.territory.domain.model;

import com.gogidix.sales.territory.domain.event.QuotaUpdatedEvent;
import com.gogidix.sales.territory.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Quota Domain Entity
 * Represents sales quota assigned to a territory
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "quotas")
public class Quota extends BaseEntity {

    private String quotaId;
    private String tenantId;
    private String territoryId;
    private String salesRepresentativeId;

    // Quota details
    private QuotaType type;
    private BigDecimal amount;
    private String currency;
    private QuotaPeriod period;
    private Integer year;
    private Integer month;
    private LocalDate startDate;
    private LocalDate endDate;

    // Breakdown
    private List<QuotaBreakdown> breakdown;

    // Status
    private QuotaStatus status;

    // Performance tracking
    private BigDecimal currentAchievement;
    private BigDecimal attainmentPercentage;
    private Instant lastCalculatedAt;

    // Approval
    private String approvedBy;
    private Instant approvedAt;

    @Builder.Default
    private List<QuotaUpdatedEvent> domainEvents = new ArrayList<>();

    public enum QuotaType {
        REVENUE,
        UNITS,
        MARGIN,
        ACTIVITY
    }

    public enum QuotaPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        ANNUAL
    }

    public enum QuotaStatus {
        DRAFT,
        ACTIVE,
        PAUSED,
        COMPLETED,
        CANCELLED
    }

    /**
     * Creates a new quota
     */
    public static Quota create(String tenantId, String territoryId,
                                String salesRepresentativeId,
                                QuotaType type, BigDecimal amount,
                                String currency, QuotaPeriod period,
                                LocalDate startDate, LocalDate endDate) {
        Quota quota = Quota.builder()
                .tenantId(tenantId)
                .territoryId(territoryId)
                .salesRepresentativeId(salesRepresentativeId)
                .type(type)
                .amount(amount)
                .currency(currency)
                .period(period)
                .startDate(startDate)
                .endDate(endDate)
                .status(QuotaStatus.DRAFT)
                .currentAchievement(BigDecimal.ZERO)
                .breakdown(new ArrayList<>())
                .build();

        // Set year and month from start date
        if (startDate != null) {
            quota.year = startDate.getYear();
            quota.month = startDate.getMonthValue();
        }

        quota.addDomainEvent(QuotaUpdatedEvent.builder()
                .quotaId(quota.getQuotaId())
                .tenantId(tenantId)
                .territoryId(territoryId)
                .amount(amount)
                .currency(currency)
                .eventType("QUOTA_CREATED")
                .timestamp(Instant.now())
                .build());

        return quota;
    }

    /**
     * Activates the quota
     */
    public void activate(String approvedBy) {
        if (this.status != QuotaStatus.DRAFT) {
            throw new IllegalStateException("Can only activate draft quotas");
        }

        this.status = QuotaStatus.ACTIVE;
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();

        addDomainEvent(QuotaUpdatedEvent.builder()
                .quotaId(this.quotaId)
                .tenantId(this.tenantId)
                .territoryId(this.territoryId)
                .amount(this.amount)
                .currency(this.currency)
                .eventType("QUOTA_ACTIVATED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Pauses the quota
     */
    public void pause() {
        if (this.status != QuotaStatus.ACTIVE) {
            throw new IllegalStateException("Can only pause active quotas");
        }
        this.status = QuotaStatus.PAUSED;
    }

    /**
     * Resumes the quota
     */
    public void resume() {
        if (this.status != QuotaStatus.PAUSED) {
            throw new IllegalStateException("Can only resume paused quotas");
        }
        this.status = QuotaStatus.ACTIVE;
    }

    /**
     * Cancels the quota
     */
    public void cancel() {
        if (this.status == QuotaStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed quotas");
        }
        this.status = QuotaStatus.CANCELLED;
    }

    /**
     * Updates the achievement
     */
    public void updateAchievement(BigDecimal achievement) {
        this.currentAchievement = achievement != null ? achievement : BigDecimal.ZERO;

        if (this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0) {
            this.attainmentPercentage = this.currentAchievement
                    .divide(this.amount, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        this.lastCalculatedAt = Instant.now();

        if (this.attainmentPercentage != null &&
            this.attainmentPercentage.compareTo(new BigDecimal("100")) >= 0) {
            this.status = QuotaStatus.COMPLETED;
        }
    }

    /**
     * Adjusts the quota amount
     */
    public void adjustAmount(BigDecimal newAmount, String adjustedBy) {
        if (this.status == QuotaStatus.COMPLETED || this.status == QuotaStatus.CANCELLED) {
            throw new IllegalStateException("Cannot adjust completed or cancelled quotas");
        }

        BigDecimal oldAmount = this.amount;
        this.amount = newAmount;

        // Recalculate attainment percentage
        updateAchievement(this.currentAchievement);

        addDomainEvent(QuotaUpdatedEvent.builder()
                .quotaId(this.quotaId)
                .tenantId(this.tenantId)
                .territoryId(this.territoryId)
                .oldAmount(oldAmount)
                .amount(newAmount)
                .currency(this.currency)
                .adjustedBy(adjustedBy)
                .eventType("QUOTA_ADJUSTED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Adds breakdown item
     */
    public void addBreakdown(String category, BigDecimal amount, String description) {
        if (this.breakdown == null) {
            this.breakdown = new ArrayList<>();
        }

        QuotaBreakdown item = QuotaBreakdown.builder()
                .category(category)
                .amount(amount)
                .description(description)
                .build();

        this.breakdown.add(item);
    }

    /**
     * Checks if quota is on track
     */
    public boolean isOnTrack() {
        if (this.status != QuotaStatus.ACTIVE) {
            return false;
        }

        if (this.attainmentPercentage == null) {
            return false;
        }

        // Simple heuristic: on track if at least 80% of expected progress
        LocalDate now = LocalDate.now();
        if (this.startDate == null || this.endDate == null) {
            return this.attainmentPercentage.compareTo(new BigDecimal("80")) >= 0;
        }

        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(this.startDate, this.endDate);
        long elapsedDays = java.time.temporal.ChronoUnit.DAYS.between(this.startDate, now);
        double expectedProgress = (double) elapsedDays / totalDays * 100;

        return this.attainmentPercentage.compareTo(BigDecimal.valueOf(expectedProgress * 0.8)) >= 0;
    }

    /**
     * Gets remaining quota amount
     */
    public BigDecimal getRemainingAmount() {
        if (this.amount == null || this.currentAchievement == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal remaining = this.amount.subtract(this.currentAchievement);
        return remaining.compareTo(BigDecimal.ZERO) > 0 ? remaining : BigDecimal.ZERO;
    }

    public void addDomainEvent(QuotaUpdatedEvent event) {
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
     * Quota Breakdown nested class
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuotaBreakdown {
        private String category;
        private BigDecimal amount;
        private String description;
        private String productId;
        private String productCategoryId;
    }
}
