package com.gogidix.sales.forecast.infrastructure.persistence.mongodb;

import com.gogidix.sales.forecast.domain.model.Forecast;
import com.gogidix.sales.forecast.domain.model.ForecastAdjustment;
import com.gogidix.sales.forecast.domain.model.ForecastLineItem;
import com.gogidix.sales.forecast.domain.event.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB document entity for storing Forecast domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "forecasts")
public class ForecastEntity {

    @Id
    private String id;

    @Indexed
    @Field("forecast_id")
    private String forecastId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("period")
    private String period;

    @Field("start_date")
    private YearMonth startDate;

    @Field("end_date")
    private YearMonth endDate;

    @Indexed
    @Field("status")
    private String status;

    @Field("created_by")
    private String createdBy;

    @Field("approved_by")
    private String approvedBy;

    @Field("version")
    private Integer version;

    @Field("parent_forecast_id")
    private String parentForecastId;

    @Field("total_best_case")
    private BigDecimal totalBestCase;

    @Field("total_likely")
    private BigDecimal totalLikely;

    @Field("total_worst_case")
    private BigDecimal totalWorstCase;

    @Field("currency")
    private String currency;

    @Field("region")
    private String region;

    @Field("territory")
    private String territory;

    @Field("business_unit")
    private String businessUnit;

    @Field("line_items")
    private List<ForecastLineItemEmbed> lineItems;

    @Field("adjustment_history")
    private List<ForecastAdjustmentEmbed> adjustmentHistory;

    @Field("current_approval_level")
    private String currentApprovalLevel;

    @Field("rejection_reason")
    private String rejectionReason;

    @Field("locked")
    private Boolean locked;

    @Field("domain_events")
    private List<Object> domainEvents;

    // Default constructor for MongoDB
    public ForecastEntity() {
    }

    // Constructor from domain model
    public ForecastEntity(Forecast forecast) {
        this.forecastId = forecast.getForecastId();
        this.tenantId = forecast.getTenantId();
        this.name = forecast.getName();
        this.description = forecast.getDescription();
        this.period = forecast.getPeriod() != null ? forecast.getPeriod().name() : null;
        this.startDate = forecast.getStartDate();
        this.endDate = forecast.getEndDate();
        this.status = forecast.getStatus() != null ? forecast.getStatus().name() : null;
        this.createdBy = forecast.getCreatedBy();
        this.approvedBy = forecast.getApprovedBy();
        this.version = forecast.getVersion();
        this.parentForecastId = forecast.getParentForecastId();
        this.totalBestCase = forecast.getTotalBestCase();
        this.totalLikely = forecast.getTotalLikely();
        this.totalWorstCase = forecast.getTotalWorstCase();
        this.currency = forecast.getCurrency();
        this.region = forecast.getRegion();
        this.territory = forecast.getTerritory();
        this.businessUnit = forecast.getBusinessUnit();
        this.currentApprovalLevel = forecast.getCurrentApprovalLevel() != null ? forecast.getCurrentApprovalLevel().name() : null;
        this.rejectionReason = forecast.getRejectionReason();
        this.locked = forecast.getLocked();

        // Convert line items
        if (forecast.getLineItems() != null) {
            this.lineItems = new ArrayList<>();
            for (ForecastLineItem item : forecast.getLineItems()) {
                this.lineItems.add(new ForecastLineItemEmbed(item));
            }
        }

        // Convert adjustments
        if (forecast.getAdjustmentHistory() != null) {
            this.adjustmentHistory = new ArrayList<>();
            for (ForecastAdjustment adjustment : forecast.getAdjustmentHistory()) {
                this.adjustmentHistory.add(new ForecastAdjustmentEmbed(adjustment));
            }
        }

        this.domainEvents = forecast.getDomainEvents() != null ? new ArrayList<>(forecast.getDomainEvents()) : new ArrayList<>();
    }

    // Convert to domain model
    public Forecast toDomainModel() {
        List<ForecastLineItem> lineItemList = new ArrayList<>();
        if (this.lineItems != null) {
            for (ForecastLineItemEmbed embed : this.lineItems) {
                lineItemList.add(embed.toDomainModel());
            }
        }

        List<ForecastAdjustment> adjustmentList = new ArrayList<>();
        if (this.adjustmentHistory != null) {
            for (ForecastAdjustmentEmbed embed : this.adjustmentHistory) {
                adjustmentList.add(embed.toDomainModel());
            }
        }

        return Forecast.builder()
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .name(this.name)
                .description(this.description)
                .period(this.period != null ? Forecast.ForecastPeriod.valueOf(this.period) : null)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .status(this.status != null ? Forecast.ForecastStatus.valueOf(this.status) : null)
                .createdBy(this.createdBy)
                .approvedBy(this.approvedBy)
                .version(this.version)
                .parentForecastId(this.parentForecastId)
                .totalBestCase(this.totalBestCase)
                .totalLikely(this.totalLikely)
                .totalWorstCase(this.totalWorstCase)
                .currency(this.currency)
                .region(this.region)
                .territory(this.territory)
                .businessUnit(this.businessUnit)
                .lineItems(lineItemList)
                .adjustmentHistory(adjustmentList)
                .currentApprovalLevel(this.currentApprovalLevel != null ? Forecast.ApprovalLevel.valueOf(this.currentApprovalLevel) : null)
                .rejectionReason(this.rejectionReason)
                .locked(this.locked)
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .build();
    }

    // Update from domain model
    public void updateFrom(Forecast forecast) {
        this.name = forecast.getName();
        this.description = forecast.getDescription();
        this.period = forecast.getPeriod() != null ? forecast.getPeriod().name() : null;
        this.startDate = forecast.getStartDate();
        this.endDate = forecast.getEndDate();
        this.status = forecast.getStatus() != null ? forecast.getStatus().name() : null;
        this.approvedBy = forecast.getApprovedBy();
        this.totalBestCase = forecast.getTotalBestCase();
        this.totalLikely = forecast.getTotalLikely();
        this.totalWorstCase = forecast.getTotalWorstCase();
        this.currentApprovalLevel = forecast.getCurrentApprovalLevel() != null ? forecast.getCurrentApprovalLevel().name() : null;
        this.rejectionReason = forecast.getRejectionReason();
        this.locked = forecast.getLocked();

        // Update line items
        if (forecast.getLineItems() != null) {
            this.lineItems = new ArrayList<>();
            for (ForecastLineItem item : forecast.getLineItems()) {
                this.lineItems.add(new ForecastLineItemEmbed(item));
            }
        }

        // Update adjustments
        if (forecast.getAdjustmentHistory() != null) {
            this.adjustmentHistory = new ArrayList<>();
            for (ForecastAdjustment adjustment : forecast.getAdjustmentHistory()) {
                this.adjustmentHistory.add(new ForecastAdjustmentEmbed(adjustment));
            }
        }

        this.domainEvents = forecast.getDomainEvents() != null ? new ArrayList<>(forecast.getDomainEvents()) : new ArrayList<>();
    }

    // Embedded class for line items
    public static class ForecastLineItemEmbed {
        private String lineItemId;
        private String forecastId;
        private String tenantId;
        private String name;
        private String description;
        private String category;
        private String type;
        private String productId;
        private String productName;
        private String territoryId;
        private String territoryName;
        private String customerSegmentId;
        private String customerSegmentName;
        private String salesChannel;
        private BigDecimal bestCase;
        private BigDecimal likely;
        private BigDecimal worstCase;
        private String currency;
        private YearMonth period;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal probability;
        private String notes;
        private String owner;
        private Integer sortOrder;
        private Boolean active;

        public ForecastLineItemEmbed() {
        }

        public ForecastLineItemEmbed(ForecastLineItem item) {
            this.lineItemId = item.getLineItemId();
            this.forecastId = item.getForecastId();
            this.tenantId = item.getTenantId();
            this.name = item.getName();
            this.description = item.getDescription();
            this.category = item.getCategory() != null ? item.getCategory().name() : null;
            this.type = item.getType() != null ? item.getType().name() : null;
            this.productId = item.getProductId();
            this.productName = item.getProductName();
            this.territoryId = item.getTerritoryId();
            this.territoryName = item.getTerritoryName();
            this.customerSegmentId = item.getCustomerSegmentId();
            this.customerSegmentName = item.getCustomerSegmentName();
            this.salesChannel = item.getSalesChannel();
            this.bestCase = item.getBestCase();
            this.likely = item.getLikely();
            this.worstCase = item.getWorstCase();
            this.currency = item.getCurrency();
            this.period = item.getPeriod();
            this.quantity = item.getQuantity();
            this.unitPrice = item.getUnitPrice();
            this.probability = item.getProbability();
            this.notes = item.getNotes();
            this.owner = item.getOwner();
            this.sortOrder = item.getSortOrder();
            this.active = item.getActive();
        }

        public ForecastLineItem toDomainModel() {
            return ForecastLineItem.builder()
                    .lineItemId(this.lineItemId)
                    .forecastId(this.forecastId)
                    .tenantId(this.tenantId)
                    .name(this.name)
                    .description(this.description)
                    .category(this.category != null ? ForecastLineItem.ForecastCategory.valueOf(this.category) : null)
                    .type(this.type != null ? ForecastLineItem.LineItemType.valueOf(this.type) : null)
                    .productId(this.productId)
                    .productName(this.productName)
                    .territoryId(this.territoryId)
                    .territoryName(this.territoryName)
                    .customerSegmentId(this.customerSegmentId)
                    .customerSegmentName(this.customerSegmentName)
                    .salesChannel(this.salesChannel)
                    .bestCase(this.bestCase)
                    .likely(this.likely)
                    .worstCase(this.worstCase)
                    .currency(this.currency)
                    .period(this.period)
                    .quantity(this.quantity)
                    .unitPrice(this.unitPrice)
                    .probability(this.probability)
                    .notes(this.notes)
                    .owner(this.owner)
                    .sortOrder(this.sortOrder)
                    .active(this.active)
                    .build();
        }

        // Getters and setters
        public String getLineItemId() { return lineItemId; }
        public void setLineItemId(String lineItemId) { this.lineItemId = lineItemId; }
        public String getForecastId() { return forecastId; }
        public void setForecastId(String forecastId) { this.forecastId = forecastId; }
        public String getTenantId() { return tenantId; }
        public void setTenantId(String tenantId) { this.tenantId = tenantId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getTerritoryId() { return territoryId; }
        public void setTerritoryId(String territoryId) { this.territoryId = territoryId; }
        public String getTerritoryName() { return territoryName; }
        public void setTerritoryName(String territoryName) { this.territoryName = territoryName; }
        public String getCustomerSegmentId() { return customerSegmentId; }
        public void setCustomerSegmentId(String customerSegmentId) { this.customerSegmentId = customerSegmentId; }
        public String getCustomerSegmentName() { return customerSegmentName; }
        public void setCustomerSegmentName(String customerSegmentName) { this.customerSegmentName = customerSegmentName; }
        public String getSalesChannel() { return salesChannel; }
        public void setSalesChannel(String salesChannel) { this.salesChannel = salesChannel; }
        public BigDecimal getBestCase() { return bestCase; }
        public void setBestCase(BigDecimal bestCase) { this.bestCase = bestCase; }
        public BigDecimal getLikely() { return likely; }
        public void setLikely(BigDecimal likely) { this.likely = likely; }
        public BigDecimal getWorstCase() { return worstCase; }
        public void setWorstCase(BigDecimal worstCase) { this.worstCase = worstCase; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public YearMonth getPeriod() { return period; }
        public void setPeriod(YearMonth period) { this.period = period; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public BigDecimal getProbability() { return probability; }
        public void setProbability(BigDecimal probability) { this.probability = probability; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public String getOwner() { return owner; }
        public void setOwner(String owner) { this.owner = owner; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }

    // Embedded class for adjustments
    public static class ForecastAdjustmentEmbed {
        private String adjustmentId;
        private String forecastId;
        private String tenantId;
        private String adjustedBy;
        private String adjusterName;
        private Instant adjustedAt;
        private String type;
        private String reason;
        private String lineItemId;
        private String lineItemName;
        private BigDecimal previousAmount;
        private BigDecimal newAmount;
        private BigDecimal difference;
        private String currency;
        private String comments;
        private String previousVersion;
        private String newVersion;

        public ForecastAdjustmentEmbed() {
        }

        public ForecastAdjustmentEmbed(ForecastAdjustment adjustment) {
            this.adjustmentId = adjustment.getAdjustmentId();
            this.forecastId = adjustment.getForecastId();
            this.tenantId = adjustment.getTenantId();
            this.adjustedBy = adjustment.getAdjustedBy();
            this.adjusterName = adjustment.getAdjusterName();
            this.adjustedAt = adjustment.getAdjustedAt();
            this.type = adjustment.getType() != null ? adjustment.getType().name() : null;
            this.reason = adjustment.getReason();
            this.lineItemId = adjustment.getLineItemId();
            this.lineItemName = adjustment.getLineItemName();
            this.previousAmount = adjustment.getPreviousAmount();
            this.newAmount = adjustment.getNewAmount();
            this.difference = adjustment.getDifference();
            this.currency = adjustment.getCurrency();
            this.comments = adjustment.getComments();
            this.previousVersion = adjustment.getPreviousVersion();
            this.newVersion = adjustment.getNewVersion();
        }

        public ForecastAdjustment toDomainModel() {
            return ForecastAdjustment.builder()
                    .adjustmentId(this.adjustmentId)
                    .forecastId(this.forecastId)
                    .tenantId(this.tenantId)
                    .adjustedBy(this.adjustedBy)
                    .adjusterName(this.adjusterName)
                    .adjustedAt(this.adjustedAt)
                    .type(this.type != null ? ForecastAdjustment.AdjustmentType.valueOf(this.type) : null)
                    .reason(this.reason)
                    .lineItemId(this.lineItemId)
                    .lineItemName(this.lineItemName)
                    .previousAmount(this.previousAmount)
                    .newAmount(this.newAmount)
                    .difference(this.difference)
                    .currency(this.currency)
                    .comments(this.comments)
                    .previousVersion(this.previousVersion)
                    .newVersion(this.newVersion)
                    .build();
        }

        // Getters and setters
        public String getAdjustmentId() { return adjustmentId; }
        public void setAdjustmentId(String adjustmentId) { this.adjustmentId = adjustmentId; }
        public String getForecastId() { return forecastId; }
        public void setForecastId(String forecastId) { this.forecastId = forecastId; }
        public String getTenantId() { return tenantId; }
        public void setTenantId(String tenantId) { this.tenantId = tenantId; }
        public String getAdjustedBy() { return adjustedBy; }
        public void setAdjustedBy(String adjustedBy) { this.adjustedBy = adjustedBy; }
        public String getAdjusterName() { return adjusterName; }
        public void setAdjusterName(String adjusterName) { this.adjusterName = adjusterName; }
        public Instant getAdjustedAt() { return adjustedAt; }
        public void setAdjustedAt(Instant adjustedAt) { this.adjustedAt = adjustedAt; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public String getLineItemId() { return lineItemId; }
        public void setLineItemId(String lineItemId) { this.lineItemId = lineItemId; }
        public String getLineItemName() { return lineItemName; }
        public void setLineItemName(String lineItemName) { this.lineItemName = lineItemName; }
        public BigDecimal getPreviousAmount() { return previousAmount; }
        public void setPreviousAmount(BigDecimal previousAmount) { this.previousAmount = previousAmount; }
        public BigDecimal getNewAmount() { return newAmount; }
        public void setNewAmount(BigDecimal newAmount) { this.newAmount = newAmount; }
        public BigDecimal getDifference() { return difference; }
        public void setDifference(BigDecimal difference) { this.difference = difference; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
        public String getPreviousVersion() { return previousVersion; }
        public void setPreviousVersion(String previousVersion) { this.previousVersion = previousVersion; }
        public String getNewVersion() { return newVersion; }
        public void setNewVersion(String newVersion) { this.newVersion = newVersion; }
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getForecastId() { return forecastId; }
    public void setForecastId(String forecastId) { this.forecastId = forecastId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public YearMonth getStartDate() { return startDate; }
    public void setStartDate(YearMonth startDate) { this.startDate = startDate; }
    public YearMonth getEndDate() { return endDate; }
    public void setEndDate(YearMonth endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getParentForecastId() { return parentForecastId; }
    public void setParentForecastId(String parentForecastId) { this.parentForecastId = parentForecastId; }
    public BigDecimal getTotalBestCase() { return totalBestCase; }
    public void setTotalBestCase(BigDecimal totalBestCase) { this.totalBestCase = totalBestCase; }
    public BigDecimal getTotalLikely() { return totalLikely; }
    public void setTotalLikely(BigDecimal totalLikely) { this.totalLikely = totalLikely; }
    public BigDecimal getTotalWorstCase() { return totalWorstCase; }
    public void setTotalWorstCase(BigDecimal totalWorstCase) { this.totalWorstCase = totalWorstCase; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getTerritory() { return territory; }
    public void setTerritory(String territory) { this.territory = territory; }
    public String getBusinessUnit() { return businessUnit; }
    public void setBusinessUnit(String businessUnit) { this.businessUnit = businessUnit; }
    public List<ForecastLineItemEmbed> getLineItems() { return lineItems; }
    public void setLineItems(List<ForecastLineItemEmbed> lineItems) { this.lineItems = lineItems; }
    public List<ForecastAdjustmentEmbed> getAdjustmentHistory() { return adjustmentHistory; }
    public void setAdjustmentHistory(List<ForecastAdjustmentEmbed> adjustmentHistory) { this.adjustmentHistory = adjustmentHistory; }
    public String getCurrentApprovalLevel() { return currentApprovalLevel; }
    public void setCurrentApprovalLevel(String currentApprovalLevel) { this.currentApprovalLevel = currentApprovalLevel; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public Boolean getLocked() { return locked; }
    public void setLocked(Boolean locked) { this.locked = locked; }
    public List<Object> getDomainEvents() { return domainEvents; }
    public void setDomainEvents(List<Object> domainEvents) { this.domainEvents = domainEvents; }
}
