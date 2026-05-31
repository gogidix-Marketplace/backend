package com.gogidix.sales.forecast.domain.model;

import com.gogidix.sales.forecast.domain.event.*;
import com.gogidix.sales.forecast.shared.base.BaseEntity;
import com.gogidix.sales.forecast.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Forecast Domain Entity
 * Multi-tenant sales forecast with approval workflow
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "forecasts")
public class Forecast extends BaseEntity {

    private String forecastId;

    private String tenantId;

    private String name;

    private String description;

    private ForecastPeriod period;

    private YearMonth startDate;

    private YearMonth endDate;

    private ForecastStatus status;

    private String createdBy;

    private String approvedBy;

    private Integer version;

    private String parentForecastId;

    private BigDecimal totalBestCase;

    private BigDecimal totalLikely;

    private BigDecimal totalWorstCase;

    private String currency;

    private String region;

    private String territory;

    private String businessUnit;

    private List<ForecastLineItem> lineItems;

    private List<ForecastAdjustment> adjustmentHistory;

    private ApprovalLevel currentApprovalLevel;

    private String rejectionReason;

    private Boolean locked;

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public enum ForecastPeriod {
        MONTHLY,
        QUARTERLY,
        ANNUAL
    }

    public enum ForecastStatus {
        DRAFT,
        SUBMITTED,
        PENDING_APPROVAL,
        APPROVED,
        REJECTED,
        PUBLISHED,
        ARCHIVED
    }

    public enum ApprovalLevel {
        NONE,
        MANAGER,
        DIRECTOR,
        VP,
        EXECUTIVE
    }

    /**
     * Creates a new forecast
     */
    public static Forecast create(String tenantId, String name, String description,
                                   ForecastPeriod period, YearMonth startDate,
                                   YearMonth endDate, String createdBy, String currency) {
        String forecastId = "FC-" + System.currentTimeMillis();

        Forecast forecast = Forecast.builder()
            .forecastId(forecastId)
            .tenantId(tenantId)
            .name(name)
            .description(description)
            .period(period)
            .startDate(startDate)
            .endDate(endDate)
            .status(ForecastStatus.DRAFT)
            .currentApprovalLevel(ApprovalLevel.NONE)
            .createdBy(createdBy)
            .currency(currency)
            .version(1)
            .lineItems(new ArrayList<>())
            .adjustmentHistory(new ArrayList<>())
            .locked(false)
            .build();

        forecast.addDomainEvent(ForecastCreatedEvent.builder()
            .forecastId(forecast.getForecastId())
            .tenantId(tenantId)
            .name(name)
            .period(period.name())
            .startDate(startDate)
            .endDate(endDate)
            .createdBy(createdBy)
            .eventType("FORECAST_CREATED")
            .build());

        return forecast;
    }

    /**
     * Submits the forecast for approval
     */
    public void submit() {
        if (this.status != ForecastStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft forecasts");
        }

        validateForSubmission();

        this.status = ForecastStatus.SUBMITTED;
        this.currentApprovalLevel = ApprovalLevel.MANAGER;

        addDomainEvent(ForecastUpdatedEvent.builder()
            .forecastId(this.forecastId)
            .tenantId(this.tenantId)
            .name(this.name)
            .status(this.status.name())
            .eventType("FORECAST_SUBMITTED")
            .build());
    }

    /**
     * Approves the forecast
     */
    public void approve(String approver, ApprovalLevel level) {
        if (!canApprove(level)) {
            throw new IllegalStateException("Cannot approve at this level");
        }

        this.approvedBy = approver;
        this.currentApprovalLevel = level;

        if (level == ApprovalLevel.EXECUTIVE || level == ApprovalLevel.VP) {
            this.status = ForecastStatus.APPROVED;
        }

        addDomainEvent(ForecastApprovedEvent.builder()
            .forecastId(this.forecastId)
            .tenantId(this.tenantId)
            .name(this.name)
            .approvedBy(approver)
            .approvalLevel(level.name())
            .eventType("FORECAST_APPROVED")
            .build());
    }

    /**
     * Rejects the forecast
     */
    public void reject(String rejecter, String reason) {
        if (this.status == ForecastStatus.PUBLISHED || this.status == ForecastStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot reject published or archived forecasts");
        }

        this.status = ForecastStatus.REJECTED;
        this.approvedBy = rejecter;
        this.rejectionReason = reason;

        addDomainEvent(ForecastUpdatedEvent.builder()
            .forecastId(this.forecastId)
            .tenantId(this.tenantId)
            .name(this.name)
            .status(this.status.name())
            .rejectionReason(reason)
            .eventType("FORECAST_REJECTED")
            .build());
    }

    /**
     * Publishes the approved forecast
     */
    public void publish() {
        if (this.status != ForecastStatus.APPROVED) {
            throw new IllegalStateException("Can only publish approved forecasts");
        }

        this.status = ForecastStatus.PUBLISHED;

        addDomainEvent(ForecastUpdatedEvent.builder()
            .forecastId(this.forecastId)
            .tenantId(this.tenantId)
            .name(this.name)
            .status(this.status.name())
            .eventType("FORECAST_PUBLISHED")
            .build());
    }

    /**
     * Archives the forecast
     */
    public void archive() {
        if (this.status != ForecastStatus.PUBLISHED) {
            throw new IllegalStateException("Can only archive published forecasts");
        }

        this.status = ForecastStatus.ARCHIVED;
    }

    /**
     * Adds a line item to the forecast
     */
    public void addLineItem(ForecastLineItem lineItem) {
        if (this.locked) {
            throw new IllegalStateException("Cannot modify locked forecast");
        }

        if (this.lineItems == null) {
            this.lineItems = new ArrayList<>();
        }

        this.lineItems.add(lineItem);
        recalculateTotals();
    }

    /**
     * Updates a line item in the forecast
     */
    public void updateLineItem(String lineItemId, ForecastLineItem.ForecastCategory category,
                               BigDecimal bestCase, BigDecimal likely, BigDecimal worstCase) {
        if (this.locked) {
            throw new IllegalStateException("Cannot modify locked forecast");
        }

        ForecastLineItem item = this.lineItems.stream()
            .filter(li -> li.getLineItemId().equals(lineItemId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Line item not found"));

        item.updateValues(category, bestCase, likely, worstCase);
        recalculateTotals();

        addDomainEvent(ForecastUpdatedEvent.builder()
            .forecastId(this.forecastId)
            .tenantId(this.tenantId)
            .name(this.name)
            .eventType("LINE_ITEM_UPDATED")
            .build());
    }

    /**
     * Removes a line item from the forecast
     */
    public void removeLineItem(String lineItemId) {
        if (this.locked) {
            throw new IllegalStateException("Cannot modify locked forecast");
        }

        this.lineItems.removeIf(li -> li.getLineItemId().equals(lineItemId));
        recalculateTotals();
    }

    /**
     * Adds an adjustment record
     */
    public void addAdjustment(String adjustedBy, String reason,
                               BigDecimal previousAmount, BigDecimal newAmount) {
        if (this.adjustmentHistory == null) {
            this.adjustmentHistory = new ArrayList<>();
        }

        ForecastAdjustment adjustment = ForecastAdjustment.builder()
            .adjustmentId("ADJ-" + System.currentTimeMillis())
            .adjustedBy(adjustedBy)
            .adjustedAt(java.time.Instant.now())
            .reason(reason)
            .previousAmount(previousAmount)
            .newAmount(newAmount)
            .build();

        this.adjustmentHistory.add(adjustment);
    }

    /**
     * Locks the forecast
     */
    public void lock() {
        this.locked = true;
    }

    /**
     * Unlocks the forecast
     */
    public void unlock() {
        this.locked = false;
    }

    /**
     * Calculates forecast accuracy based on actuals
     */
    public BigDecimal calculateAccuracy(BigDecimal actualAmount) {
        if (totalLikely == null || totalLikely.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return actualAmount.divide(totalLikely, 4, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }

    /**
     * Creates a new version of this forecast
     */
    public Forecast createNewVersion(String updatedBy) {
        Forecast newForecast = Forecast.builder()
            .tenantId(this.tenantId)
            .name(this.name + " (v" + (this.version + 1) + ")")
            .description(this.description)
            .period(this.period)
            .startDate(this.startDate)
            .endDate(this.endDate)
            .status(ForecastStatus.DRAFT)
            .currentApprovalLevel(ApprovalLevel.NONE)
            .createdBy(updatedBy)
            .currency(this.currency)
            .region(this.region)
            .territory(this.territory)
            .businessUnit(this.businessUnit)
            .version(this.version + 1)
            .parentForecastId(this.forecastId)
            .lineItems(new ArrayList<>(this.lineItems))
            .adjustmentHistory(new ArrayList<>())
            .locked(false)
            .build();

        newForecast.addDomainEvent(ForecastCreatedEvent.builder()
            .forecastId(newForecast.getForecastId())
            .tenantId(this.tenantId)
            .name(newForecast.getName())
            .period(this.period.name())
            .startDate(this.startDate)
            .endDate(this.endDate)
            .createdBy(updatedBy)
            .eventType("FORECAST_VERSION_CREATED")
            .build());

        return newForecast;
    }

    private void validateForSubmission() {
        if (this.name == null || this.name.isBlank()) {
            throw new ValidationException("name", "Forecast name is required");
        }
        if (this.startDate == null) {
            throw new ValidationException("startDate", "Start date is required");
        }
        if (this.endDate == null) {
            throw new ValidationException("endDate", "End date is required");
        }
        if (this.lineItems == null || this.lineItems.isEmpty()) {
            throw new ValidationException("lineItems", "At least one line item is required");
        }
    }

    private boolean canApprove(ApprovalLevel level) {
        return this.status == ForecastStatus.SUBMITTED ||
               this.status == ForecastStatus.PENDING_APPROVAL ||
               (this.status == ForecastStatus.APPROVED &&
                (this.currentApprovalLevel == ApprovalLevel.MANAGER && level == ApprovalLevel.DIRECTOR) ||
                (this.currentApprovalLevel == ApprovalLevel.DIRECTOR && level == ApprovalLevel.VP));
    }

    private void recalculateTotals() {
        this.totalBestCase = this.lineItems.stream()
            .map(ForecastLineItem::getBestCase)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalLikely = this.lineItems.stream()
            .map(ForecastLineItem::getLikely)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalWorstCase = this.lineItems.stream()
            .map(ForecastLineItem::getWorstCase)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addDomainEvent(Object event) {
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
